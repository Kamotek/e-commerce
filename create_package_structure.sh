#!/bin/bash

if [ -z "$1" ]; then
  echo "Użycie: $0 <NazwaMikroserwisu>"
  exit 1
fi

SERVICE_NAME=$1
BASE_PACKAGE="com.${SERVICE_NAME,,}" 
SRC_DIR="${SERVICE_NAME}/src/main/java"
RESOURCES_DIR="${SERVICE_NAME}/src/main/resources"
TEST_DIR="${SERVICE_NAME}/src/test/java"

mkdir -p "${SRC_DIR}/${BASE_PACKAGE}/application/command/handler"
mkdir -p "${SRC_DIR}/${BASE_PACKAGE}/application/command/model"
mkdir -p "${SRC_DIR}/${BASE_PACKAGE}/application/query/handler"
mkdir -p "${SRC_DIR}/${BASE_PACKAGE}/application/query/model"
mkdir -p "${SRC_DIR}/${BASE_PACKAGE}/domain/model"
mkdir -p "${SRC_DIR}/${BASE_PACKAGE}/domain/repository"
mkdir -p "${SRC_DIR}/${BASE_PACKAGE}/infrastructure/configuration"
mkdir -p "${SRC_DIR}/${BASE_PACKAGE}/infrastructure/controller"
mkdir -p "${SRC_DIR}/${BASE_PACKAGE}/infrastructure/persistence/entity"
mkdir -p "${SRC_DIR}/${BASE_PACKAGE}/infrastructure/persistence/repository"
mkdir -p "${SRC_DIR}/${BASE_PACKAGE}/infrastructure/messaging/producer"
mkdir -p "${SRC_DIR}/${BASE_PACKAGE}/infrastructure/messaging/consumer"
mkdir -p "${RESOURCES_DIR}"
mkdir -p "${TEST_DIR}/${BASE_PACKAGE}"

echo "Struktura pakietów dla mikroserwisu '${SERVICE_NAME}' została utworzona."
