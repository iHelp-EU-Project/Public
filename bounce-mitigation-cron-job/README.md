# bounce-mitigation-cron-job



## Building/pushing the docker images

From the bounce-mitigation-cron-job execute
```

docker build -t quest-adh:<tag_version> -f question-adh/Dockerfile .


docker build -t device-adh:<tag_version> -f device-adh/Dockerfile .

```




## Installation on local

The installation on the pilot side is perfomed manually.

Each pilot might have diffent configurations, therefore different value files, named values-<pilot_short>-prod.yaml was used.

For the TMU pilot the installation can be done by using this commands, from the folder bounce-mitigation-cron-job: 

```
#questionnare installation
helm install bm-quest-report-cronjob ./question-adh/charts/ --values ./question-adh/charts/values.yaml

#device install
helm install bm-device-report-cronjob ./device-adh/charts/ --values ./device-adh/charts/values.yaml

```



