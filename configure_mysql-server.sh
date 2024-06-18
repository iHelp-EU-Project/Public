#!/bin/bash

###############################
# MySQL Server dependencies #
###############################



# set -e # Exit script immediately on first error.
# #set -x # Print commands and their arguments as they are executed.

# # Check if MySQL environment is already installed
# RUN_ONCE_FLAG=~/.mysql_env_build_time
# MYSQL_PASSWORD="1234"

# if [ -e "$RUN_ONCE_FLAG" ]; then
#   echo "MySQL Server environment is already installed."
#   exit 0
# fi

#curl -OL https://dev.mysql.com/get/mysql-apt-config_0.8.10-1_all.deb
#dpkg -i mysql-apt-config*

# Update Ubuntu package index
apt-get update -y

# # Installs MySQL
# echo "mysql-server mysql-server/root_password password $MYSQL_PASSWORD" |  debconf-set-selections
# echo "mysql-server mysql-server/root_password_again password $MYSQL_PASSWORD" |  debconf-set-selections
# apt-get install -y mysql-server software-properties-common curl wget nano sudo openjdk-11-jdk gnupg2 build-essential git mercurial

# # Configures MySQL
# cp /usr/src/app/mysqld.cnf /etc/mysql/mysql.conf.d/
# sed -i 's/127.0.0.1/0.0.0.0/g'  /etc/mysql/mysql.conf.d/mysqld.cnf #/etc/mysql/my.cnf
# sed -i '/\[mysqld\]/a\lower_case_table_names=1' /etc/mysql/mysql.conf.d/mysqld.cnf #/etc/mysql/my.cnf


# echo "MySQL Password set to '${MYSQL_PASSWORD}'. Remember to delete ~/.mysql.passwd" | tee ~/.mysql.passwd;
# service mysql start
# mysql -uroot -p$MYSQL_PASSWORD -e "GRANT ALL ON *.* TO root@'%' IDENTIFIED BY '$MYSQL_PASSWORD'; FLUSH PRIVILEGES;";
# mysql -uroot -p$MYSQL_PASSWORD -e "CREATE DATABASE IF NOT EXISTS db;";
# service mysql restart


#Install keycloak 17.0.0
# cd /opt
# cp -r /usr/src/app/configurekeycloak/keycloak/* keycloak/
# groupadd keycloak
# useradd -r -g keycloak -d /opt/keycloak -s /sbin/nologin keycloak
# chown -R keycloak: keycloak
# cd /opt/keycloak
# cp /usr/src/app/configurekeycloak/keycloak.service /etc/systemd/system/
# bin/kc.sh build
# export KEYCLOAK_ADMIN=admin
# export KEYCLOAK_ADMIN_PASSWORD=admin
# export KC_DB_URL_HOST=192.168.1.122
cp -r /usr/src/app/dist/AnalyticsICE/* /usr/share/nginx/html

# JavaScript file to modify
file="/usr/share/nginx/html/assets/env.js"

#Value of the environment variable
new_value=$(echo "$KEYCLOAK_SERVER"| sed 's/\//\\\//g')
number_row=4
# Search and replace pattern
pattern="    window['env']['KEYCLOAK_SERVER'] = '$new_value'"

# Replace the variable in the file
sed -i "${number_row}s/.*/$pattern/" $file

# Cleaning unneeded packages
 apt-get autoremove -y
 apt-get clean

# sets "run once" flag
#date > $RUN_ONCE_FLAG

#echo "alias python=python3.7" >> ~/.bashrc 
#. ~/.bashrc
#npm start &
#npm run server --host 0.0.0.0 &
cd /usr/src/app/scriptsML
# /opt/keycloak/bin/kc.sh start-dev &
node ../app.js & 
python3.9 initServer.py & 
