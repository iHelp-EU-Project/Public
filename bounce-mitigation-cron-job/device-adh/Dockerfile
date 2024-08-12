FROM openjdk:slim
COPY --from=python:3.8-slim-bullseye / /

RUN mkdir -p /bounce_mitigation

#copy the script that loads periodicall
COPY device-adh/main.py /bounce_mitigation
#polulate script is used to load data from the pastif the clinical study started before the instalation of the BM tool
COPY device-adh/populate.py /bounce_mitigation

COPY ext-lib/qe-driver-1.8-RC.9-jar-with-dependencies.jar /bounce_mitigation

RUN pip install JayDeBeApi==1.2.3
WORKDIR /bounce_mitigation

CMD ["python","main.py"]
