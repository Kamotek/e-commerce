# Kubernetes Setup for E-Commerce Project

This directory contains the Kubernetes configuration files for deploying the e-commerce application services. The setup replicates the existing Docker Compose configuration.

## Prerequisites

- Install [Minikube](https://minikube.sigs.k8s.io/docs/start/) to run Kubernetes locally.
- Install [kubectl](https://kubernetes.io/docs/tasks/tools/install-kubectl/) to interact with your Kubernetes cluster.

## Installation Instructions

1. **Install Minikube:**
   - For Windows: Use Chocolatey:
     ```
     choco install minikube
     ```
   - For macOS: Use Homebrew:
     ```
     brew install minikube
     ```
   - For Linux: Download the binary from the [Minikube releases page](https://github.com/kubernetes/minikube/releases).

2. **Start Minikube:**
   ```
   minikube start
   ```

3. **Verify the installation:**
   ```
   kubectl get nodes
   ```

4. **Apply the Kubernetes configurations:**
   ```
   kubectl apply -f kubernetes/
   ```

This command will deploy all the services defined in the Kubernetes configuration files located in this directory.

## Services Overview

- **PostgreSQL**: Database service for storing application data.
- **RabbitMQ**: Message broker for handling asynchronous communication between services.
- **Auth Service**: Manages user authentication and authorization.
- **Catalog Service**: Handles product catalog management.
- **Inventory Service**: Manages inventory levels and stock.
- **Order Service**: Processes customer orders.
- **Notification Service**: Sends notifications to users.
- **Payment Service**: Manages payment processing.
- **Welcome Notification Service**: Sends welcome notifications to new users.
- **BFF Service**: Backend for frontend, aggregates data from multiple services.
- **NGINX**: Acts as a reverse proxy and load balancer.
- **Invoice Service**: Manages invoice generation and processing.
- **Health GRPC Service**: Provides health check endpoints for monitoring.

## Configuration Files

Each service has its own deployment and service configuration files, which define how the services are deployed and how they communicate with each other. Additionally, secrets and config maps are defined for managing sensitive information and configuration data.

Make sure to customize the configuration files as needed for your environment before deploying.