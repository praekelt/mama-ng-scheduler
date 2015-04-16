FROM mozart/grails:2.5.0
MAINTAINER Praekelt Foundation <dev@praekeltfoundation.org>

COPY . /app
WORKDIR /app

EXPOSE 8090

RUN grails prod war
CMD ["prod", "run-war"]
