resource "aws_security_group" "this" {
  name        = "${var.service_name}-rds-sg"
  description = "Security group for ${var.service_name} RDS"
  vpc_id      = var.vpc_id

  ingress {
    from_port   = 5432
    to_port     = 5432
    protocol    = "tcp"
    cidr_blocks = ["10.0.0.0/16"] 
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "random_password" "this" {
  length  = 16
  special = false
}

resource "aws_secretsmanager_secret" "this" {
  name = "${var.service_name}-db-password"
}

resource "aws_secretsmanager_secret_version" "this" {
  secret_id     = aws_secretsmanager_secret.this.id
  secret_string = random_password.this.result
}

resource "aws_db_instance" "this" {
  identifier           = "${var.service_name}-db"
  engine               = "postgres"
  engine_version       = "15.3"
  instance_class       = var.instance_class
  allocated_storage    = 20
  
  db_name              = "${var.service_name}db"
  username             = "${var.service_name}user"
  password             = random_password.this.result 
  
  db_subnet_group_name   = var.db_subnet_group_name
  vpc_security_group_ids = [aws_security_group.this.id]
  publicly_accessible    = false
  skip_final_snapshot    = true
}
