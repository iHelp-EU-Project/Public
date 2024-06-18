# from flask import Flask
# from flask_cors import CORS, cross_origin

# app = Flask(__name__)
# CORS(app)

# @app.route("/")
# def helloWorld():
#   return "Hello, cross-origin-world!"

from flask import Flask, request, g
from flask_cors import CORS, cross_origin
from flask_restful import Resource, Api
from flask.views import MethodView
from json import dumps
from flask_jsonpify import jsonify
import os
import subprocess
import json
import threading
import time
sem = threading.Semaphore()
import datetime
from sys import platform
from flask import Flask, g, jsonify
#from flask_oidc import OpenIDConnect
# Get environment variables
HOST = os.getenv('PYTHON_HOST', '0.0.0.0')
PORT = os.environ.get('PYTHON_PORT', '5002')
# from keycloak.extensions.flask import AuthenticationMiddleware
# from keycloak.keycloak_openid import KeycloakOpenID
# from keycloak.client import KeycloakClient
# import requests

app = Flask(__name__)
api = Api(app)

csvPath = os.getcwd()

# app.config.update({
#         'SECRET_KEY': 'YFQnPw6hBsD5Z8StJqyDJb8P4hJdV57n',
#         'TESTING': False,
#         'DEBUG': False,
#         'OIDC_CLIENT_SECRETS': 'client_secrets.json',
#         'OIDC_OPENID_REALM': 'analyticsproduct',
#         'OIDC_INTROSPECTION_AUTH_METHOD': 'client_secret_post',
#         'OIDC_TOKEN_TYPE_HINT': 'access_token',
#         'OIDC-SCOPES': ['email', 'profile']
#         })


# oidc = OpenIDConnect(app)

token_bool = False;

CORS(app) 



#Cluster KMEANS  start



# class ScriptsKmeansModel(Resource):
@app.route('/scripts/kmeansModel/<name>/<train>/<columnsIdPrediction>/<split>/<seed>/<init>/<maxiteration>/<clusterk>/<standardize>/<userName>/<deleteColumns>/<classColumn>/<valuesClassColumn>/<valuesNan>/<advancedModel>')
# @oidc.accept_token(require_token=token_bool, scopes_required=['email', 'profile'])    
def ScriptsKmeansModel(name,train,columnsIdPrediction,split,seed,init,maxiteration,clusterk,standardize,userName,deleteColumns,classColumn,valuesClassColumn,valuesNan,advancedModel):
    if Autorization(userName):
        Logout(userName)
        today =  datetime.date.today()
        dateToday = str(today.year) + str('{:02d}'.format(today.month)) + str('{:02d}'.format(today.day))
        sem.acquire()
        if platform == "linux" or platform == "linux2":
            os.system("python3.9  kMeans/KmeansModel.py " + name +"."+ dateToday  + " " + train +  " " +  columnsIdPrediction + " "  + split + " "  + seed + " "  + init + " "  + maxiteration + " "  + clusterk + " "  + standardize + " " + userName + " " + deleteColumns + " " + classColumn + " " + valuesClassColumn + " " + valuesNan + " " + advancedModel + "> output.txt")    
        else:
            os.system("python kMeans/KmeansModel.py " + name +"."+ dateToday  + " " + train + " "  +  columnsIdPrediction + " "  + split + " "  + seed + " "  + init + " "  + maxiteration + " "  + clusterk + " "  + standardize + " " + userName + " " + deleteColumns + " " + classColumn + " " + valuesClassColumn + " " + valuesNan + " " + advancedModel + "> output.txt")    
        sem.release() 
        # with open('output.txt', 'r') as handle:
        #     parsed = handle.read()
        return ('Algorithm runned OK')
    else:
        return('Sorry, not authorised')





# Cluster KMEANS  END


# Sklearn Start

# class ScriptsSklearn(Resource):
@app.route('/scripts/ScriptsSklearn/<modelName>/<userName>/<fileNameData>/<init>/<max_iteration>/<cluster_k>/<random_state>/<deleteColumns>/<class_column>/<columnsIdPrediction>/<nInit>/<itemsClassColumn>/<valuesNan>/<advancedModel>')
# @oidc.accept_token(require_token=token_bool, scopes_required=['email', 'profile'])
def ScriptsSklearn(modelName,userName,fileNameData,init,max_iteration,cluster_k,random_state,deleteColumns,class_column,columnsIdPrediction,nInit,itemsClassColumn,valuesNan,advancedModel):
    if Autorization(userName):
        Logout(userName)
        today =  datetime.date.today()
        dateToday = str(today.year) + str('{:02d}'.format(today.month)) + str('{:02d}'.format(today.day))
        sem.acquire()
        if platform == "linux" or platform == "linux2":
            os.system("python3.9  clusteringSklearn/sklearn-script.py " + modelName +"."+ dateToday  + " " + userName + " " + fileNameData + " " + init + " " + max_iteration + " " + cluster_k + " " + random_state + " " + deleteColumns + " " + class_column + " " + columnsIdPrediction + " " + nInit + " " + itemsClassColumn + " " + valuesNan + " " + advancedModel + "> output.txt")    
        else:
            os.system("python clusteringSklearn/sklearn-script.py " + modelName +"."+ dateToday + " " + userName + " " +  fileNameData + " " + init + " " + max_iteration + " " + cluster_k + " " + random_state + " " + deleteColumns + " " + class_column + " " + columnsIdPrediction + " " + nInit + " " + itemsClassColumn + " " + valuesNan + " " + advancedModel + "> output.txt")    
        sem.release() 
        # with open('output.txt', 'r') as handle:
        #     parsed = handle.read()
        return ('Algorithm runned OK')
    else:
        return('Sorry, not authorised')





