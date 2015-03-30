#!/bin/bash

docker stop mama-ng-scheduler
docker rm mama-ng-scheduler

# Exit on errors from here.
set -e

docker build -t praekelt/mama-ng-scheduler $REPO
docker run -d --name=mama-ng-scheduler praekelt/mama-ng-scheduler
