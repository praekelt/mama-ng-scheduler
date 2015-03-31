#!/bin/bash

docker stop mama-ng-scheduler
docker rm mama-ng-scheduler

docker stop postgresql
docker rm postgresql

# Exit on errors from here.
set -e

docker run -d --name="postgresql" \
    -v $INSTALLDIR/data:/data:rw \
    -e USER="postgres" \
    -e DB="scheduler" \
    -e PASS="postgres" \
    paintedfox/postgresql

docker build -t praekelt/mama-ng-scheduler $INSTALLDIR/$REPO
docker run -d --name=mama-ng-scheduler \
    --link=postgresql:postgresql \
    -e DATABASE_URL="postgresql://postgresql:5432/scheduler" \
    praekelt/mama-ng-scheduler
