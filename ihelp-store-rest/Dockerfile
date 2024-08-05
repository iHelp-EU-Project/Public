FROM ubuntu:18.04

MAINTAINER  Pavlos Kranas <pavlos@leanxale.com>


ENV SERVICE_BACKEND_HOME="/lx"
ENV SERVICE_BACKEND_DIST="/lx_dist" 
ENV TOMCAT_KEYSTORE_PATH="/tmp/keystore.jks"
#ENV swagger_path = "/tmp/swagger"

RUN apt-get update && apt-get -y install software-properties-common \
 && add-apt-repository -y ppa:webupd8team/java \
 && add-apt-repository -y ppa:openjdk-r/ppa \
 && apt-get update \
 && apt-get -y install screen iputils-ping netcat vim vsftpd net-tools \
 openssh-server virtualenv telnet apt-transport-https openjdk-8-jdk curl  \
 && groupadd -r appuser \
 && useradd -d ${SERVICE_BACKEND_HOME} -r -s /bin/false -g appuser appuser \
 && mkdir ${SERVICE_BACKEND_HOME} \ 
 && mkdir ${SERVICE_BACKEND_DIST} \
 && mkdir /tmp/src/ \
 && mkdir /tmp/src/main/ \
 && mkdir /tmp/src/main/webapp/

COPY ihelp-store-rest-0.0.1-SNAPSHOT-shaded.jar keystore.jks  /tmp/
COPY src/main/webapp/ /tmp/src/main/webapp/

RUN chown -R appuser:appuser ${SERVICE_BACKEND_HOME} 


EXPOSE 22 23 54735

WORKDIR /tmp
 
CMD cd /tmp \
  && sleep 2 \
#  && sleep infinity
  && java -jar ihelp-store-rest-0.0.1-SNAPSHOT-shaded.jar
