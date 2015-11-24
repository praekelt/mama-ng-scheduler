FROM mozart/grails:2.4.4
MAINTAINER Praekelt Foundation <dev@praekeltfoundation.org>

COPY . /app
WORKDIR /app

EXPOSE 8000

RUN grails prod war
CMD ["prod", "run-war"]
