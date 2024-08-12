# bounce-mitigation-cron-job



## Building/pushing the docker images

From the bounce-mitigation-cron-job execute
```
docker login cloudgitlab.dslab.ece.ntua.gr:5050

docker build -t cloudgitlab.dslab.ece.ntua.gr:5050/ihelp/t55-bounce-mitigation/bounce-mitigation-cron-job/quest-adh:0.3.0 -f question-adh/Dockerfile .
docker push cloudgitlab.dslab.ece.ntua.gr:5050/ihelp/t55-bounce-mitigation/bounce-mitigation-cron-job/quest-adh:0.3.0


docker build -t cloudgitlab.dslab.ece.ntua.gr:5050/ihelp/t55-bounce-mitigation/bounce-mitigation-cron-job/device-adh:0.3.0 -f device-adh/Dockerfile .
docker push cloudgitlab.dslab.ece.ntua.gr:5050/ihelp/t55-bounce-mitigation/bounce-mitigation-cron-job/device-adh:0.3.0

```

## Installation on the ihelp platform (UPRC cloud)

The installation of these cronjobs is performed by the ArgoCD tool managed by the Kodar team.

The ArgoCD points out to the the paths ./question-adh/charts/values-uprc-prod.yaml and ./device-adh/charts/values-uprc-prod.yaml
 

## Manual Installation on k8s cluster using Helm charts UPRC
If you have more than one k8s context, please ensure that you are in the one that is corresponding to the iHelp platform.
This can be done by using, thhe folliwng command  for example, for the iHelp context:
```
kubectl config use-context iHelp
```

Installation of questionnaire cronjob, from folder bounce-mitigation-cron-job/question-adh 
```
helm install bm-quest-report-cronjob charts/ --values charts/values-uprc-prod.yaml
#If the helm chart is already installed then you can perfom an update 
helm upgrade bm-quest-report-cronjob charts/ --values charts/values-uprc-prod.yaml --install
#check the installation status
kubectl get cronjobs
```

Installation of device cronjob, from folder bounce-mitigation-cron-job/device-adh
```
helm install bm-device-report-cronjob charts/ --values charts/values-uprc-prod.yaml
#If the helm chart is already installed then you can perfom an update 
helm upgrade bm-device-report-cronjob charts/ --values charts/values-uprc-prod.yaml --install
#check the installation status
kubectl get cronjobs
```

Observation. On the UPRC side, the deployment is performed via ArgoCD (see above).


## Installation on the pilot side

The installation on the pilot side is perfomed manually.

Each pilot might have diffent configurations, therefore different value files, named values-<pilot_short>-prod.yaml was used.

For the TMU pilot the installation can be done by using this commands, from the folder bounce-mitigation-cron-job: 

```
#questionnare installation
helm install bm-quest-report-cronjob ./question-adh/charts/ --values ./question-adh/charts/values-tmu-prod.yaml

#device install
helm install bm-device-report-cronjob ./device-adh/charts/ --values ./device-adh/charts/values-tmu-prod.yaml

```

while for the FPG

```
helm install bm-quest-report-cronjob ./question-adh/charts/ --values ./question-adh/charts/values-tmu-prod.yaml

#device install
helm install bm-device-report-cronjob ./device-adh/charts/ --values ./device-adh/charts/values-tmu-prod.yaml

```

## How to load data from the past 
If the clinical study started before the installation of the cronjobs, there will be some missing data for patients

Therefore, in order to mitigate this case, for a partner we can run the following scripts that will populate the missing data until today month.

```
#load questionnare data report

kubectl run loadquest  -i --tty --image cloudgitlab.dslab.ece.ntua.gr:5050/ihelp/t55-bounce-mitigation/bounce-mitigation-cron-job/quest-adh:0.3.0 --env="PILOT=TMU" -- python /bounce_mitigation/populate.py

#load device data report

kubectl run loaddevice  -i --tty --image cloudgitlab.dslab.ece.ntua.gr:5050/ihelp/t55-bounce-mitigation/bounce-mitigation-cron-job/device-adh:0.3.0 --env="PILOT=TMU" -- python /bounce_mitigation/populate.py
``` 


