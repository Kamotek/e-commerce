#!/bin/bash
# scripts/load_secrets_into_kubernetes.sh
set -e
TEMPLATE_FILE="../kubernetes/secret-provider.template.yaml"
GENERATED_FILE="../kubernetes/secret-provider.generated.yaml"

echo "Fetching secret ARNs from Terraform..."
TF_OUTPUTS=$(terraform output -json database_secrets_arn)

if [ -z "$TF_OUTPUTS" ]; then
	echo "ERROR: Could not fetch Terraform secret ARNs."
        exit 1
fi

cp $TEMPLATE_FILE $GENERATED_FILE

echo "Replacing placeholders in secret provider template..."

for key in $(echo $TF_OUTPUTS | jq -r 'keys[]'); do
	value=$(echo $TF_OUTPUTS | jq -r --arg key '.[$key]')

        placeholder_key=$(echo "$key" | tr '-' '_' | tr '[:lower:]' '[:upper:]')
        placeholder="__${placeholder_key}_SECRET_ARN__"

        echo "   - Replacing ${placeholder} with ${value}"
        sed -i.bak "s|${placeholder}|${value}|g" $GENERATED_FILE
done

echo "   - Replacing App Secrets ARN placeholder"
 TF_APP_SECRETS_ARN=$(terraform output -raw app_secrets_arn)
 if [ -z "$TF_APP_SECRETS_ARN" ]; then
     echo "ERROR: Could not fetch app_secrets_arn."
     exit 1
 fi
 sed -i.bak "s|__APP_SECRETS_ARN__|${TF_APP_SECRETS_ARN}|g" $GENERATED_FILE

echo "Applying the generated SecretProviderClass to Kubernetes..."
kubectl apply -f $GENERATED_FILE

rm $GENERATED_FILE $GENERATED_FILE.bak
echo "Done."

