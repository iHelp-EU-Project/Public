FROM ubuntu:20.04

#image for the data capture gateway

MAINTAINER  Pavlos Kranas <pavlos@leanxale.com>

ENV SERVICE_BACKEND_HOME="/lx"
ENV SERVICE_BACKEND_DIST="/lx_dist" 
ENV TOMCAT_KEYSTORE_PATH="/tmp/keystore.jks"
#ENV swagger_path = "/tmp/swagger"

RUN apt-get update && apt-get -y install software-properties-common \
 && apt-get update \
 && apt-get -y install screen iputils-ping netcat vim vsftpd net-tools \
 openssh-server virtualenv telnet apt-transport-https openjdk-11-jdk curl  \
 && groupadd -r appuser \
 && useradd -d ${SERVICE_BACKEND_HOME} -r -s /bin/false -g appuser appuser \
 && mkdir ${SERVICE_BACKEND_HOME} \ 
 && mkdir ${SERVICE_BACKEND_DIST} 

COPY data-capture-rest-0.1.1-SNAPSHOT-shaded.jar keystore.jks data-capture-rest/ /tmp/

RUN chown -R appuser:appuser ${SERVICE_BACKEND_HOME} 

RUN mkdir /datasets

EXPOSE 22 23 54735

WORKDIR /tmp
 
CMD cd /tmp \
  && sleep 2 \
  && java -jar data-capture-rest-0.1.1-SNAPSHOT-shaded.jar
