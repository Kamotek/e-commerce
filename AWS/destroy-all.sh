#!/usr/bin/env bash
set -euo pipefail

# Upewnij się, że jesteś w katalogu z terraformem:
# cd /ścieżka/do/terraform/projektu

# (1) Jeden raz inicjalizujemy Terraform
terraform init

# Lista zasobów do stopniowego niszczenia
resources=(
  'postgresql_database.svc_dbs["e_commerce_auth_db"]'
  'postgresql_database.svc_dbs["e_commerce_bff_db"]'
  'postgresql_database.svc_dbs["e_commerce_catalog_db"]'
  'postgresql_database.svc_dbs["e_commerce_inventory_db"]'
  'postgresql_database.svc_dbs["e_commerce_notification_db"]'
  'postgresql_database.svc_dbs["e_commerce_order_db"]'
  'postgresql_database.svc_dbs["e_commerce_payment_db"]'
  'postgresql_database.svc_dbs["e_commerce_welcomenotification_db"]'
  'aws_db_instance.postgres'
  'aws_db_subnet_group.rds_subnets_new'
  'module.vpc'
  'aws_mq_broker.rabbitmq'
)

for res in "${resources[@]}"; do
  echo
  echo ">>> terraform destroy -target=${res} -auto-approve"
  terraform destroy -target="${res}" -auto-approve
done

echo
echo "✅ Wszystkie zasoby zostały usunięte."
