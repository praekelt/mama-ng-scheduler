FROM mozart/grails:2.4.4
MAINTAINER Praekelt Foundation <dev@praekeltfoundation.org>

COPY . /app
WORKDIR /app

RUN grails refresh-dependencies
CMD ["prod", "run-war"]
