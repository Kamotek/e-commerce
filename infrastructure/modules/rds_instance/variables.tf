variable "service_name" {
  description = "Nazwa mikroserwisu, dla którego tworzona jest baza danych"
  type        = string
}

variable "vpc_id" {
  description = "ID VPC, w którym zostaną utworzone zasoby"
  type        = string
}

variable "db_subnet_group_name" {
  description = "Nazwa grupy podsieci dla RDS"
  type        = string
}

variable "instance_class" {
  description = "Klasa instancji RDS"
  type        = string
  default     = "db.t3.micro"
}
