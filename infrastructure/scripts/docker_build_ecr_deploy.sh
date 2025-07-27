#!/bin/bash

set -e

AWS_ACCOUNT_ID="359850485340"
AWS_REGION="eu-central-1"
declare -A SERVICE_MAP=(
    ["NotificationService"]="notification-service"
    ["BFFService"]="bff-service"
    ["AuthService"]="auth-service"
    ["OrderService"]="order-service"
    ["CatalogService"]="catalog-service"
    ["PaymentService"]="payment-service"
    ["InventoryService"]="inventory-service"
    ["WelcomeNotificationService"]="welcomenotification-service"
)

echo "Logging in..."
aws ecr get-login-password --region $AWS_REGION | docker login --username AWS --password-stdin $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com
echo "Logging finishedf..."


# Over there we create two separate tags because we want one which is completely unique and it is also pretty cool if we can call our images as :latest later on
for code_name in "${!SERVICE_MAP[@]}"; do
    infra_name=${SERVICE_MAP[$code_name]}

    echo "Processing service: $code_name -> $infra_name"

    if [ ! -d "$code_name" ]; then
        echo "ERROR: Directory $code_name not found. Skipping."
        continue
    fi
    
    REPO_URI="$AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/$infra_name"
    IMAGE_TAG=$(date +%s)

    echo "Building image for $code_name..."
    docker build -t $REPO_URI:$IMAGE_TAG -t $REPO_URI:latest -f "$code_name/Dockerfile" "$code_name/"

    echo "Pushing image to ECR repo: $infra_name..."
    docker push $REPO_URI:$IMAGE_TAG
    docker push $REPO_URI:latest
    echo "$code_name pushed successfully to $infra_name with tag: $IMAGE_TAG"
    echo "------------------------"
done

echo "All images have been built and pushed successfully."


