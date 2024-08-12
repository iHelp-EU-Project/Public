FROM openjdk:slim
COPY --from=python:3.8-slim-bullseye / /

RUN mkdir -p /bounce_mitigation

#main script that runs  based on the scheduled k8s cronjob
COPY question-adh/main_v2.py /bounce_mitigation

#script that will load past data, if the clinical study was started before the BM installation

COPY question-adh/populate.py /bounce_mitigation


COPY ext-lib /bounce_mitigation


RUN pip install JayDeBeApi==1.2.3
WORKDIR /bounce_mitigation

CMD ["python","main_v2.py"]
