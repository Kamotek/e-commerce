import os
import time
import socket
import json
import logging
import threading
from concurrent import futures

import requests
import grpc

import health_pb2
import health_pb2_grpc

from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware

import uvicorn
from grpc_reflection.v1alpha import reflection

LOG = logging.getLogger("health-grpc")
LOG.setLevel(logging.INFO)
handler = logging.StreamHandler()
handler.setFormatter(logging.Formatter("%(asctime)s %(levelname)s %(message)s"))
LOG.addHandler(handler)

DEFAULT_TARGETS = os.environ.get(
    "HEALTH_TARGETS",
    ",".join([
        "http://auth-service:8080",
        "http://catalog-service:8081",
        "http://inventory-service:8082",
        "http://order-service:8083",
        "http://notification-service:8084",
        "http://payment-service:8086",
        "http://welcome-notification-service:8087",
        "tcp://postgres:5432"
    ])
)
MONITOR_INTERVAL = int(os.environ.get("HEALTH_INTERVAL_SECONDS", "10"))
DEFAULT_TIMEOUT = int(os.environ.get("HEALTH_TIMEOUT_SECONDS", "3"))

TARGETS = [t.strip() for t in DEFAULT_TARGETS.split(",") if t.strip()]

HTTP_PORT = int(os.environ.get("HEALTH_HTTP_PORT", "8088"))

def _tcp_check(host: str, port: int, timeout: int):
    start = time.time()
    try:
        with socket.create_connection((host, port), timeout=timeout):
            rtt = int((time.time() - start) * 1000)
            return True, "tcp_ok", rtt
    except Exception as e:
        rtt = int((time.time() - start) * 1000)
        return False, f"tcp_error: {e}", rtt

def _http_check(url: str, timeout: int):
    url = url.rstrip("/")
    candidates = [url + "/actuator/health", url + "/"]
    last_err = "no_attempt"
    for u in candidates:
        start = time.time()
        try:
            r = requests.get(u, timeout=timeout)
            rtt = int((time.time() - start) * 1000)
            if r.status_code == 200:
                try:
                    j = r.json()
                    status = j.get("status", "").upper()
                    healthy = status in ("UP", "OK") or r.status_code == 200
                    return healthy, json.dumps(j), rtt
                except Exception:
                    return True, f"status=200;url={u}", rtt
            else:
                return False, f"status_code={r.status_code};url={u}", rtt
        except Exception as e:
            last_err = str(e)
    return False, f"http_error: {last_err}", int(timeout * 1000)

def _amqp_check(target: str, timeout: int):
    try:
        _, rest = target.split("://", 1)
        host, port = rest.split(":", 1)
        return _tcp_check(host, int(port), timeout)
    except Exception as e:
        return False, f"amqp_parse_error: {e}", int(timeout * 1000)

def check_target(target: str, timeout: int = DEFAULT_TIMEOUT):
    if target.startswith("http://") or target.startswith("https://"):
        return _http_check(target, timeout)
    if target.startswith("tcp://"):
        try:
            _, rest = target.split("://", 1)
            host, port = rest.split(":", 1)
            return _tcp_check(host, int(port), timeout)
        except Exception as e:
            return False, f"tcp_parse_error: {e}", int(timeout * 1000)
    if target.startswith("amqp://"):
        return _amqp_check(target, timeout)
    return _http_check("http://" + target, timeout)

class HealthCheckerServicer(health_pb2_grpc.HealthCheckerServicer):
    def Check(self, request, context):
        timeout = request.timeout_seconds or DEFAULT_TIMEOUT
        if not request.targets:
            target = TARGETS[0] if TARGETS else ""
        else:
            target = request.targets[0]
        healthy, detail, rtt = check_target(target, timeout)
        return health_pb2.HealthResponse(
            target=target,
            healthy=healthy,
            detail=detail,
            rtt_ms=rtt,
            timestamp=int(time.time() * 1000)
        )

    def Watch(self, request, context):
        timeout = request.timeout_seconds or DEFAULT_TIMEOUT
        targets = request.targets if request.targets else TARGETS
        for target in targets:
            healthy, detail, rtt = check_target(target, timeout)
            yield health_pb2.HealthResponse(
                target=target,
                healthy=healthy,
                detail=detail,
                rtt_ms=rtt,
                timestamp=int(time.time() * 1000)
            )

