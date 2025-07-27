output "db_instance_endpoint" {
  description = "The connection endpoint for the RDS instance."
  value       = aws_db_instance.this.endpoint
  sensitive   = true
}

output "db_instance_username" {
  description = "The username for the RDS instance."
  value       = aws_db_instance.this.username
  sensitive   = true
}

output "db_secret_arn" {
  description = "The ARN of the secret in AWS Secrets Manager."
  value       = aws_secretsmanager_secret.this.arn
}
