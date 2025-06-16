# --------------------------- Stage 1: Infrastructure ---------------------------
terraform {
  required_version = ">= 1.3.0"
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.97.0"
    }
    postgresql = {
      source  = "cyrilgdn/postgresql"
      version = ">= 1.25.0"
    }
  }
}

provider "aws" {
  region = "eu-central-1"
}

locals {
  db_names = [
    "e_commerce_auth_db",
    "e_commerce_bff_db",
    "e_commerce_inventory_db",
    "e_commerce_payment_db",
    "e_commerce_notification_db",
    "e_commerce_catalog_db",
    "e_commerce_order_db",
    "e_commerce_welcomenotification_db",
  ]
}


module "vpc" {
  source  = "terraform-aws-modules/vpc/aws"
  version = "4.0.2"

  name                 = "ecommerce-vpc"
  cidr                 = "10.0.0.0/16"
  azs                  = ["eu-central-1a", "eu-central-1b"]
  public_subnets       = ["10.0.1.0/24", "10.0.2.0/24"]
  private_subnets      = ["10.0.3.0/24", "10.0.4.0/24"]

  enable_nat_gateway   = false
  enable_dns_hostnames = true
  enable_dns_support   = true
}


resource "aws_security_group" "rds_sg" {
  name        = "ecommerce-rds-sg"
  description = "Allow Postgres access for testing"
  vpc_id      = module.vpc.vpc_id

  ingress {
    from_port   = 5432
    to_port     = 5432
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
    lifecycle {
    create_before_destroy = true

  }
  
}




resource "aws_db_subnet_group" "rds_subnets_new" {
  name       = "ecommerce-rds-subnet-group-v2"
  subnet_ids = module.vpc.public_subnets

  lifecycle {
    create_before_destroy = true
  }
}


resource "aws_db_instance" "postgres" {
  identifier             = "ecommerce-postgres"
  engine                 = "postgres"
  engine_version         = "15"
  instance_class         = "db.t3.micro"
  allocated_storage      = 20
  username               = "postgres"
  password               = "postgres"
  skip_final_snapshot    = true
  publicly_accessible    = true
  vpc_security_group_ids = [aws_security_group.rds_sg.id]
  db_subnet_group_name   = aws_db_subnet_group.rds_subnets_new.name
  apply_immediately      = true

  depends_on = [
    aws_security_group.rds_sg,
    aws_db_subnet_group.rds_subnets_new
  ]
}



resource "aws_mq_broker" "rabbitmq" {
  broker_name                = "ecommerce-rabbitmq"
  engine_type                = "RabbitMQ"
  engine_version             = "3.13"
  host_instance_type         = "mq.t3.micro"
  publicly_accessible        = true
  auto_minor_version_upgrade = true

  user {
    username = "rabbitmq"
    password = "rabbit123456"
  }

  lifecycle {
    prevent_destroy = true
    ignore_changes  = [broker_name]
  }
}


output "db_endpoint" {
  value       = aws_db_instance.postgres.address
  description = "RDS PostgreSQL endpoint"
}
output "db_port" {
  value       = aws_db_instance.postgres.port
  description = "RDS PostgreSQL port"
}
output "rabbitmq_endpoint" {
  value       = aws_mq_broker.rabbitmq.instances[0].endpoints[0]
  description = "RabbitMQ endpoint"
}
output "rabbitmq_port" {
  value       = 5671
  description = "RabbitMQ SSL port"
}

# --------------------------- Etap numer dwa ---------------------------


provider "postgresql" {
  alias           = "rds"
  host            = aws_db_instance.postgres.address
  port            = aws_db_instance.postgres.port
  database        = "postgres"
  username        = aws_db_instance.postgres.username
  password        = aws_db_instance.postgres.password
  sslmode         = "verify-full"           # lub "verify-ca"
  sslrootcert     = "${path.module}/rds-combined-ca-bundle.pem"
  connect_timeout = 60
}


resource "postgresql_database" "svc_dbs" {
  for_each = toset(local.db_names)
  provider = postgresql.rds
  name     = each.value

  depends_on = [aws_db_instance.postgres]
}



