#!/bin/bash

# # Exit on errors from here.
set -e

docker build -t praekelt/mama-ng-scheduler $INSTALLDIR/$REPO
