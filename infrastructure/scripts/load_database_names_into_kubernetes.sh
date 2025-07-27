#!/bin/bash

set -e



# Check if there's a command jq, which is json processor and install it
if ! command -v jq >/dev/null 2>&1
then
    echo "<the_command> could not be found"
    dnf install jq
fi



TEMPLATE_FILE="../kubernetes/config.template.yaml"
GENERATED_FILE="../kubernetes/config.generated.yaml"

echo "Step 1: Fetching outputs from Terraform..."

# Fetch terraform outputs in json format
TF_OUTPUTS=$(terraform output -json db_endpoints | tr -d ' "')

# check if there are outputs indeed
if [ -z "$TF_OUTPUTS" ]; then
    echo "ERROR: Could not fetch Terraform outputs. Make sure you have run 'terraform apply'."
    exit 1
fi

echo "Outputs fetched successfully."

# we create new file based on previously created template
cp $TEMPLATE_FILE $GENERATED_FILE

echo "Step 2: Replacing placeholders in the template..."


# for each database we use
# we format outputs and replace values inside generated file using sed command
for key in $(echo $TF_OUTPUTS | jq -r 'keys[]'); do
    value=$(echo $TF_OUTPUTS | jq -r --arg key "$key" '.[$key]')
    
    placeholder_key=$(echo "$key" | tr '-' '_' | tr '[:lower:]' '[:upper:]')
    placeholder="__${placeholder_key}_DB_HOST__"

    echo "   - Replacing ${placeholder} with ${value}"
    
    sed -i.bak "s|${placeholder}|${value}|g" $GENERATED_FILE || :
done

echo "Replacing service account ARN placeholder..."
TF_SA_ARN=$(terraform output -raw app_service_account_role_arn)
if [ -z "$TF_SA_ARN" ]; then
    echo "Could not fetch ARN..."
    exit 1
fi
sed -i.bak "s|__APP_SERVICE_ACCOUNT_ROLE_ARN__|${TF_SA_ARN}|g" $GENERATED_FILE

echo "Template processed."

echo "Step 3: Applying the generated ConfigMap to Kubernetes..."

kubectl apply -f $GENERATED_FILE

echo "Success! ConfigMap '$GENERATED_FILE' has been applied to the cluster."

rm $GENERATED_FILE
rm $GENERATED_FILE.bak

echo "Done."
