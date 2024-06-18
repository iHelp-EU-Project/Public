# FROM quay.io/keycloak/keycloak:17.0.0

# # Make the realm configuration available for import
# COPY realm-export-analytics.json /opt/keycloak_import/

# # Import the realm and user
# RUN /opt/keycloak/bin/kc.sh import --file /opt/keycloak_import/realm-export-analytics.json

# # The Keycloak server is configured to listen on port 8080
# EXPOSE 8080
# EXPOSE 8443

# # Import the realm on start-up
# CMD ["start-dev"]


FROM quay.io/keycloak/keycloak:17.0.0
# Must add json files before import, volume is not mounted yet
ADD realm-export-analytics.json /opt/keycloak/data/import/
# Import all files
RUN /opt/keycloak/bin/kc.sh import --dir=/opt/keycloak/data/import/ --override true; exit 0
# Start image
ENTRYPOINT ["/opt/keycloak/bin/kc.sh", "start-dev"]