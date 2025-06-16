#!/bin/bash
set -e

function create_database() {
    local database=$1
    echo "  Creating database '$database'"
    psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
        SELECT 'CREATE DATABASE $database'
        WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = '$database')\gexec
EOSQL
}

if [ -n "$POSTGRES_MULTIPLE_DBS" ]; then
    echo "Multiple database creation requested: $POSTGRES_MULTIPLE_DBS"
    for db in $(echo $POSTGRES_MULTIPLE_DBS | tr ',' ' '); do
        create_database $db
    done
    echo "Multiple databases created"
fi
