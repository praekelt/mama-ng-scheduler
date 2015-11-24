MAMA Nigeria Scheduler
======================

.. image:: https://travis-ci.org/praekelt/mama-ng-scheduler.svg?branch=develop
    :target: https://travis-ci.org/praekelt/mama-ng-scheduler
    :alt: Continuous Integration

.. image:: https://coveralls.io/repos/praekelt/mama-ng-scheduler/badge.png?branch=develop
    :target: https://coveralls.io/r/praekelt/mama-ng-scheduler?branch=develop
    :alt: Code Coverage

A re-usable scheduler for MAMA Nigeria. It is a Grails application that
exposes a REST API for applications needing scheduling to interface with.

Build the local Docker image
----------------------------

::

    git clone https://github.com/praekelt/mama-ng-scheduler ./mama-ng-scheduler
    docker build -t praekelt/mama-ng-scheduler ./mama-ng-scheduler


.. note:: This application builds on an existing ``mozart/grails:2.4.4``
          grails image which is 1.2 GB in size. Expect some time for the first
          download and build.

Run the application with Docker
-------------------------------

We need to be running postgres in a docker container first::

    docker run --rm --name=postgresql \
        -v `pwd`/postgres-data:/var/lib/postgresql/data:rw \
        -e POSTGRES_USER="scheduler" \
        -e POSTGRES_PASSWORD="scheduler" \
        postgres

Then we need to run the Docker image we created in a container and link
it to the postgres container we just started::

    docker run --rm --name=mama-ng-scheduler \
        --link=postgresql:postgresql \
        -p 8000:8000 \
        -e DATABASE_URL="postgresql://scheduler:scheduler@postgresql:5432/scheduler" \
        praekelt/mama-ng-scheduler

You can now access it on your docker container's IP address on port ``8000``.
