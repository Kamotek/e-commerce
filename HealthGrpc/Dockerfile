FROM python:3.11-slim

WORKDIR /app
ENV PYTHONDONTWRITEBYTECODE=1
ENV PYTHONUNBUFFERED=1

RUN apt-get update && apt-get install -y build-essential --no-install-recommends && rm -rf /var/lib/apt/lists/*

COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

COPY protos ./protos
COPY main.py .

RUN python -m grpc_tools.protoc -I./protos --python_out=. --grpc_python_out=. protos/health.proto

EXPOSE 50052

CMD ["python", "main.py"]