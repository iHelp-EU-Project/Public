
# FROM ubuntu:18.04
FROM ubuntu:22.04

# maintainer details
MAINTAINER Pepe Gil "jose.gil@informationcatalyst.com"



# Opdating the base image
RUN \
  apt update -q -y && \
  apt dist-upgrade -y && \
  apt clean && \
  rm -rf /var/cache/apt/* && \
  
# Installling the requiered build tools for Python (version 3.6.4)
  DEBIAN_FRONTEND=noninteractive apt-get install -y tzdata apt-utils software-properties-common nginx default-jdk && \
  apt install -y make build-essential libssl-dev zlib1g-dev checkinstall libgdbm-dev libc6-dev libffi-dev && \
  apt install -y libbz2-dev libreadline-dev libsqlite3-dev wget curl llvm && \
  apt install -y libncurses5-dev  libncursesw5-dev xz-utils tk-dev nano python3-pip python3-tk python3-distutils python3-apt openssh-server &&\
  apt install -y software-properties-common jq sed adduser libfontconfig1 unzip git curl &&\
  add-apt-repository ppa:deadsnakes/ppa -y &&\
  apt update -q -y &&\
  apt install -y apt-transport-https python3-tk


RUN   cd /usr/src && \
      wget https://www.python.org/ftp/python/3.9.6/Python-3.9.6.tgz && \
      tar xzf Python-3.9.6.tgz && \
      cd Python-3.9.6 && \
      ./configure --enable-optimizations && \
      make altinstall

# Set the working directory to /app
WORKDIR /usr/src/app



# Requeriments python
COPY ./ /usr/src/app
RUN python3.9 -m pip install --upgrade pip setuptools wheel                                                                                                                                                                                                
RUN python3.9 -m pip install --no-cache-dir -r scriptsML/requirements39.txt
RUN python3.9 -m pip uninstall h2o
RUN python3.9 -m pip install http://h2o-release.s3.amazonaws.com/h2o/rel-zermelo/4/Python/h2o-3.32.0.4-py2.py3-none-any.whl
RUN dpkg --remove --force-remove-reinstreq libnode-dev || true
RUN dpkg --remove --force-remove-reinstreq libnode72:amd64 || true
RUN apt remove -y nodejs
RUN curl -sL https://deb.nodesource.com/setup_14.x | bash - 
RUN apt install -y nodejs

RUN export NODE_OPTIONS="--max-old-space-size=32768"

ENV HOSTNAME AnalyticsICE
ENV PROJECTNAME AnalyticsICE
ENV COMPONENT AnalyticsICE
ENV JAVA_HOME /usr/lib/jvm/java-1.8-openjdk
ENV PATH $PATH:/usr/lib/jvm/java-1.8-openjdk/jre/bin:/usr/lib/jvm/java-1.8-openjdk/bin
ENV PYTHONPATH /usr/src/app/scriptsML
# Make port 80 available to the world outside this container

EXPOSE 3001
EXPOSE 5002


# Set the working directory to /app
WORKDIR /usr/src/app
#RUN envsubst < dist/AnaliticsICE/src/assets/env.template.js > dist/AnaliticsICE/src/assets/env.js;

RUN npm install


WORKDIR /usr/src/app/scriptsML
#Run  App
# CMD sh ../configure_mysql-server.sh
CMD sh ../configure_mysql-server.sh && npm start;

#


