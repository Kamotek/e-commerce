#!/usr/bin/env bash
set -euo pipefail

# 0) wskazanie miejsca, gdzie jest state Terraform
TF_DIR="$(pwd)"      # lub bezwzględna ścieżka
cd "$TF_DIR"

# 1) Generowanie .env w tym samym katalogu
cat > .env <<EOF
DB_HOST=$(terraform output -raw db_endpoint)
DB_PORT=$(terraform output -raw db_port)
RABBITMQ_HOST=$(terraform output -raw rabbitmq_endpoint)
RABBITMQ_PORT=$(terraform output -raw rabbitmq_port)
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=postgres
JAVA_OPTS="-Xms512m -Xmx1024m"
NETWORK_MODE="bridge"
EOF

# 2) Wczytanie i weryfikacja
source .env
echo "→ DB_HOST=$DB_HOST"
echo "→ RABBITMQ_HOST=$RABBITMQ_HOST"

# 3) Mapowanie i deploy
declare -A DB_MAP=(
  [AuthService]=e_commerce_auth_db
  [BFFService]=e_commerce_bff_db
  [InventoryService]=e_commerce_inventory_db
  [PaymentService]=e_commerce_payment_db
  [NotificationService]=e_commerce_notification_db
  [CatalogService]=e_commerce_catalog_db
  [OrderService]=e_commerce_order_db
  [WelcomeNotificationService]=e_commerce_welcomenotification_db
)


for SERVICE in "${!DB_MAP[@]}"; do
  IMAGE_NAME="$(echo "$SERVICE" | tr '[:upper:]' '[:lower:]')"
  DB_NAME="${DB_MAP[$SERVICE]}"

  echo "=== Deploying $SERVICE ==="
  (cd "$SERVICE" && docker build -t "$IMAGE_NAME" .)

  docker rm -f "$IMAGE_NAME" 2>/dev/null || true

  docker run -d \
    --name "$IMAGE_NAME" \
    --network "$NETWORK_MODE" \
    -e SPRING_DATASOURCE_URL="jdbc:postgresql://${DB_HOST}:${DB_PORT}/${DB_NAME}" \
    -e SPRING_DATASOURCE_USERNAME="$SPRING_DATASOURCE_USERNAME" \
    -e SPRING_DATASOURCE_PASSWORD="$SPRING_DATASOURCE_PASSWORD" \
    -e SPRING_RABBITMQ_HOST="${RABBITMQ_HOST}" \
    -e SPRING_RABBITMQ_PORT="${RABBITMQ_PORT}" \
    -e JAVA_OPTS="$JAVA_OPTS" \
    "$IMAGE_NAME:latest"

  echo "$SERVICE started."
done

echo "All services deployed."

