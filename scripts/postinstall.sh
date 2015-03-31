#!/bin/bash

docker stop mama-ng-scheduler
docker rm mama-ng-scheduler

docker stop postgresql
docker rm postgresql

# Exit on errors from here.
set -e

docker run -d --name="postgresql" \
    -v $INSTALLDIR/$REPO/postgres-data:/var/lib/postgresql/data:rw \
    -e POSTGRES_USER="scheduler" \
    -e POSTGRES_PASSWORD="scheduler" \
    postgres

docker build -t praekelt/mama-ng-scheduler $INSTALLDIR/$REPO
docker run -d --name=mama-ng-scheduler \
    --link=postgresql:postgresql \
    -p 8090:8090 \
    -e DATABASE_URL="postgresql://scheduler:scheduler@postgresql:5432/scheduler" \
    praekelt/mama-ng-scheduler
