# Bounce Mitigation UI

![Python](https://img.shields.io/badge/python-3670A0?style=for-the-badge&logo=python&logoColor=ffdd54)

## Local Installation 

- Directly
```bash
pip install -r requirements.txt

streamlit run Start_Page.py
```

- With docker
    - Build the image
    ```bash
    docker build -t <image_tag> .
    ```
    - Run the image
    ```bash
    docker run -p 8050:8050 <image_tag>
    ```
    

## Installation Common command for On-premise
```bash
git clone https://cloudgitlab.dslab.ece.ntua.gr/ihelp/t55-bounce-mitigation/bounce-mitigation-ui.git
```

## iHelp UPRC cloud Installation 
    
In Ihelp platform hotsted on UPRC cloud, the deployment is perfomed by the ArgoCD tool managed by the Kodar partner.

The tool is reffering to the values defined on the /bounce_helm/values-uprc-prod.yaml
 
## Installation on TMU premise   

```bash

#Chart values verification
helm install --dry-run --debug --name-template bounce-mitigation-ui ./bounce_helm/ -f ./bounce_helm/values-tmu-prod.yaml

#Installation

helm install --name-template  bounce-mitigation-ui ./bounce_helm/ -f ./bounce_helm/values-tmu-prod.yaml
```

## Installation on FPG premise   

```bash

#Verify the chart values 

helm install --dry-run --debug --name-template bounce-mitigation-ui ./bounce_helm/ -f ./bounce_helm/values-fpg-prod.yaml

#installation
helm install --name-template bounce-mitigation-ui ./bounce_helm/ -f ./bounce_helm/values-fpg-prod.yaml
```

## Creating a new docker image and pushing to UPRC docker registry

Use the script build_push_to_uprc_docker_reg.sh with tag increased version number.

Check the latest version number on: 
 
Example

If the latest version is 1.0.1, depending on the changes yoy've (minor/major) done then use the following script
```bash
#minor changes increase the latest number
build_push_to_uprc_docker_reg.sh 1.0.2

#medium changes
build_push_to_uprc_docker_reg.sh 1.1.0


#major changes increase the first number
build_push_to_uprc_docker_reg.sh 2.0.0

```

Observation. If you want to deploy  the latestversion, change the docuker image value in the helm_chart/values-*-prod.yaml files with the version that you'd want. 

