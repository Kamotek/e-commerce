# docker-deploy.sh
#!/usr/bin/env bash
set -euo pipefail

# Ścieżka do katalogu z Terraform (wyciąganie account/region)
TF_DIR="/home/kamil/Documents/PwrCourses/CloudComputing/e-commerce/AWS"

# Ustaw AWS region i account
AWS_REGION="$(cd "$TF_DIR" && terraform output -raw aws_region 2>/dev/null || echo eu-central-1)"
AWS_ACCOUNT_ID="$(aws sts get-caller-identity --query Account --output text)"

ecr_prefix="${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com"

# Logowanie do ECR
echo "Logging into ECR $AWS_REGION"
aws ecr get-login-password --region "$AWS_REGION" | docker login --username AWS --password-stdin "${ecr_prefix}"

# Mapowanie katalogów usług na nazwy obrazów (wszystkie lowercase)
declare -A SERVICES=(
  [AuthService]=authservice
  [BFFService]=bffservice
  [CatalogService]=catalogservice
  [InventoryService]=inventoryservice
  [OrderService]=orderservice
  [PaymentService]=paymentservice
  [NotificationService]=notificationservice
  [WelcomeNotificationService]=welcomenotificationservice
)

echo
for SERVICE_DIR in "${!SERVICES[@]}"; do
  IMAGE_NAME="${SERVICES[$SERVICE_DIR]}"
  REPO_URI="${ecr_prefix}/${IMAGE_NAME}:latest"

  # Utworzenie repozytorium ECR, jeśli nie istnieje
  if ! aws ecr describe-repositories --repository-names "$IMAGE_NAME" --region "$AWS_REGION" >/dev/null 2>&1; then
    echo "Creating ECR repository: $IMAGE_NAME"
    aws ecr create-repository --repository-name "$IMAGE_NAME" --region "$AWS_REGION" >/dev/null
  fi

  echo "Building Docker image: $SERVICE_DIR -> $IMAGE_NAME"
  (cd "$SERVICE_DIR" && docker build -t "$IMAGE_NAME:latest" .)

  echo "Tagging and pushing to ECR: $REPO_URI"
  docker tag "$IMAGE_NAME:latest" "$REPO_URI"
  docker push "$REPO_URI"
  echo
 done

echo "All images pushed to ECR."
