FROM ubuntu:20.04

#image for the data importer function

MAINTAINER  Pavlos Kranas <pavlos@leanxale.com>


RUN apt-get update && apt-get -y install software-properties-common \
 && apt-get update \
 && apt-get -y install screen iputils-ping netcat vim vsftpd net-tools \
 openssh-server virtualenv telnet apt-transport-https openjdk-11-jdk curl  \
 && groupadd -r appuser 

COPY target/data-importer-0.1.1-SNAPSHOT-shaded.jar  /tmp/

RUN mkdir /datasets

EXPOSE 22 23 54735

WORKDIR /tmp
 
CMD cd /tmp \
  && sleep 2 \
  && java -jar data-importer-0.1.1-SNAPSHOT-shaded.jar