def background_monitor(stop_event: threading.Event):
    LOG.info("Background monitor started, targets=%s interval=%ss", TARGETS, MONITOR_INTERVAL)
    while not stop_event.wait(MONITOR_INTERVAL):
        for t in TARGETS:
            healthy, detail, rtt = check_target(t, DEFAULT_TIMEOUT)
            level = logging.INFO if healthy else logging.WARNING
            LOG.log(level, "target=%s healthy=%s rtt=%dms detail=%s", t, healthy, rtt, detail)

app = FastAPI(title="HealthGrpc HTTP API")

@app.get("/health")
def http_health():
    results = []
    for t in TARGETS:
        healthy, detail, rtt = check_target(t, DEFAULT_TIMEOUT)
        results.append({
            "target": t,
            "healthy": bool(healthy),
            "detail": detail,
            "rtt_ms": int(rtt),
            "timestamp": int(time.time() * 1000)
        })
    return {"targets": results, "timestamp": int(time.time() * 1000)}

def _start_http_server():
    uvicorn.run(app, host="0.0.0.0", port=HTTP_PORT, log_level="info")

def serve():
    http_thread = threading.Thread(target=_start_http_server, daemon=True)
    http_thread.start()
    LOG.info("Started HTTP health endpoint on port %s", HTTP_PORT)

    grpc_port = int(os.environ.get("HEALTH_GRPC_PORT", "50052"))
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=8))
    health_pb2_grpc.add_HealthCheckerServicer_to_server(HealthCheckerServicer(), server)

    SERVICE_NAMES = ['health.HealthChecker', reflection.SERVICE_NAME]
    reflection.enable_server_reflection(SERVICE_NAMES, server)

    bind = f"[::]:{grpc_port}"
    server.add_insecure_port(bind)

    stop_event = threading.Event()
    t = threading.Thread(target=background_monitor, args=(stop_event,), daemon=True)
    t.start()

    server.start()
    LOG.info("gRPC Health server started on %s (monitor interval %ss)", bind, MONITOR_INTERVAL)
    try:
        server.wait_for_termination()
    except KeyboardInterrupt:
        LOG.info("Shutting down...")
        stop_event.set()
        server.stop(0)

def start_grpc_server():
    grpc_port = int(os.environ.get("HEALTH_GRPC_PORT", "50052"))
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=8))
    health_pb2_grpc.add_HealthCheckerServicer_to_server(HealthCheckerServicer(), server)
    SERVICE_NAMES = ['health.HealthChecker', reflection.SERVICE_NAME]
    reflection.enable_server_reflection(SERVICE_NAMES, server)
    bind = f"[::]:{grpc_port}"
    server.add_insecure_port(bind)
    stop_event = threading.Event()
    t = threading.Thread(target=background_monitor, args=(stop_event,), daemon=True)
    t.start()
    server.start()
    LOG.info("gRPC Health server started on %s (monitor interval %ss)", bind, MONITOR_INTERVAL)
    server.wait_for_termination()


if __name__ == "__main__":


    app.add_middleware(
        CORSMiddleware,
        allow_origins=["*"],  
        allow_credentials=True,
        allow_methods=["*"],
        allow_headers=["*"],
    )
    grpc_thread = threading.Thread(target=start_grpc_server, daemon=True)
    grpc_thread.start()
    LOG.info("Started gRPC server thread")
    uvicorn.run(app, host="0.0.0.0", port=HTTP_PORT, log_level="info")