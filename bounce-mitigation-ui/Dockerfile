FROM openjdk:slim
COPY --from=python:3.8-slim-bullseye / /

# Set the working directory to /app
WORKDIR /app

# Copy the current directory contents into the container at /app
COPY driver ./driver
COPY images ./images
COPY .streamlit ./.streamlit
COPY pages ./pages
COPY config_ini.py .
COPY requirements.txt .
COPY Start_Page.py .

# Expose port 8050 for the Dash application to run on
EXPOSE 8050

# Install Nginx
RUN pip install --trusted-host pypi.python.org -r requirements.txt
#RUN apt-get update && apt-get install -y nginx && apt-get install -y apache2-utils

# Configure Nginx to proxy to the Dash app
#RUN rm /etc/nginx/sites-enabled/default
#COPY nginx.conf /etc/nginx/sites-enabled/

# Add Nginx password protection
#RUN htpasswd -bc /etc/nginx/.htpasswd clinician iHelp20##22


# Start Nginx and the Dash application
CMD streamlit run Start_Page.py --server.fileWatcherType none

