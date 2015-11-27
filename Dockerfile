FROM debian:jessie
MAINTAINER Praekelt Foundation <dev@praekeltfoundation.org>

ENV APT_GET_INSTALL "apt-get install -qyy -o APT::Install-Recommends=false -o APT::Install-Suggests=false"

# Install OpenJDK 8 JDK
RUN echo "deb http://http.debian.net/debian jessie-backports main" > /etc/apt/sources.list.d/backports.list
RUN apt-get update \
    && $APT_GET_INSTALL \
        openjdk-8-jdk \
    && rm -rf /var/lib/apt/lists/*
ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64

# Install Grails
ENV GRAILS_VERSION "2.5.2"
RUN apt-get update \
    && $APT_GET_INSTALL curl unzip \
    && curl -L -o /tmp/grails-$GRAILS_VERSION.zip "https://github.com/grails/grails-core/releases/download/v$GRAILS_VERSION/grails-$GRAILS_VERSION.zip" \
    && unzip /tmp/grails-$GRAILS_VERSION.zip -d /tmp \
    && mv /tmp/grails-$GRAILS_VERSION /opt/grails \
    && rm /tmp/grails-$GRAILS_VERSION.zip \
    && apt-get purge -y --auto-remove curl unzip \
    && rm -rf /var/lib/apt/lists/*
ENV GRAILS_HOME /opt/grails
ENV PATH $PATH:$GRAILS_HOME/bin

COPY . /app
WORKDIR /app

RUN grails prod war

EXPOSE 8000

CMD grails prod run-war
