output "database_endpoints" {
  description = "Map of microservice names to their RDS endpoints."
  value = {
    for service, db in module.microservice_db :
    service => db.db_instance_endpoint
  }
  sensitive = true
}

output "database_secrets_arn" {
  description = "Map of microservice names to their Secrets Manager ARN."
  value = {
    for service, db in module.microservice_db :
    service => db.db_secret_arn

  }
}


# ===  outputs related to ASCP which helps us to manage credentials in secure way

output "app_service_account_role_arn" {
  value = aws_iam_role.app_service_account_role.arn
}

# ---


output "app_secrets_arn" {
  description = "The ARN of the main application secrets (RabbitMQ, JWT, etc.)"
  value       = aws_secretsmanager_secret.app_secrets.arn
}