# Sklearn END

# WARD Start

# class ScriptsWard(Resource):
@app.route('/scripts/ScriptsWard/<modelName>/<userName>/<fileNameData>/<clusterK>/<choseDeleteColumnsString>/<classColumn>/<columnsPredict>/<nameClases>/<affinity>/<computeFullTree>/<computeDistances>/<link>/<valuesNan>/<advancedModel>')
# @oidc.accept_token(require_token=token_bool, scopes_required=['email', 'profile'])
def ScriptsWard(modelName,userName,fileNameData,clusterK,choseDeleteColumnsString,classColumn,columnsPredict,nameClases,affinity,computeFullTree,computeDistances,link,valuesNan,advancedModel):
    if Autorization(userName):
        Logout(userName)
        today =  datetime.date.today()
        dateToday = str(today.year) + str('{:02d}'.format(today.month)) + str('{:02d}'.format(today.day))
        sem.acquire()
        if platform == "linux" or platform == "linux2":
            os.system("python3.9  clusteringWard/ward-script.py " + modelName +"."+ dateToday  + " " + userName + " " + fileNameData + " " + clusterK + " " + choseDeleteColumnsString + " " + classColumn + " " + columnsPredict + " " + nameClases + " " + affinity + " " + computeFullTree + " " + computeDistances + " " + link + " " + valuesNan + " " + advancedModel + "> output.txt")    
        else:
            os.system("python clusteringWard/ward-script.py " + modelName +"."+ dateToday  + " " + userName + " " + fileNameData + " " + clusterK + " " + choseDeleteColumnsString + " " + classColumn + " " + columnsPredict + " " + nameClases + " " + affinity + " " + computeFullTree + " " + computeDistances + " " + link + " " + valuesNan + " " + advancedModel + "> output.txt")    
        sem.release() 
        # with open('output.txt', 'r') as handle:
        #     parsed = handle.read()
        return ('Algorithm runned OK')
    else:
        return('Sorry, not authorised')



# WARD END

# Birch Start

@app.route('/scripts/ScriptsBirch/<modelName>/<userName>/<fileNameData>/<clusterK>/<choseDeleteColumnsString>/<classColumn>/<columnsPredict>/<nameClases>/<threshold>/<branching_factor>/<compute_labels>/<valuesNan>/<advancedModel>', methods=['GET'])
# @oidc.accept_token(require_token=token_bool, scopes_required=['email', 'profile'])
def ScriptsBirch(modelName,userName,fileNameData,clusterK,choseDeleteColumnsString,classColumn,columnsPredict,nameClases,threshold,branching_factor,compute_labels,valuesNan,advancedModel):
    if Autorization(userName):
        Logout(userName)
        today =  datetime.date.today()
        dateToday = str(today.year) + str('{:02d}'.format(today.month)) + str('{:02d}'.format(today.day))
        sem.acquire()
        if platform == "linux" or platform == "linux2":
            os.system("python3.9  clusteringBirch/birch-script.py " + modelName +"."+ dateToday  + " " + userName + " " + fileNameData + " " + clusterK + " " + choseDeleteColumnsString + " " + classColumn + " " + columnsPredict + " " + nameClases + " " + threshold + " " + branching_factor + " " + compute_labels + " " + valuesNan + " " + advancedModel + "> output.txt")    
        else:
            os.system("python clusteringBirch/birch-script.py " + modelName +"."+ dateToday + " " + userName + " " +  fileNameData + " " + clusterK + " " + choseDeleteColumnsString + " " + classColumn + " " + columnsPredict + " " + nameClases + " " + threshold + " " + branching_factor + " " + compute_labels + " " + valuesNan + " " + advancedModel + "> output.txt")    
        sem.release() 
        # with open('output.txt', 'r') as handle:
        #     parsed = handle.read()
        return ('Algorithm runned OK')
    else:
        return('Sorry, not authorised')



# Birch END



#Auorization python back-end
def Autorization(username):
    with open(csvPath + '/autorization.json', 'r') as f:
        data = json.load(f)
    # print(data)
    if data[username] in 'true':
        return True
    else:   
        return False
# Logout in Backend python for security
def Logout(username):
    with open(csvPath + '/autorization.json', 'r') as f:
        data = json.load(f)
    data[username] = 'false'

    with open(csvPath + '/autorization.json', 'w') as file:
        json.dump(data, file)





if __name__ == '__main__':
   app.run(host=HOST,port=5002, threaded=True)





