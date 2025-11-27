#!/bin/bash
set -e

# Ustaw środowisko Dockera na Minikube
eval $(minikube docker-env)

# Katalog główny projektu
PROJECT_ROOT="$(dirname "$(dirname "$(pwd)")")"

# Lista katalogów z Dockerfile
SERVICES=(
  AuthService
  CatalogService
  InventoryService
  OrderService
  NotificationService
  PaymentService
  WelcomeNotificationService
  BFFService
  InvoiceService
  HealthGrpc
)

# Budowanie obrazów
for SERVICE in "${SERVICES[@]}"; do
  IMAGE_NAME=$(echo "$SERVICE" | tr '[:upper:]' '[:lower:]')
  SERVICE_PATH="$PROJECT_ROOT/$SERVICE"
  echo "Building $IMAGE_NAME:latest from $SERVICE_PATH"
  docker build -t "$IMAGE_NAME:latest" "$SERVICE_PATH"
done

echo "All images built for Minikube Docker!"