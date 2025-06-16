#!/usr/bin/env bash
set -euo pipefail

TF_DIR="/home/kamil/Documents/PwrCourses/CloudComputing/e-commerce/AWS"


DB_HOST="$(cd "$TF_DIR" && terraform output -raw db_endpoint)"
DB_PORT="$(cd "$TF_DIR" && terraform output -raw db_port)"
RABBITMQ_ENDPOINT="$(cd "$TF_DIR" && terraform output -raw rabbitmq_endpoint)"
RABBITMQ_HOST="$(echo "$RABBITMQ_ENDPOINT" | sed -E 's#amqps?://([^:]+):?.*#\1#')"
RABBITMQ_PORT="$(cd "$TF_DIR" && terraform output -raw rabbitmq_port)"


SPRING_RABBITMQ_USERNAME="rabbitmq"
SPRING_RABBITMQ_PASSWORD="rabbit123456"


JAVA_OPTS="-Xms512m -Xmx1024m -Djava.net.preferIPv4Stack=true"


NETWORK_MODE="host"


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

  echo
  echo "=== Deploying $SERVICE ==="


  (cd "$SERVICE" && docker build -t "$IMAGE_NAME" .)


  if docker ps -a --format '{{.Names}}' | grep -Eq "^${IMAGE_NAME}$"; then
    docker rm -f "$IMAGE_NAME"
  fi


  docker run -d \
    --name "$IMAGE_NAME" \
    --network $NETWORK_MODE \
    -e SPRING_DATASOURCE_URL="jdbc:postgresql://${DB_HOST}:${DB_PORT}/${DB_NAME}" \
    -e SPRING_DATASOURCE_USERNAME="postgres" \
    -e SPRING_DATASOURCE_PASSWORD="postgres" \
    -e SPRING_RABBITMQ_ADDRESSES="amqps://${SPRING_RABBITMQ_USERNAME}:${SPRING_RABBITMQ_PASSWORD}@${RABBITMQ_HOST}:${RABBITMQ_PORT}" \
    -e SPRING_RABBITMQ_SSL_ENABLED="true" \
    -e JAVA_OPTS="$JAVA_OPTS" \
    "$IMAGE_NAME:latest"

  echo "$SERVICE started as container '$IMAGE_NAME'."
 done

echo "All services deployed."

