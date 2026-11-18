#!/usr/bin/env bash

set -ex

unpack() {
  tar -xvf in/foodb_2020_4_7_csv.tar.gz
}

export PGHOST=localhost
export PGUSER=postgres
export PGPORT=5432
export PGDATABASE=foodb
export PGPASSWORD=postgres
export DB_USER=scientist
export DB_PASSWORD=$DB_USER

function delete_db() {
    dropdb --echo $PGDATABASE
}

function create_new_db() {
    # Run as Superuser
    createdb --echo --encoding='utf-8'
}

function delete_db_user() {
    # Run as Superuser
    psql -c "DROP ROLE $DB_USER;"
}

function create_db_user() {
    # Run as Superuser
    psql <<EOF
    CREATE USER $DB_USER WITH
    NOSUPERUSER NOCREATEDB NOCREATEROLE NOINHERIT NOREPLICATION NOBYPASSRLS
    CONNECTION LIMIT 256
    PASSWORD '$DB_PASSWORD';

    GRANT CONNECT, CREATE ON DATABASE $PGDATABASE TO $DB_USER;
EOF
}

function create_schema() {
    # Run as App User
    PGPASSWORD=$DB_PASSWORD psql -U $DB_USER <<EOF
    CREATE SCHEMA IF NOT EXISTS foodb;

    GRANT ALL PRIVILEGES ON SCHEMA foodb TO $DB_USER;
    ALTER SCHEMA foodb OWNER TO $DB_USER;
EOF
}

function create_tables() {
    PGPASSWORD=$DB_PASSWORD psql -U $DB_USER -f ../db/foodb/src/main/resources/foodb_schema.sql
}

function create_db() {
    create_new_db
    create_db_user
    create_schema
    create_tables
}

function recreate_db() {
    delete_db
    create_new_db
    create_db_user
    create_schema
    create_tables
}

case "$1" in
    unpack)
        unpack
        ;;
    create_db)
        create_db
        ;;
    recreate_db)
        recreate_db
        ;;
    *)
        echo "Usage: $0 {function name}"
        exit 1
        ;;
esac
