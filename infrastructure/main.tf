provider "aws" {
  region = "eu-central-1"
}

resource "aws_vpc" "main" {
  cidr_block           = "10.0.0.0/16"
  enable_dns_hostnames = true
  enable_dns_support   = true

  tags = {
    name = "main-vpc"
  }
}


resource "aws_internet_gateway" "igw" {
  vpc_id = aws_vpc.main.id

  tags = {
    name = "main-igw"
  }
}

resource "aws_route_table" "public_rt" {
  vpc_id = aws_vpc.main.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.igw.id
  }

  tags = {
    Name = "main-public-rt"
  }
}


resource "aws_subnet" "public" {

  for_each = toset(slice(data.aws_availability_zones.available.names, 0, 2))


  vpc_id            = aws_vpc.main.id
  availability_zone = each.value

  cidr_block              = cidrsubnet(aws_vpc.main.cidr_block, 8, index(keys(aws_subnet.public), each.key) + 1)
  map_public_ip_on_launch = true
  tags = {
    name = "main-public-${each.value}"
  }
}

resource "aws_eip" "nat_eip" {
  domain = "vpc"
  tags = {
    name = "nat-gateway-eip"
  }
}


resource "aws_nat_gateway" "nat_gw" {
  allocation_id = aws_eip.nat_eip.id
  subnet_id     = values(aws_subnet.public)[0].id

  tags = {
    Name = "main-nat-gw"
  }

  depends_on = [aws_internet_gateway.igw]
}

resource "aws_subnet" "private" {
  for_each = toset(slice(data.aws_availability_zones.available.names, 0, 2))

  vpc_id                  = aws_vpc.main.id
  availability_zone       = each.value
  map_public_ip_on_launch = false

  cidr_block = cidrsubnet(aws_vpc.main.cidr_block, 8, index(keys(aws_subnet.private), each.key) + 101)

  tags = {
    Name = "private-subnet-${each.value}"
  }
}

resource "aws_route_table" "private_rt" {
  vpc_id = aws_vpc.main.id

  route {
    cidr_block     = "0.0.0.0/0"
    nat_gateway_id = aws_nat_gateway.nat_gw.id
  }

  tags = {
    Name = "main-private-rt"
  }

}
resource "aws_route_table_association" "public_assoc" {
  for_each       = aws_subnet.public
  subnet_id      = each.value.id
  route_table_id = aws_route_table.public_rt.id
}

resource "aws_route_table_association" "private_assoc" {
  for_each       = aws_subnet.private
  subnet_id      = each.value.id
  route_table_id = aws_route_table.private_rt.id
}

# ===

data "aws_availability_zones" "available" {
  state = "available"
}


resource "aws_db_subnet_group" "rds_subnet_group" {
  name       = "rds-subnet-group"
  subnet_ids = [for subnet in aws_subnet.private : subnet.id]

  tags = {
    Name = "My RDS Subnet Group"
  }
}


# ---



locals {
  microservices = toset([
    "auth-service",
    "order-service",
    "catalog-service",
    "payment-service",
    "notification-service",
    "inventory-service",
    "welcome-notification-service",
    "bff-service"
  ])
}

module "microservice_db" {
  source = "./modules/rds_instance"

  for_each = local.microservices

  service_name         = each.key
  vpc_id               = aws_vpc.main.id
  db_subnet_group_name = aws_db_subnet_group.rds_subnet_group.name

}



### ---

resource "aws_ecr_repository" "microservice_repos" {
  for_each = local.microservices

  name = each.key

  image_scanning_configuration {
    scan_on_push = true
  }

  image_tag_mutability = "IMMUTABLE"

  tags = {
    Project     = "Microservices"
    ManagedBy   = "Terraform"
    ServiceName = each.key
  }
}

resource "aws_ecr_lifecycle_policy" "repo_policies" {
  for_each = local.microservices

  repository = aws_ecr_repository.microservice_repos[each.key].name

  policy = jsonencode({
    rules = [
      {
        rulePriority = 1,
        description  = "Keep last 10 tagged images",
        selection = {
          tagStatus     = "tagged",
          tagPrefixList = ["v"],
          countType     = "imageCountMoreThan",
          countNumber   = 10
        },
        action = {
          type = "expire"
        }
      },
      {
        rulePriority = 2,
        description  = "Expire untagged images after 7 days",
        selection = {
          tagStatus   = "untagged",
          countType   = "sinceImagePushed",
          countUnit   = "days",
          countNumber = 7
        },
        action = {
          type = "expire"
        }
      }
    ]
  })
}




# ==============



resource "random_password" "rabbitmq_password" {
  length           = 24
  special          = true
  override_special = "!#$%&()*+,-./:;<=>?@[]^_`{|}~"
}


resource "random_password" "jwt_secret" {
  length  = 64
  special = true
}


resource "random_password" "mail_password" {
  length  = 24
  special = true
}



# -----------------------------------------------------------------
resource "aws_secretsmanager_secret" "app_secrets" {
  name = "e-commerce-app-secrets"
}


# -----------------------------------------------------------------
resource "aws_secretsmanager_secret_version" "app_secrets_version" {
  secret_id = aws_secretsmanager_secret.app_secrets.id

  secret_string = jsonencode({
    RABBITMQ_USER     = "mainuser"
    RABBITMQ_PASSWORD = random_password.rabbitmq_password.result
    JWT_SECRET        = random_password.jwt_secret.result
    MAIL_USERNAME     = "projekttesting12657@gmail.com"
    MAIL_PASSWORD     = random_password.mail_password.result
  })
}

