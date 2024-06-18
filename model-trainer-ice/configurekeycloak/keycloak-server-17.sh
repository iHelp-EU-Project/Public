#!/bin/bash
cd /opt
wget https://github.com/keycloak/keycloak/releases/download/17.0.0/keycloak-17.0.0.tar.gz
tar -xvf keycloak-17.0.0.tar.gz
mv keycloak-17.0.0 keycloak
groupadd keycloak
useradd -r -g keycloak -d /opt/keycloak -s /sbin/nologin keycloak
chown -R keycloak: keycloak
cd /opt/keycloak
cp /usr/src/app/configurekeycloak/keycloak.service /etc/systemd/system/
cp -r /usr/src/app/configurekeycloak/keycloak/data /opt/keycloak
bin/kc.sh build
export KEYCLOAK_ADMIN=admin
export KEYCLOAK_ADMIN_PASSWORD=admin
sudo -E bin/kc.sh start
systemctl daemon-reload
systemctl enable keycloak