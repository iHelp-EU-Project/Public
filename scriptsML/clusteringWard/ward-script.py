# -*- coding: utf-8 -*-
"""
Created on Wed Jan 26 09:49:10 2022

@author: spokk

"""

import glob
import zipfile
import numpy as np
import matplotlib.pyplot as plt
import pandas as pd
import sys
import os
import time
import copy
import json
import shutil
from timeit import default_timer as timer
from datetime import timedelta
from datetime import datetime
from itertools import cycle, islice
import seaborn as sns
from sys import platform
import sys

import sklearn
sys.setrecursionlimit(10000)


import h2o
from h2o.estimators.kmeans import H2OKMeansEstimator
sys.path.append('../shapPlots')
import shapPlots.shapPlots as shapPlots

import warnings


from sklearn.preprocessing import StandardScaler,MinMaxScaler, LabelEncoder
from sklearn.cluster import KMeans
from sklearn import metrics
from scipy.spatial.distance import cdist,pdist
from sklearn.pipeline import Pipeline
from sklearn.decomposition import PCA
from sklearn.metrics import silhouette_score, adjusted_rand_score
from sklearn.metrics import mean_squared_error


import re


import pickle


from sklearn import datasets

def main(params):

    h2o.init() 

    modelName = params['modelName']
    print('modelName:' + modelName)
    userName = params['userName']
    print('userName:' + userName)
    fileNameData = params['fileNameData']
    print('fileNameData:' , fileNameData)
    clusterK = int(params['clusterK'])
    print('clusterK:' , clusterK)          
    choseDeleteColumnsString = params['choseDeleteColumnsString']
    print('choseDeleteColumnsString:' , choseDeleteColumnsString)
    classColumn = params['classColumn']
    print('classColumn:' , classColumn)           
    columnsPredict = params['columnsPredict']
    print('columnsPredict:' , columnsPredict)                       
    nameClases = params['nameClases']
    print('NameClases:' , nameClases)

    affinity = params['affinity']
    print('affinity:' , affinity)
    compute_full_tree = params['compute_full_tree']
    print('compute_full_tree:' , compute_full_tree)           
    compute_distances = params['compute_distances']
    print('compute_distances:' , compute_distances)
    link = params['link']
    print('link:' , link)
    valuesNan = params['valuesNan']
    print('valuesNan:' , valuesNan)
    advancedModel = params['advancedModel']
    print('advancedModel:' , advancedModel)  


    mse = ''
    rmse = ''
    nobs = ''



    datafile =  fileNameData
    labels_file =  nameClases

    pathWorkSpace =  os.getcwd() + "/imagesup/" + params['userName'] +'/' + params['modelName'] +'/'

    true_label_names = labels_file.split(',')

    label_encoder = LabelEncoder()

    true_labels = label_encoder.fit_transform(true_label_names)

    nameBigClassNames =  true_label_names #['Breast invasive carcinoma', 'Colon adenocarcinoma', 'Kidney renal clear cell carcinoma', 'Lung adenocarcinoma', 'Prostate adenocarcinoma']

    n_clusters = len(label_encoder.classes_)

    # Getting the current date and time
    dt = datetime.now()

    # getting the timestamp
    ts = datetime.timestamp(dt)

    #print('Classes: ' , n_clusters)

    # JSON of pregress bar in frond-end
    loadingJSON(userName, 20, 'Awaiting data, please be patient', False)
    time.sleep(2.0)

    df = pd.read_csv(datafile,  na_values = ['no info', '.'])
    if str(df.loc[0]).split(' ')[0] == 'Unnamed:':
        df = df.iloc[: , 1:]# delete first column in fact is void, because is a bug the library datatables when is generated the new table with propierties chosen per user in angular frond-end 
    print(df.columns, '*******************************************')
    classesStringToNumberDictionary = {
        "name":"",
        "possibleValues":[],
        "realValues":[],
    }
    plotAvailable = {
        'typeOfPlot': '',
        'xAxis': '',
        'yAxis': '',
        'value': ''
    }
    classesStringTONumberDictionariTmp = []
    nameColumnsObject = { 
                        'columns': '' ,
                        'list' : [] , 
                        'predictorClasses': [], 
                        'all' : [], 
                        'deleteColumns' : [], 
                        'classesStringToNumberDictionary' : [], 
                        'algorithmUsed': '',
                        'libraryUsed': '',  
                        'targetClass': '', 
                        'itemsClassColumn': '' , 
                        'targetOrganisation': ['ICE'], 
                        'timestamp': ts, 
                        'description' : '' , 
                        'libraryVersion': '' , 
                        'modelContainerFile': '', 
                        'plotAvailable': [],
                        'titleOfPlot': '',
                        'version': '1.0',
                        'advancedModel': '',
                        'modelaccuracy': '',
                        'snomedDict': {}

    } #Object save information about columns} #Object save information about columns
    columnsItems = []
    df_text = df # save copy for regression data
    columsAll = []
    deleteColumnsArray = choseDeleteColumnsString.split(',')
    # set the predictor columns names
    predictors = columnsPredict.split(',')
    # predictors =  lis 

    # Save the dictionary SNOMED in JSON for DSS and model manager
    array = predictors
    result = generate_object(array)
    print(result)
    nameColumnsObject['snomedDict'] = result



    # df.to_csv(pathWorkSpace + modelName + 'original.csv',
    #                 sep=',', encoding='utf-8', index=False)
    #Save all the original  information the items and columns in JSON file for deploy model
    # Save all the original  information the items and columns in JSON file for deploy model
    for x in df.columns:
        columsAll.append(x)
        if df[x].dtypes == 'object':  # just if items of columns is object
            # Change NaN per 'none' string
            df[x] = df[x].replace(np.nan, 'none', regex=True)
            # clean null and NaN the columns with number
            if valuesNan in 'drop':
                df.dropna(subset=[x], inplace=True)

            elif valuesNan in 'with0':
                df[x] = df[x].fillna(0)

            elif valuesNan in 'with1':
                df[x] = df[x].fillna(1)

            else:
                df[x] = df[x].fillna(method='ffill')

            # if x in predictors:
            classesStringToNumberDictionary['name'] = x  # save name column
            classesStringToNumberDictionary['possibleValues'] = df[x].unique(
            ).tolist()  # save unique values of column
            temp = []
            for idx, target in enumerate(classesStringToNumberDictionary['possibleValues']):
                temp.append(idx)
            classesStringToNumberDictionary['realValues'] = temp
            classesStringTONumberDictionariTmp.append(
                copy.copy(classesStringToNumberDictionary))
        else:
            # clean null and NaN the columns with number
            if valuesNan in 'drop':
                df.dropna(subset=[x], inplace=True) # delete rows with vlues null or Nan

            elif valuesNan in 'with0':
                df[x] = df[x].fillna(0) # with 0

            elif valuesNan in 'with1':
                df[x] = df[x].fillna(1) # with 1

            elif valuesNan in 'fill':
                df[x] = df[x].fillna(method='ffill') # Propagate values forward or backward

            elif valuesNan in '0.0':
                df[x] = df[x].fillna(df[x].quantile(0.0)) # min percentile

            elif valuesNan in '0.25':
                df[x] = df[x].fillna(df[x].quantile(0.25)) # 25th percentile

            elif valuesNan in '0.75':
                df[x] = df[x].fillna(df[x].quantile(0.75)) # 75th percentile

            elif valuesNan in '0.100':
                df[x] = df[x].fillna(df[x].quantile(0.100)) # max percentile

            elif valuesNan in 'median':
                df[x] = df[x].fillna(df[x].median())# median

            elif valuesNan in 'mean':
                df[x] = df[x].fillna(df[x].mean()) # mean

            elif valuesNan in 'mode':
                df[x] = df[x].fillna(df[x].mode()[0]) # mode

         
    # df.to_csv(pathWorkSpace + modelName + 'clean.csv',
    #                 sep=',', encoding='utf-8', index=False)


    nameColumnsObject['predictorClasses'] = predictors
    nameColumnsObject['all'] = columsAll
    nameColumnsObject['deleteColumns'] = deleteColumnsArray
    nameColumnsObject['classesStringToNumberDictionary'] = classesStringTONumberDictionariTmp
    nameColumnsObject['algorithmUsed'] = 'Ward'
    nameColumnsObject['libraryUsed'] = 'scikit-learn'
    nameColumnsObject['targetClass'] = classColumn
    nameColumnsObject['description'] = 'K-Means falls in the general category of clustering algorithms. Clustering is a form of unsupervised learning that tries to find structures in the data without using any labels or target values. Clustering partitions a set of observations into separate groupings such that an observation in a given group is more similar to another observation in the same group than to another observation in a different group.'
    nameColumnsObject['libraryVersion'] = sklearn.__version__
    nameColumnsObject['titleOfPlot'] = 'Prediction'
    nameColumnsObject['modelContainerFile'] = modelName + "_model.pkl"
    nameColumnsObject['version'] = '1.0'
    nameColumnsObject['advancedModel'] = advancedModel

    
    loadingJSON(userName, 30, 'Clean data', False)
    time.sleep(2.0);


    # for idx,target_list in enumerate(columnsItems):
    #     if idx != 0:
    #         columnsItems.pop(idx)

        
    cancer = []

    
        
    print(df.T)

    
    #Change strings columns we can assign numbers like '0' and '1' respectively so that our algorithms can work on the data.
    for x in df.columns:
        if df[x].dtypes == 'object':# just if items of columns is object
            for idx ,val in enumerate(df[x].unique().tolist()):
                df[x][df[x] == val] = idx  # change the strings per numbers to each column
                if x == classColumn:
                    cancer.append(val)
                
    print(df.T)
    
    
    # Importing the datasets and assign them to internal attributes
    trainData = h2o.H2OFrame(df)   #h2o.import_file(trainDataName)

    if platform == "linux" or platform == "linux2":
        trainData.rename(columns={trainData.columns[0]:trainData.columns[0].replace('ï»¿','')})
    else:    
        trainData.rename(columns={trainData.columns[0]:trainData.columns[0].replace('Ã¯Â»Â¿','')})

    print(trainData.columns[0])

    trainData = trainData.drop([0], axis=0)

    print(trainData)

    #Datatest for testing model builded
    datatest = trainData.as_data_frame(use_pandas=True,header=True);
    datos = datatest.sample(n=min(200, len(datatest))) #Cambiar el tamaño de la muestra:
    # datos = datatest.sample(n=200, replace=True) # Permitir el reemplazo en la muestra:
    # datos = datatest.sample(n=200)    
    rango_array = datos[classColumn][0:].values
    rango_array = rango_array.tolist()

    loadingJSON(userName, 40, 'Generate Shap Plots & Data', False)
    time.sleep(2.0);

    shapPlots.ShapClassPlots(modelName,userName, fileNameData,choseDeleteColumnsString,columnsPredict,classColumn, trainData, nameClases)


    print(trainData)

    #delete columns chosen
    if deleteColumnsArray[0] != '_':
        for column in deleteColumnsArray:
            print('deleted columnof dataset: ',column)
            trainData = trainData.drop(column)

    

    print(trainData.columns)


    raw_data = h2o.as_list(trainData)

    data = trainData.as_data_frame(use_pandas=True,header=True)


    raw_data.to_csv(pathWorkSpace + modelName + '_dataFinal.csv', sep=',', encoding='utf-8',index=False)

    Clustering_data = raw_data

   

    # Clustering_data = data["data"]
    # print(data["data"])
    #Clustering_data

    X = MinMaxScaler().fit_transform(Clustering_data)
    #print(X)
    y= true_label_names
    #print(y)

    distortions = []
    Ks = [2, 3, 4, 5, 10]
    for k in Ks:
        kmeans = KMeans(n_clusters = k)
        model = kmeans.fit(X)
        centers = model.cluster_centers_
        distortions.append(sum(np.min(cdist(X,centers, 'euclidean'), axis=1)) / X.shape[0])

    plt.plot(Ks, distortions)
    plt.xlabel('k')
    plt.ylabel('Distortion')
    plt.title('The Elbow Method ')
    # plt.show()
    plt.savefig(pathWorkSpace + modelName + "_Cluster_K.png")

    # dist = pd.DataFrame({'Cluster' : Ks,'Distortions' : distortions})
    # dist.to_csv(pathWorkSpace + params['modelName'] + "_Distortion_X_Y.csv", index=False, sep=',')



    ks = 2,3,4,5,10
    SSE = []

    for k in ks:
        
        model = KMeans(n_clusters=k)   
        model.fit(Clustering_data)



        # Append the inertia to the list of inertias
        SSE.append(model.inertia_)
        
    # Plot ks vs inertias
    print(SSE)
    plt.plot(ks, SSE, '-o')
    plt.xlabel('number of clusters')
    plt.ylabel('SSE')
    plt.xticks(ks)
    # plt.show()
    plt.savefig(pathWorkSpace + modelName + "_Number_Cluster.png")

    sse = pd.DataFrame({'Cluster' : ks,'SSE' : SSE,'Distortions' : distortions})
    sse.to_csv(pathWorkSpace + params['modelName'] + "_SEE_Distortions.csv", index=False, sep=',')
    
    nameBigClassNames = nameClases.split(',')#['Breast invasive carcinoma', 'Colon adenocarcinoma', 'Kidney renal clear cell carcinoma', 'Lung adenocarcinoma', 'Prostate adenocarcinoma']

    if link == 'single' or link == 'all':
        '''
        Single
        '''
        type(Clustering_data)


        from scipy.cluster.hierarchy import dendrogram, linkage, cophenet
        import pickle


        linked1 = linkage(X, 'single')

        labelList = range(len(X))

        plt.figure(figsize=(64, 32))  
        dendrogram(linked1,
                labels = labelList,
                distance_sort = 'descending',
                show_leaf_counts = True)
        #plt.show()  
        plt.savefig(pathWorkSpace + modelName + "_Single_model_dendrogram.png")



        c1=cophenet(linked1,pdist(X))
        c1

        # from sklearn.cluster import AgglomerativeClustering
        # clusters=[]
        # cluster = AgglomerativeClustering(n_clusters=clusterK, affinity='euclidean', linkage='single')  
        # clusters.append(cluster.fit_predict(X)) 
        # plt.figure(figsize=(10, 7))  
        # plt.scatter(X[:,4], X[:,9], c=cluster.labels_, cmap='rainbow') 
        # #plt.show()
        # plt.savefig(pathWorkSpace + modelName + "_Single_model_predict.png")

        # save the model
        # pickle.dump(cluster, open(pathWorkSpace + modelName + "_Single_model.pkl", "wb"))
        # prediction = cluster.fit_predict(raw_data[1:10])

        # print (prediction)


        # for x in prediction:
        #     print('Prediction Single: ', label_encoder.classes_[x] + ' -> ' + nameBigClassNames[x])

        loadingJSON(userName, 50, 'Building model Single', False)
        time.sleep(2.0);


        single, hits = generateSkatterPlot(clusterK, data, classColumn, 'single', label_encoder, cancer, pathWorkSpace, params,affinity,compute_full_tree,compute_distances)
        metri = metrics(single, data,pathWorkSpace,params,'single')

        mse = metri[0]
        rmse = metri[1]
        nobs = metri[2]
        nameColumnsObject['modelaccuracy'] = str(hits)

        '''


        Complete

        '''
    if link == 'complete' or link == 'all':

        from scipy.cluster.hierarchy import dendrogram, linkage, cophenet

        linked2 = linkage(X, 'complete')

        labelList = range(len(X))

        plt.figure(figsize=(64, 32))  
        dendrogram(linked2,
                labels = labelList,
                distance_sort = 'descending',
                show_leaf_counts = True)
        # plt.show()  
        plt.savefig(pathWorkSpace + modelName + "_Complete_model_dendogram.png")


        c2=cophenet(linked2,pdist(X))
        c2

        # from sklearn.cluster import AgglomerativeClustering
        # clusters=[]
        # cluster = AgglomerativeClustering(n_clusters=clusterK, affinity='euclidean', linkage='complete')  
        # clusters.append(cluster.fit_predict(X)) 
        # plt.figure(figsize=(10, 7))  
        # plt.scatter(X[:,4], X[:,9], c=cluster.labels_, cmap='rainbow') 
        # #plt.show()
        # plt.savefig(pathWorkSpace + modelName + "_Complete_model_predict.png")


        # save the model
        # pickle.dump(cluster, open(pathWorkSpace + modelName + "_Complete_model.pkl", "wb"))

        # prediction = cluster.fit_predict(raw_data[1:10])

        # for x in prediction:
        #     print('Prediction Complete: ', label_encoder.classes_[x] + ' -> ' + nameBigClassNames[x])

        loadingJSON(userName, 60, 'Building model Complete', False)
        time.sleep(2.0);


        complete, hits = generateSkatterPlot(clusterK, data, classColumn, 'complete', label_encoder, cancer, pathWorkSpace, params,affinity,compute_full_tree,compute_distances)
        metri = metrics(complete, data,pathWorkSpace,params,'complete')

        mse = metri[0]
        rmse = metri[1]
        nobs = metri[2]
        nameColumnsObject['modelaccuracy'] = str(hits) + '%'

        '''
        Wards
        '''
    if link == 'ward' or link == 'all':

        from scipy.cluster.hierarchy import dendrogram, linkage,cophenet

        linked3 = linkage(X, 'ward')

        labelList = range(len(X))

        plt.figure(figsize=(64, 32))  
        dendrogram(linked3,
                labels = labelList,
                distance_sort = 'descending',
                show_leaf_counts = True)
        # plt.show()  
        plt.savefig(pathWorkSpace + modelName + "_Wards_model_dendogram.png")


        c3=cophenet(linked3,pdist(X))
        c3

        # from sklearn.cluster import AgglomerativeClustering
        # clusters=[]
        # cluster = AgglomerativeClustering(n_clusters=clusterK, affinity='euclidean', linkage='ward')  
        # clusters.append(cluster.fit_predict(X)) 
        # plt.figure(figsize=(10, 7))  
        # plt.scatter(X[:,4], X[:,9], c=cluster.labels_, cmap='rainbow') 
        # #plt.show()
        # plt.savefig(pathWorkSpace + modelName + "_Wards_model_predict.png")

        # save the model

        # pickle.dump(cluster, open(pathWorkSpace + modelName + "_Wards_model.pkl", "wb"))

        # prediction = cluster.fit_predict(raw_data[1:10])

        # for x in prediction:
        #     print('Prediction Wards: ', label_encoder.classes_[x] + ' -> ' + nameBigClassNames[x])
        loadingJSON(userName, 70, 'Building model Ward', False)
        time.sleep(2.0);


        ward, hits = generateSkatterPlot(clusterK, data, classColumn, 'ward', label_encoder, cancer, pathWorkSpace, params,'euclidean',compute_full_tree,compute_distances)

        metri = metrics(ward, data, pathWorkSpace,params,'ward')

        mse = metri[0]
        rmse = metri[1]
        nobs = metri[2]
        nameColumnsObject['modelaccuracy'] = str(hits) + '%'


        '''

        Average

        '''

    if link == 'average' or link == 'all':

        from scipy.cluster.hierarchy import dendrogram, linkage, cophenet

        linked4 = linkage(X, 'average')

        labelList = range(len(X))

        plt.figure(figsize=(64, 32))  
        dendrogram(linked4,
                labels = labelList,
                distance_sort = 'descending',
                show_leaf_counts = True)
        max_d=100
        plt.savefig(pathWorkSpace + modelName + "_Average_model_dendogram.png")
        # plt.show()  


        c4=cophenet(linked4,pdist(X))
        c4

        # from sklearn.cluster import AgglomerativeClustering
        # clusters=[]
        # cluster = AgglomerativeClustering(n_clusters=clusterK, affinity='euclidean', linkage='average')  
        # clusters.append(cluster.fit_predict(X)) 
        # plt.figure(figsize=(10, 7))  
        # plt.scatter(X[:,4], X[:,9], c=cluster.labels_, cmap='rainbow') 
        # #plt.show()
        # plt.savefig(pathWorkSpace + modelName + "_Average_model_predict.png")


        # save the model
        # import pickle
        # pickle.dump(cluster, open(pathWorkSpace + modelName + "_Average_model.pkl", "wb"))


        # prediction = cluster.fit_predict(raw_data[1:10])
        #print('Average: ', prediction)

        # for x in prediction:
        #     print('Prediction Average: ', label_encoder.classes_[x] + ' -> ' + nameBigClassNames[x])

        loadingJSON(userName, 80, 'Building model Average', False)
        time.sleep(2.0);

        aver, hits = generateSkatterPlot(clusterK, data, classColumn, 'average', label_encoder, cancer, pathWorkSpace, params,affinity,compute_full_tree,compute_distances)

        metri = metrics(aver, data, pathWorkSpace,params,'average')

        mse = metri[0]
        rmse = metri[1]
        nobs = metri[2]

        nameColumnsObject['modelaccuracy'] = str(hits) + '%'


        time.sleep(2.0)

    # # Obtain the coordinates of the centroids
    # centroids = birch_cluster["clusterer"]["birch"].subcluster_centers_
    # labelsPipe =  birch_cluster["clusterer"]["birch"].predict(raw_data)
    # # Calculate MSE and RMSE of the clusters
    # mse = mean_squared_error(raw_data, centroids[labelsPipe])
    # rmse = np.sqrt(mse)
    # # Calculate the NOBS
    # nobs = raw_data.shape[0]


    # current date and time
    now = datetime.now()
    null = 'null'
    timestamp = datetime.timestamp(now)
    timestamp = int(timestamp * 1000)


         # Object json with information about model for myModels
    data = {
        "__meta": {
            "schema_version": 3,
            "schema_name": "GLMModelV3",
            "schema_type": "GLMModel"
        },
        "model_id": {
            "__meta": {
                "schema_version": 3,
                "schema_name": "ModelKeyV3",
                "schema_type": "Key<Model>"
            },
            "name": params['modelName'],
            "type": "Key<Model>",
            "URL": "/3/Models/" + params['modelName']
        },
        "algo": "Scikit-Learn-Clustering",
        "algo_full_name": "Scikit-Learn-Clustering",
        "response_column_name": "Time_Mean",
        "data_frame": {
            "__meta": {
                "schema_version": 3,
                "schema_name": "FrameKeyV3",
                "schema_type": "Key<Frame>"
            },
            "name": "py_6_sid_803f",
            "type": "Key<Frame>",
            "URL": "/3/Frames/py_6_sid_803f"
        },
        "timestamp": timestamp,
        "have_pojo": 'true',
        "have_mojo": 'true',
        "parameters": [
            {
                "__meta": {
                    "schema_version": 3,
                    "schema_name": "ModelParameterSchemaV3",
                    "schema_type": "Iced"
                },
                "name": "model_id",
                "label": "model_id",
                "help": "Destination id for this model; auto-generated if not specified.",
                "required": 'false',
                "type": "Key<Model>",
                "default_value": null,
                "actual_value": {
                    "__meta": {
                        "schema_version": 3,
                        "schema_name": "ModelKeyV3",
                        "schema_type": "Key<Model>"
                    },
                    "name": params['modelName'],
                    "type": "Key<Model>",
                    "URL": "/3/Models/GLM_V1.20211119"
                },
                "input_value": null,
                "level": "critical",
                "values": [],
                "is_member_of_frames": [],
                "is_mutually_exclusive_with": [],
                "gridable": 'false'
            },
            {
                "__meta": {
                    "schema_version": 3,
                    "schema_name": "ModelParameterSchemaV3",
                    "schema_type": "Iced"
                },
                "name": "training_frame",
                "label": "training_frame",
                "help": "Id of the training data frame.",
                "required": 'false',
                "type": "Key<Frame>",
                "default_value": null,
                "actual_value": null,
                "input_value": null,
                "level": "critical",
                "values": [],
                "is_member_of_frames": [],
                "is_mutually_exclusive_with": [],
                "gridable": 'false'
            },
            {
                "__meta": {
                    "schema_version": 3,
                    "schema_name": "ModelParameterSchemaV3",
                    "schema_type": "Iced"
                },
                "name": "validation_frame",
                "label": "validation_frame",
                "help": "Id of the validation data frame.",
                "required": 'false',
                "type": "Key<Frame>",
                "default_value": null,
                "actual_value": null,
                "input_value": null,
                "level": "critical",
                "values": [],
                "is_member_of_frames": [],
                "is_mutually_exclusive_with": [],
                "gridable": 'true'
            },
            {
                "__meta": {
                    "schema_version": 3,
                    "schema_name": "ModelParameterSchemaV3",
                    "schema_type": "Iced"
                },
                "name": "nfolds",
                "label": "nfolds",
                "help": "Number of folds for K-fold cross-validation (0 to disable or >= 2).",
                "required": 'false',
                "type": "int",
                "default_value": 0,
                "actual_value": 2,
                "input_value": 2,
                "level": "critical",
                "values": [],
                "is_member_of_frames": [],
                "is_mutually_exclusive_with": [],
                "gridable": 'false'
            },
            {
                "__meta": {
                    "schema_version": 3,
                    "schema_name": "ModelParameterSchemaV3",
                    "schema_type": "Iced"
                },
                "name": "checkpoint",
                "label": "checkpoint",
                "help": "Model checkpoint to resume training with.",
                "required": 'false',
                "type": "Key<Model>",
                "default_value": null,
                "actual_value": null,
                "input_value": null,
                "level": "secondary",
                "values": [],
                "is_member_of_frames": [],
                "is_mutually_exclusive_with": [],
                "gridable": 'false'
            },
            {
                "__meta": {
                    "schema_version": 3,
                    "schema_name": "ModelParameterSchemaV3",
                    "schema_type": "Iced"
                },
                "name": "export_checkpoints_dir",
                "label": "export_checkpoints_dir",
                "help": "Automatically export generated models to this directory.",
                "required": 'false',
                "type": "string",
                "default_value": null,
                "actual_value": null,
                "input_value": null,
                "level": "secondary",
                "values": [],
                "is_member_of_frames": [],
                "is_mutually_exclusive_with": [],
                "gridable": 'false'
            },
            {
                "__meta": {
                    "schema_version": 3,
                    "schema_name": "ModelParameterSchemaV3",
                    "schema_type": "Iced"
                },
                "name": "seed",
                "label": "seed",
                "help": "Seed for pseudo random number generator (if applicable)",
                "required": 'false',
                "type": "long",
                "default_value": -1,
                "actual_value": 4,
                "input_value": 4,
                "level": "critical",
                "values": [],
                "is_member_of_frames": [],
                "is_mutually_exclusive_with": [],
                "gridable": 'true'
            },
            {
                "__meta": {
                    "schema_version": 3,
                    "schema_name": "ModelParameterSchemaV3",
                    "schema_type": "Iced"
                },
                "name": "keep_cross_validation_models",
                "label": "keep_cross_validation_models",
                "help": "Whether to keep the cross-validation models.",
                "required": 'false',
                "type": "boolean",
                "default_value": 'true',
                "actual_value": 'true',
                "input_value": 'true',
                "level": "expert",
                "values": [],
                "is_member_of_frames": [],
                "is_mutually_exclusive_with": [],
                "gridable": 'false'
            },
            {
                "__meta": {
                    "schema_version": 3,
                    "schema_name": "ModelParameterSchemaV3",
                    "schema_type": "Iced"
                },
                "name": "keep_cross_validation_predictions",
                "label": "keep_cross_validation_predictions",
                "help": "Whether to keep the predictions of the cross-validation models.",
                "required": 'false',
                "type": "boolean",
                "default_value": 'false',
                "actual_value": 'false',
                "input_value": 'false',
                "level": "expert",
                "values": [],
                "is_member_of_frames": [],
                "is_mutually_exclusive_with": [],
                "gridable": 'false'
            },
            {
                "__meta": {
                    "schema_version": 3,
                    "schema_name": "ModelParameterSchemaV3",
                    "schema_type": "Iced"
                },
                "name": "keep_cross_validation_fold_assignment",
                "label": "keep_cross_validation_fold_assignment",
                "help": "Whether to keep the cross-validation fold assignment.",
                "required": 'false',
                "type": "boolean",
                "default_value": 'false',
                "actual_value": 'false',
                "input_value": 'false',
                "level": "expert",
                "values": [],
                "is_member_of_frames": [],
                "is_mutually_exclusive_with": [],
                "gridable": 'false'
            },
            {
                "__meta": {
                    "schema_version": 3,
                    "schema_name": "ModelParameterSchemaV3",
                    "schema_type": "Iced"
                },
                "name": "fold_assignment",
                "label": "fold_assignment",
                "help": "Cross-validation fold assignment scheme, if fold_column is not specified. The 'Stratified' option will stratify the folds based on the response variable, for classification problems.",
                "required": 'false',
                "type": "enum",
                "default_value": "AUTO",
                "actual_value": "Random",
                "input_value": "AUTO",
                "level": "secondary",
                "values": [
                    "AUTO",
                    "Random",
                    "Modulo",
                    "Stratified"
                ],
                "is_member_of_frames": [],
                "is_mutually_exclusive_with": [],
                "gridable": 'true'
            },
            {
                "__meta": {
                    "schema_version": 3,
                    "schema_name": "ModelParameterSchemaV3",
                    "schema_type": "Iced"
                },
                "name": "fold_column",
                "label": "fold_column",
                "help": "Column with cross-validation fold index assignment per observation.",
                "required": 'false',
                "type": "VecSpecifier",
                "default_value": null,
                "actual_value": null,
                "input_value": null,
                "level": "secondary",
                "values": [],
                "is_member_of_frames": [
                    "training_frame"
                ],
                "is_mutually_exclusive_with": [
                    "response_column",
                    "weights_column",
                    "ignored_columns",
                    "offset_column"
                ],
                "gridable": 'true'
            },
            {
                "__meta": {
                    "schema_version": 3,
                    "schema_name": "ModelParameterSchemaV3",
                    "schema_type": "Iced"
                },
                "name": "response_column",
                "label": "response_column",
                "help": "Response variable column.",
                "required": 'false',
                "type": "VecSpecifier",
                "default_value": null,
                "actual_value": {
                    "__meta": {
                        "schema_version": 3,
                        "schema_name": "ColSpecifierV3",
                        "schema_type": "VecSpecifier"
                    },
                    "column_name": "Time_Mean",
                    "is_member_of_frames": null
                },
                "input_value": {
                    "__meta": {
                        "schema_version": 3,
                        "schema_name": "ColSpecifierV3",
                        "schema_type": "VecSpecifier"
                    },
                    "column_name": "Time_Mean",
                    "is_member_of_frames": null
                },
                "level": "critical",
                "values": [],
                "is_member_of_frames": [
                    "training_frame",
                    "validation_frame"
                ],
                "is_mutually_exclusive_with": [
                    "weights_column",
                    "ignored_columns",
                    "offset_column",
                    "fold_column"
                ],
                "gridable": 'true'
            },
            {
                "__meta": {
                    "schema_version": 3,
                    "schema_name": "ModelParameterSchemaV3",
                    "schema_type": "Iced"
                },
                "name": "ignored_columns",
                "label": "ignored_columns",
                "help": "Names of columns to ignore for training.",
                "required": 'false',
                "type": "string[]",
                "default_value": null,
                "actual_value": null,
                "input_value": null,
                "level": "critical",
                "values": [],
                "is_member_of_frames": [
                    "training_frame",
                    "validation_frame"
                ],
                "is_mutually_exclusive_with": [
                    "response_column",
                    "weights_column",
                    "offset_column",
                    "fold_column"
                ],
                "gridable": 'false'
            },
            {
                "__meta": {
                    "schema_version": 3,
                    "schema_name": "ModelParameterSchemaV3",
                    "schema_type": "Iced"
                },
                "name": "random_columns",
                "label": "random_columns",
                "help": "random columns indices for HGLM.",
                "required": 'false',
                "type": "int[]",
                "default_value": null,
                "actual_value": null,
                "input_value": null,
                "level": "critical",
                "values": [],
                "is_member_of_frames": [],
                "is_mutually_exclusive_with": [],
                "gridable": 'false'
            },
            {
                "__meta": {
                    "schema_version": 3,
                    "schema_name": "ModelParameterSchemaV3",
                    "schema_type": "Iced"
                },
                "name": "ignore_const_cols",
                "label": "ignore_const_cols",
                "help": "Ignore constant columns.",
                "required": 'false',
                "type": "boolean",
                "default_value": 'true',
                "actual_value": 'false',
                "input_value": 'false',
                "level": "critical",
                "values": [],
                "is_member_of_frames": [],
                "is_mutually_exclusive_with": [],
                "gridable": 'false'
            },
            {
                "__meta": {
                    "schema_version": 3,
                    "schema_name": "ModelParameterSchemaV3",
                    "schema_type": "Iced"
                },
                "name": "score_each_iteration",
                "label": "score_each_iteration",
                "help": "Whether to score during each iteration of model training.",
                "required": 'false',
                "type": "boolean",
                "default_value": 'false',
                "actual_value": 'false',
                "input_value": 'false',
                "level": "secondary",
                "values": [],
                "is_member_of_frames": [],
                "is_mutually_exclusive_with": [],
                "gridable": 'false'
            },
            {
                "__meta": {
                    "schema_version": 3,
                    "schema_name": "ModelParameterSchemaV3",
                    "schema_type": "Iced"
                },
                "name": "score_iteration_interval",
                "label": "score_iteration_interval",
                "help": "Perform scoring for every score_iteration_interval iterations",
                "required": 'false',
                "type": "int",
                "default_value": -1,
                "actual_value": -1,
                "input_value": -1,
                "level": "secondary",
                "values": [],
                "is_member_of_frames": [],
                "is_mutually_exclusive_with": [],
                "gridable": 'false'
            },
            {
                "__meta": {
                    "schema_version": 3,
                    "schema_name": "ModelParameterSchemaV3",
                    "schema_type": "Iced"
                },
                "name": "offset_column",
                "label": "offset_column",
                "help": "Offset column. This will be added to the combination of columns before applying the link function.",
                "required": 'false',
                "type": "VecSpecifier",
                "default_value": null,
                "actual_value": null,
                "input_value": null,
                "level": "secondary",
                "values": [],
                "is_member_of_frames": [
                    "training_frame",
                    "validation_frame"
                ],
                "is_mutually_exclusive_with": [
                    "response_column",
                    "weights_column",
                    "ignored_columns",
                    "fold_column"
                ],
                "gridable": 'true'
            },
            {
                "__meta": {
                    "schema_version": 3,
                    "schema_name": "ModelParameterSchemaV3",
                    "schema_type": "Iced"
                },
                "name": "weights_column",
                "label": "weights_column",
                "help": "Column with observation weights. Giving some observation a weight of zero is equivalent to excluding it from the dataset; giving an observation a relative weight of 2 is equivalent to repeating that row twice. Negative weights are not allowed. Note: Weights are per-row observation weights and do not increase the size of the data frame. This is typically the number of times a row is repeated, but non-integer values are supported as well. During training, rows with higher weights matter more, due to the larger loss function pre-factor.",
                "required": 'false',
                "type": "VecSpecifier",
                "default_value": null,
                "actual_value": null,
                "input_value": null,
                "level": "secondary",
                "values": [],
                "is_member_of_frames": [
                    "training_frame",
                    "validation_frame"
                ],
                "is_mutually_exclusive_with": [
                    "response_column",
                    "ignored_columns",
                    "offset_column",
                    "fold_column"
                ],
                "gridable": 'true'
            },
            {
                "__meta": {
                    "schema_version": 3,
                    "schema_name": "ModelParameterSchemaV3",
                    "schema_type": "Iced"
                },
                "name": "family",
                "label": "family",
                "help": "Family. Use binomial for classification with logistic regression, others are for regression problems.",
                "required": 'false',
                "type": "enum",
                "default_value": "AUTO",
                "actual_value": "gaussian",
                "input_value": "gaussian",
                "level": "critical",
                "values": [
                    "AUTO",
                    "gaussian",
                    "binomial",
                    "fractionalbinomial",
                    "quasibinomial",
                    "ordinal",
                    "multinomial",
                    "poisson",
                    "gamma",
                    "tweedie",
                    "negativebinomial"
                ],
                "is_member_of_frames": [],
                "is_mutually_exclusive_with": [],
                "gridable": 'false'
            },
            {
                "__meta": {
                    "schema_version": 3,
                    "schema_name": "ModelParameterSchemaV3",
                    "schema_type": "Iced"
                },
                "name": "rand_family",
                "label": "rand_family",
                "help": "Random Component Family array.  One for each random component. Only support gaussian for now.",
                "required": 'false',
                "type": "enum[]",
                "default_value": null,
                "actual_value": null,
                "input_value": null,
                "level": "critical",
                "values": [
                    "[gaussian]"
                ],
                "is_member_of_frames": [],
                "is_mutually_exclusive_with": [],
                "gridable": 'true'
            },
            {
                "__meta": {
                    "schema_version": 3,
                    "schema_name": "ModelParameterSchemaV3",
                    "schema_type": "Iced"
                },
                "name": "tweedie_variance_power",
                "label": "tweedie_variance_power",
                "help": "Tweedie variance power",
                "required": 'false',
                "type": "double",
                "default_value": 0.0,
                "actual_value": 0.0,
                "input_value": 0.0,
                "level": "critical",
                "values": [],
                "is_member_of_frames": [],
                "is_mutually_exclusive_with": [],
                "gridable": 'true'
            },
            {
                "__meta": {
                    "schema_version": 3,
                    "schema_name": "ModelParameterSchemaV3",
                    "schema_type": "Iced"
                },
                "name": "tweedie_link_power",
                "label": "tweedie_link_power",
                "help": "Tweedie link power",
                "required": 'false',
                "type": "double",
                "default_value": 1.0,
                "actual_value": 1.0,
                "input_value": 1.0,
                "level": "critical",
                "values": [],
                "is_member_of_frames": [],
                "is_mutually_exclusive_with": [],
                "gridable": 'true'
            },
            {
                "__meta": {
                    "schema_version": 3,
                    "schema_name": "ModelParameterSchemaV3",
                    "schema_type": "Iced"
                },
                "name": "theta",
                "label": "theta",
                "help": "Theta",
                "required": 'false',
                "type": "double",
                "default_value": 1e-10,
                "actual_value": 1e-10,
                "input_value": 1e-10,
                "level": "critical",
                "values": [],
                "is_member_of_frames": [],
                "is_mutually_exclusive_with": [],
                "gridable": 'true'
            },
            {
                "__meta": {
                    "schema_version": 3,
                    "schema_name": "ModelParameterSchemaV3",
                    "schema_type": "Iced"
                },
                "name": "solver",
                "label": "solver",
                "help": "AUTO will set the solver based on given data and the other parameters. IRLSM is fast on on problems with small number of predictors and for lambda-search with L1 penalty, L_BFGS scales better for datasets with many columns.",
                "required": 'false',
                "type": "enum",
                "default_value": "AUTO",
                "actual_value": "IRLSM",
                "input_value": "AUTO",
                "level": "critical",
                "values": [
                    "AUTO",
                    "IRLSM",
                    "L_BFGS",
                    "COORDINATE_DESCENT_NAIVE",
                    "COORDINATE_DESCENT",
                    "GRADIENT_DESCENT_LH",
                    "GRADIENT_DESCENT_SQERR"
                ],
                "is_member_of_frames": [],
                "is_mutually_exclusive_with": [],
                "gridable": 'false'
            },
            {
                "__meta": {
                    "schema_version": 3,
                    "schema_name": "ModelParameterSchemaV3",
                    "schema_type": "Iced"
                },
                "name": "alpha",
                "label": "alpha",
                "help": "Distribution of regularization between the L1 (Lasso) and L2 (Ridge) penalties. A value of 1 for alpha represents Lasso regression, a value of 0 produces Ridge regression, and anything in between specifies the amount of mixing between the two. Default value of alpha is 0 when SOLVER = 'L-BFGS'; 0.5 otherwise.",
                "required": 'false',
                "type": "double[]",
                "default_value": null,
                "actual_value": [
                    0.25
                ],
                "input_value": [
                    0.25
                ],
                "level": "critical",
                "values": [],
                "is_member_of_frames": [],
                "is_mutually_exclusive_with": [],
                "gridable": 'true'
            },
            {
                "__meta": {
                    "schema_version": 3,
                    "schema_name": "ModelParameterSchemaV3",
                    "schema_type": "Iced"
                },
                "name": "lambda",
                "label": "lambda",
                "help": "Regularization strength",
                "required": 'false',
                "type": "double[]",
                "default_value": null,
                "actual_value": [
                    0.12740979773555953
                ],
                "input_value": null,
                "level": "critical",
                "values": [],
                "is_member_of_frames": [],
                "is_mutually_exclusive_with": [],
                "gridable": 'true'
            },
            {
                "__meta": {
                    "schema_version": 3,
                    "schema_name": "ModelParameterSchemaV3",
                    "schema_type": "Iced"
                },
                "name": "lambda_search",
                "label": "lambda_search",
                "help": "Use lambda search starting at lambda max, given lambda is then interpreted as lambda min",
                "required": 'false',
                "type": "boolean",
                "default_value": 'false',
                "actual_value": 'false',
                "input_value": 'false',
                "level": "critical",
                "values": [],
                "is_member_of_frames": [],
                "is_mutually_exclusive_with": [],
                "gridable": 'false'
            },
            {
                "__meta": {
                    "schema_version": 3,
                    "schema_name": "ModelParameterSchemaV3",
                    "schema_type": "Iced"
                },
                "name": "early_stopping",
                "label": "early_stopping",
                "help": "Stop early when there is no more relative improvement on train or validation (if provided)",
                "required": 'false',
                "type": "boolean",
                "default_value": 'true',
                "actual_value": 'true',
                "input_value": 'true',
                "level": "critical",
                "values": [],
                "is_member_of_frames": [],
                "is_mutually_exclusive_with": [],
                "gridable": 'false'
            },
            {
                "__meta": {
                    "schema_version": 3,
                    "schema_name": "ModelParameterSchemaV3",
                    "schema_type": "Iced"
                },
                "name": "nlambdas",
                "label": "nlambdas",
                "help": "Number of lambdas to be used in a search. Default indicates: If alpha is zero, with lambda search set to True, the value of nlamdas is set to 30 (fewer lambdas are needed for ridge regression) otherwise it is set to 100.",
                "required": 'false',
                "type": "int",
                "default_value": -1,
                "actual_value": -1,
                "input_value": -1,
                "level": "critical",
                "values": [],
                "is_member_of_frames": [],
                "is_mutually_exclusive_with": [],
                "gridable": 'false'
            },
            {
                "__meta": {
                    "schema_version": 3,
                    "schema_name": "ModelParameterSchemaV3",
                    "schema_type": "Iced"
                },
                "name": "standardize",
                "label": "standardize",
                "help": "Standardize numeric columns to have zero mean and unit variance",
                "required": 'false',
                "type": "boolean",
                "default_value": 'true',
                "actual_value": 'true',
                "input_value": 'true',
                "level": "critical",
                "values": [],
                "is_member_of_frames": [],
                "is_mutually_exclusive_with": [],
                "gridable": 'false'
            },
            {
                "__meta": {
                    "schema_version": 3,
                    "schema_name": "ModelParameterSchemaV3",
                    "schema_type": "Iced"
                },
                "name": "missing_values_handling",
                "label": "missing_values_handling",
                "help": "Handling of missing values. Either MeanImputation, Skip or PlugValues.",
                "required": 'false',
                "type": "enum",
                "default_value": "MeanImputation",
                "actual_value": "Skip",
                "input_value": "Skip",
                "level": "expert",
                "values": [
                    "MeanImputation",
                    "Skip",
                    "PlugValues"
                ],
                "is_member_of_frames": [],
                "is_mutually_exclusive_with": [],
                "gridable": 'true'
            },
            {
                "__meta": {
                    "schema_version": 3,
                    "schema_name": "ModelParameterSchemaV3",
                    "schema_type": "Iced"
                },
                "name": "plug_values",
                "label": "plug_values",
                "help": "Plug Values (a single row frame containing values that will be used to impute missing values of the training/validation frame, use with conjunction missing_values_handling = PlugValues)",
                "required": 'false',
                "type": "Key<Frame>",
                "default_value": null,
                "actual_value": null,
                "input_value": null,
                "level": "critical",
                "values": [],
                "is_member_of_frames": [],
                "is_mutually_exclusive_with": [],
                "gridable": 'false'
            },
            {
                "__meta": {
                    "schema_version": 3,
                    "schema_name": "ModelParameterSchemaV3",
                    "schema_type": "Iced"
                },
                "name": "compute_p_values",
                "label": "compute_p_values",
                "help": "Request p-values computation, p-values work only with IRLSM solver and no regularization",
                "required": 'false',
                "type": "boolean",
                "default_value": 'false',
                "actual_value": 'false',
                "input_value": 'false',
                "level": "secondary",
                "values": [],
                "is_member_of_frames": [],
                "is_mutually_exclusive_with": [],
                "gridable": 'false'
            },
            {
                "__meta": {
                    "schema_version": 3,
                    "schema_name": "ModelParameterSchemaV3",
                    "schema_type": "Iced"
                },
                "name": "remove_collinear_columns",
                "label": "remove_collinear_columns",
                "help": "In case of linearly dependent columns, remove some of the dependent columns",
                "required": 'false',
                "type": "boolean",
                "default_value": 'false',
                "actual_value": 'false',
                "input_value": 'false',
                "level": "secondary",
                "values": [],
                "is_member_of_frames": [],
                "is_mutually_exclusive_with": [],
                "gridable": 'false'
            },
            {
                "__meta": {
                    "schema_version": 3,
                    "schema_name": "ModelParameterSchemaV3",
                    "schema_type": "Iced"
                },
                "name": "intercept",
                "label": "intercept",
                "help": "Include constant term in the model",
                "required": 'false',
                "type": "boolean",
                "default_value": 'true',
                "actual_value": 'true',
                "input_value": 'true',
                "level": "expert",
                "values": [],
                "is_member_of_frames": [],
                "is_mutually_exclusive_with": [],
                "gridable": 'false'
            },
            {
                "__meta": {
                    "schema_version": 3,
                    "schema_name": "ModelParameterSchemaV3",
                    "schema_type": "Iced"
                },
                "name": "non_negative",
                "label": "non_negative",
                "help": "Restrict coefficients (not intercept) to be non-negative",
                "required": 'false',
                "type": "boolean",
                "default_value": 'false',
                "actual_value": 'false',
                "input_value": 'false',
                "level": "critical",
                "values": [],
                "is_member_of_frames": [],
                "is_mutually_exclusive_with": [],
                "gridable": 'false'
            },
            {
                "__meta": {
                    "schema_version": 3,
                    "schema_name": "ModelParameterSchemaV3",
                    "schema_type": "Iced"
                },
                "name": "max_iterations",
                "label": "max_iterations",
                "help": "Maximum number of iterations",
                "required": 'false',
                "type": "int",
                "default_value": -1,
                "actual_value": 50,
                "input_value": -1,
                "level": "secondary",
                "values": [],
                "is_member_of_frames": [],
                "is_mutually_exclusive_with": [],
                "gridable": 'false'
            },
            {
                "__meta": {
                    "schema_version": 3,
                    "schema_name": "ModelParameterSchemaV3",
                    "schema_type": "Iced"
                },
                "name": "objective_epsilon",
                "label": "objective_epsilon",
                "help": "Converge if  objective value changes less than this. Default indicates: If lambda_search is set to True the value of objective_epsilon is set to .0001. If the lambda_search is set to False and lambda is equal to zero, the value of objective_epsilon is set to .000001, for any other value of lambda the default value of objective_epsilon is set to .0001.",
                "required": 'false',
                "type": "double",
                "default_value": -1.0,
                "actual_value": 0.0001,
                "input_value": -1.0,
                "level": "expert",
                "values": [],
                "is_member_of_frames": [],
                "is_mutually_exclusive_with": [],
                "gridable": 'false'
            },
            {
                "__meta": {
                    "schema_version": 3,
                    "schema_name": "ModelParameterSchemaV3",
                    "schema_type": "Iced"
                },
                "name": "beta_epsilon",
                "label": "beta_epsilon",
                "help": "Converge if  beta changes less (using L-infinity norm) than beta esilon, ONLY applies to IRLSM solver ",
                "required": 'false',
                "type": "double",
                "default_value": 0.0001,
                "actual_value": 0.0001,
                "input_value": 0.0001,
                "level": "expert",
                "values": [],
                "is_member_of_frames": [],
                "is_mutually_exclusive_with": [],
                "gridable": 'false'
            },
            {
                "__meta": {
                    "schema_version": 3,
                    "schema_name": "ModelParameterSchemaV3",
                    "schema_type": "Iced"
                },
                "name": "gradient_epsilon",
                "label": "gradient_epsilon",
                "help": "Converge if  objective changes less (using L-infinity norm) than this, ONLY applies to L-BFGS solver. Default indicates: If lambda_search is set to False and lambda is equal to zero, the default value of gradient_epsilon is equal to .000001, otherwise the default value is .0001. If lambda_search is set to True, the conditional values above are 1E-8 and 1E-6 respectively.",
                "required": 'false',
                "type": "double",
                "default_value": -1.0,
                "actual_value": 0.0001,
                "input_value": -1.0,
                "level": "expert",
                "values": [],
                "is_member_of_frames": [],
                "is_mutually_exclusive_with": [],
                "gridable": 'false'
            },
            {
                "__meta": {
                    "schema_version": 3,
                    "schema_name": "ModelParameterSchemaV3",
                    "schema_type": "Iced"
                },
                "name": "link",
                "label": "link",
                "help": "Link function.",
                "required": 'false',
                "type": "enum",
                "default_value": "family_default",
                "actual_value": "identity",
                "input_value": "identity",
                "level": "secondary",
                "values": [
                    "family_default",
                    "identity",
                    "logit",
                    "log",
                    "inverse",
                    "tweedie",
                    "ologit"
                ],
                "is_member_of_frames": [],
                "is_mutually_exclusive_with": [],
                "gridable": 'false'
            },
            {
                "__meta": {
                    "schema_version": 3,
                    "schema_name": "ModelParameterSchemaV3",
                    "schema_type": "Iced"
                },
                "name": "rand_link",
                "label": "rand_link",
                "help": "Link function array for random component in HGLM.",
                "required": 'false',
                "type": "enum[]",
                "default_value": null,
                "actual_value": null,
                "input_value": null,
                "level": "secondary",
                "values": [
                    "[identity]",
                    "[family_default]"
                ],
                "is_member_of_frames": [],
                "is_mutually_exclusive_with": [],
                "gridable": 'true'
            },
            {
                "__meta": {
                    "schema_version": 3,
                    "schema_name": "ModelParameterSchemaV3",
                    "schema_type": "Iced"
                },
                "name": "startval",
                "label": "startval",
                "help": "double array to initialize fixed and random coefficients for HGLM, coefficients for GLM.",
                "required": 'false',
                "type": "double[]",
                "default_value": null,
                "actual_value": null,
                "input_value": null,
                "level": "critical",
                "values": [],
                "is_member_of_frames": [],
                "is_mutually_exclusive_with": [],
                "gridable": 'true'
            },
            {
                "__meta": {
                    "schema_version": 3,
                    "schema_name": "ModelParameterSchemaV3",
                    "schema_type": "Iced"
                },
                "name": "calc_like",
                "label": "calc_like",
                "help": "if 'true', will return likelihood function value for HGLM.",
                "required": 'false',
                "type": "boolean",
                "default_value": 'false',
                "actual_value": 'false',
                "input_value": 'false',
                "level": "critical",
                "values": [],
                "is_member_of_frames": [],
                "is_mutually_exclusive_with": [],
                "gridable": 'false'
            },
            {
                "__meta": {
                    "schema_version": 3,
                    "schema_name": "ModelParameterSchemaV3",
                    "schema_type": "Iced"
                },
                "name": "HGLM",
                "label": "HGLM",
                "help": "If set to 'true', will return HGLM model.  Otherwise, normal GLM model will be returned",
                "required": 'false',
                "type": "boolean",
                "default_value": 'false',
                "actual_value": 'false',
                "input_value": 'false',
                "level": "critical",
                "values": [],
                "is_member_of_frames": [],
                "is_mutually_exclusive_with": [],
                "gridable": 'false'
            },
            {
                "__meta": {
                    "schema_version": 3,
                    "schema_name": "ModelParameterSchemaV3",
                    "schema_type": "Iced"
                },
                "name": "prior",
                "label": "prior",
                "help": "Prior probability for y==1. To be used only for logistic regression iff the data has been sampled and the mean of response does not reflect reality.",
                "required": 'false',
                "type": "double",
                "default_value": -1.0,
                "actual_value": -1.0,
                "input_value": -1.0,
                "level": "expert",
                "values": [],
                "is_member_of_frames": [],
                "is_mutually_exclusive_with": [],
                "gridable": 'false'
            },
            {
                "__meta": {
                    "schema_version": 3,
                    "schema_name": "ModelParameterSchemaV3",
                    "schema_type": "Iced"
                },
                "name": "cold_start",
                "label": "cold_start",
                "help": "Only applicable to multiple alpha/lambda values.  If 'false', build the next model for next set of alpha/lambda values starting from the values provided by current model.  If 'true' will start GLM model from scratch.",
                "required": 'false',
                "type": "boolean",
                "default_value": 'false',
                "actual_value": 'false',
                "input_value": 'false',
                "level": "critical",
                "values": [],
                "is_member_of_frames": [],
                "is_mutually_exclusive_with": [],
                "gridable": 'false'
            },
            {
                "__meta": {
                    "schema_version": 3,
                    "schema_name": "ModelParameterSchemaV3",
                    "schema_type": "Iced"
                },
                "name": "lambda_min_ratio",
                "label": "lambda_min_ratio",
                "help": "Minimum lambda used in lambda search, specified as a ratio of lambda_max (the smallest lambda that drives all coefficients to zero). Default indicates: if the number of observations is greater than the number of variables, then lambda_min_ratio is set to 0.0001; if the number of observations is less than the number of variables, then lambda_min_ratio is set to 0.01.",
                "required": 'false',
                "type": "double",
                "default_value": -1.0,
                "actual_value": 0.0001,
                "input_value": -1.0,
                "level": "expert",
                "values": [],
                "is_member_of_frames": [],
                "is_mutually_exclusive_with": [],
                "gridable": 'false'
            },
            {
                "__meta": {
                    "schema_version": 3,
                    "schema_name": "ModelParameterSchemaV3",
                    "schema_type": "Iced"
                },
                "name": "beta_constraints",
                "label": "beta_constraints",
                "help": "Beta constraints",
                "required": 'false',
                "type": "Key<Frame>",
                "default_value": null,
                "actual_value": null,
                "input_value": null,
                "level": "critical",
                "values": [],
                "is_member_of_frames": [],
                "is_mutually_exclusive_with": [],
                "gridable": 'false'
            },
            {
                "__meta": {
                    "schema_version": 3,
                    "schema_name": "ModelParameterSchemaV3",
                    "schema_type": "Iced"
                },
                "name": "max_active_predictors",
                "label": "max_active_predictors",
                "help": "Maximum number of active predictors during computation. Use as a stopping criterion to prevent expensive model building with many predictors. Default indicates: If the IRLSM solver is used, the value of max_active_predictors is set to 5000 otherwise it is set to 100000000.",
                "required": 'false',
                "type": "int",
                "default_value": -1,
                "actual_value": 5000,
                "input_value": -1,
                "level": "expert",
                "values": [],
                "is_member_of_frames": [],
                "is_mutually_exclusive_with": [],
                "gridable": 'false'
            },
            {
                "__meta": {
                    "schema_version": 3,
                    "schema_name": "ModelParameterSchemaV3",
                    "schema_type": "Iced"
                },
                "name": "interactions",
                "label": "interactions",
                "help": "A list of predictor column indices to interact. All pairwise combinations will be computed for the list.",
                "required": 'false',
                "type": "string[]",
                "default_value": null,
                "actual_value": null,
                "input_value": null,
                "level": "expert",
                "values": [],
                "is_member_of_frames": [],
                "is_mutually_exclusive_with": [],
                "gridable": 'false'
            },
            {
                "__meta": {
                    "schema_version": 3,
                    "schema_name": "ModelParameterSchemaV3",
                    "schema_type": "Iced"
                },
                "name": "interaction_pairs",
                "label": "interaction_pairs",
                "help": "A list of pairwise (first order) column interactions.",
                "required": 'false',
                "type": "StringPair[]",
                "default_value": null,
                "actual_value": null,
                "input_value": null,
                "level": "expert",
                "values": [],
                "is_member_of_frames": [],
                "is_mutually_exclusive_with": [],
                "gridable": 'false'
            },
            {
                "__meta": {
                    "schema_version": 3,
                    "schema_name": "ModelParameterSchemaV3",
                    "schema_type": "Iced"
                },
                "name": "obj_reg",
                "label": "obj_reg",
                "help": "Likelihood divider in objective value computation, default is 1/nobs",
                "required": 'false',
                "type": "double",
                "default_value": -1.0,
                "actual_value": 0.00013377926421404682,
                "input_value": -1.0,
                "level": "critical",
                "values": [],
                "is_member_of_frames": [],
                "is_mutually_exclusive_with": [],
                "gridable": 'false'
            },
            {
                "__meta": {
                    "schema_version": 3,
                    "schema_name": "ModelParameterSchemaV3",
                    "schema_type": "Iced"
                },
                "name": "stopping_rounds",
                "label": "stopping_rounds",
                "help": "Early stopping based on convergence of stopping_metric. Stop if simple moving average of length k of the stopping_metric does not improve for k:=stopping_rounds scoring events (0 to disable)",
                "required": 'false',
                "type": "int",
                "default_value": 0,
                "actual_value": 0,
                "input_value": 0,
                "level": "secondary",
                "values": [],
                "is_member_of_frames": [],
                "is_mutually_exclusive_with": [],
                "gridable": 'true'
            },
            {
                "__meta": {
                    "schema_version": 3,
                    "schema_name": "ModelParameterSchemaV3",
                    "schema_type": "Iced"
                },
                "name": "stopping_metric",
                "label": "stopping_metric",
                "help": "Metric to use for early stopping (AUTO: logloss for classification, deviance for regression and anonomaly_score for Isolation Forest). Note that custom and custom_increasing can only be used in GBM and DRF with the Python client.",
                "required": 'false',
                "type": "enum",
                "default_value": "AUTO",
                "actual_value": "AUTO",
                "input_value": "AUTO",
                "level": "secondary",
                "values": [
                    "AUTO",
                    "deviance",
                    "logloss",
                    "MSE",
                    "RMSE",
                    "MAE",
                    "RMSLE",
                    "AUC",
                    "AUCPR",
                    "lift_top_group",
                    "misclassification",
                    "mean_per_class_error",
                    "anomaly_score",
                    "custom",
                    "custom_increasing"
                ],
                "is_member_of_frames": [],
                "is_mutually_exclusive_with": [],
                "gridable": 'true'
            },
            {
                "__meta": {
                    "schema_version": 3,
                    "schema_name": "ModelParameterSchemaV3",
                    "schema_type": "Iced"
                },
                "name": "stopping_tolerance",
                "label": "stopping_tolerance",
                "help": "Relative tolerance for metric-based stopping criterion (stop if relative improvement is not at least this much)",
                "required": 'false',
                "type": "double",
                "default_value": 0.001,
                "actual_value": 0.001,
                "input_value": 0.001,
                "level": "secondary",
                "values": [],
                "is_member_of_frames": [],
                "is_mutually_exclusive_with": [],
                "gridable": 'true'
            },
            {
                "__meta": {
                    "schema_version": 3,
                    "schema_name": "ModelParameterSchemaV3",
                    "schema_type": "Iced"
                },
                "name": "balance_classes",
                "label": "balance_classes",
                "help": "Balance training data class counts via over/under-sampling (for imbalanced data).",
                "required": 'false',
                "type": "boolean",
                "default_value": 'false',
                "actual_value": 'false',
                "input_value": 'false',
                "level": "secondary",
                "values": [],
                "is_member_of_frames": [],
                "is_mutually_exclusive_with": [],
                "gridable": 'false'
            },
            {
                "__meta": {
                    "schema_version": 3,
                    "schema_name": "ModelParameterSchemaV3",
                    "schema_type": "Iced"
                },
                "name": "class_sampling_factors",
                "label": "class_sampling_factors",
                "help": "Desired over/under-sampling ratios per class (in lexicographic order). If not specified, sampling factors will be automatically computed to obtain class balance during training. Requires balance_classes.",
                "required": 'false',
                "type": "float[]",
                "default_value": null,
                "actual_value": null,
                "input_value": null,
                "level": "expert",
                "values": [],
                "is_member_of_frames": [],
                "is_mutually_exclusive_with": [],
                "gridable": 'false'
            },
            {
                "__meta": {
                    "schema_version": 3,
                    "schema_name": "ModelParameterSchemaV3",
                    "schema_type": "Iced"
                },
                "name": "max_after_balance_size",
                "label": "max_after_balance_size",
                "help": "Maximum relative size of the training data after balancing class counts (can be less than 1.0). Requires balance_classes.",
                "required": 'false',
                "type": "float",
                "default_value": 5.0,
                "actual_value": 5.0,
                "input_value": 5.0,
                "level": "expert",
                "values": [],
                "is_member_of_frames": [],
                "is_mutually_exclusive_with": [],
                "gridable": 'false'
            },
            {
                "__meta": {
                    "schema_version": 3,
                    "schema_name": "ModelParameterSchemaV3",
                    "schema_type": "Iced"
                },
                "name": "max_confusion_matrix_size",
                "label": "max_confusion_matrix_size",
                "help": "[Deprecated] Maximum size (# classes) for confusion matrices to be printed in the Logs",
                "required": 'false',
                "type": "int",
                "default_value": 20,
                "actual_value": 20,
                "input_value": 20,
                "level": "secondary",
                "values": [],
                "is_member_of_frames": [],
                "is_mutually_exclusive_with": [],
                "gridable": 'false'
            },
            {
                "__meta": {
                    "schema_version": 3,
                    "schema_name": "ModelParameterSchemaV3",
                    "schema_type": "Iced"
                },
                "name": "max_runtime_secs",
                "label": "max_runtime_secs",
                "help": "Maximum allowed runtime in seconds for model training. Use 0 to disable.",
                "required": 'false',
                "type": "double",
                "default_value": 0.0,
                "actual_value": 0.0,
                "input_value": 0.0,
                "level": "secondary",
                "values": [],
                "is_member_of_frames": [],
                "is_mutually_exclusive_with": [],
                "gridable": 'true'
            },
            {
                "__meta": {
                    "schema_version": 3,
                    "schema_name": "ModelParameterSchemaV3",
                    "schema_type": "Iced"
                },
                "name": "custom_metric_func",
                "label": "custom_metric_func",
                "help": "Reference to custom evaluation function, format: `language:keyName=funcName`",
                "required": 'false',
                "type": "string",
                "default_value": null,
                "actual_value": null,
                "input_value": null,
                "level": "secondary",
                "values": [],
                "is_member_of_frames": [],
                "is_mutually_exclusive_with": [],
                "gridable": 'false'
            },
            {
                "__meta": {
                    "schema_version": 3,
                    "schema_name": "ModelParameterSchemaV3",
                    "schema_type": "Iced"
                },
                "name": "auc_type",
                "label": "auc_type",
                "help": "Set default multinomial AUC type.",
                "required": 'false',
                "type": "enum",
                "default_value": "AUTO",
                "actual_value": "AUTO",
                "input_value": "AUTO",
                "level": "secondary",
                "values": [
                    "AUTO",
                    "NONE",
                    "MACRO_OVR",
                    "WEIGHTED_OVR",
                    "MACRO_OVO",
                    "WEIGHTED_OVO"
                ],
                "is_member_of_frames": [],
                "is_mutually_exclusive_with": [],
                "gridable": 'true'
            }
        ],
        "output": {
            "__meta": {
                "schema_version": 3,
                "schema_name": "GLMModelOutputV3",
                "schema_type": "GLMOutput"
            },
            "names": [
                "Time_Desv",
                "Time_Max",
                "Time_Min",
                "Time_Mean"
            ],
            "original_names": null,
            "column_types": [
                "Numeric",
                "Numeric",
                "Numeric",
                "Numeric"
            ],
            "domains": [
                null,
                null,
                null,
                null
            ],
            "cross_validation_models": [
                {
                    "__meta": {
                        "schema_version": 3,
                        "schema_name": "ModelKeyV3",
                        "schema_type": "Key<Model>"
                    },
                    "name": "GLM_V1.20211119_cv_1",
                    "type": "Key<Model>",
                    "URL": "/3/Models/GLM_V1.20211119_cv_1"
                },
                {
                    "__meta": {
                        "schema_version": 3,
                        "schema_name": "ModelKeyV3",
                        "schema_type": "Key<Model>"
                    },
                    "name": "GLM_V1.20211119_cv_2",
                    "type": "Key<Model>",
                    "URL": "/3/Models/GLM_V1.20211119_cv_2"
                }
            ],
            "cross_validation_predictions": null,
            "cross_validation_holdout_predictions_frame_id": null,
            "cross_validation_fold_assignment_frame_id": null,
            "model_category": "Regression",
            "model_summary": {
                "__meta": {
                    "schema_version": 3,
                    "schema_name": "TwoDimTableV3",
                    "schema_type": "TwoDimTable"
                },
                "name": "GLM Model",
                "description": "summary",
                "columns": [
                    {
                        "__meta": {
                            "schema_version": -1,
                            "schema_name": "ColumnSpecsBase",
                            "schema_type": "Iced"
                        },
                        "name": "",
                        "type": "string",
                        "format": "%s",
                        "description": ""
                    },
                    {
                        "__meta": {
                            "schema_version": -1,
                            "schema_name": "ColumnSpecsBase",
                            "schema_type": "Iced"
                        },
                        "name": "family",
                        "type": "string",
                        "format": "%s",
                        "description": "Family"
                    },
                    {
                        "__meta": {
                            "schema_version": -1,
                            "schema_name": "ColumnSpecsBase",
                            "schema_type": "Iced"
                        },
                        "name": "link",
                        "type": "string",
                        "format": "%s",
                        "description": "Link"
                    },
                    {
                        "__meta": {
                            "schema_version": -1,
                            "schema_name": "ColumnSpecsBase",
                            "schema_type": "Iced"
                        },
                        "name": "regularization",
                        "type": "string",
                        "format": "%s",
                        "description": "Regularization"
                    },
                    {
                        "__meta": {
                            "schema_version": -1,
                            "schema_name": "ColumnSpecsBase",
                            "schema_type": "Iced"
                        },
                        "name": "number_of_predictors_total",
                        "type": "int",
                        "format": "%d",
                        "description": "Number of Predictors Total"
                    },
                    {
                        "__meta": {
                            "schema_version": -1,
                            "schema_name": "ColumnSpecsBase",
                            "schema_type": "Iced"
                        },
                        "name": "number_of_active_predictors",
                        "type": "int",
                        "format": "%d",
                        "description": "Number of Active Predictors"
                    },
                    {
                        "__meta": {
                            "schema_version": -1,
                            "schema_name": "ColumnSpecsBase",
                            "schema_type": "Iced"
                        },
                        "name": "number_of_iterations",
                        "type": "int",
                        "format": "%d",
                        "description": "Number of Iterations"
                    },
                    {
                        "__meta": {
                            "schema_version": -1,
                            "schema_name": "ColumnSpecsBase",
                            "schema_type": "Iced"
                        },
                        "name": "training_frame",
                        "type": "string",
                        "format": "%s",
                        "description": "Training Frame"
                    }
                ],
                "rowcount": 1,
                "data": [
                    [
                        ""
                    ],
                    [
                        "gaussian"
                    ],
                    [
                        "identity"
                    ],
                    [
                        "Elastic Net (alpha = 0.25, lambda = 0.1274 )"
                    ],
                    [
                        3
                    ],
                    [
                        "3"
                    ],
                    [
                        1
                    ],
                    [
                        "py_6_sid_803f"
                    ]
                ]
            },
            "scoring_history": {
                "__meta": {
                    "schema_version": 3,
                    "schema_name": "TwoDimTableV3",
                    "schema_type": "TwoDimTable"
                },
                "name": "Scoring History",
                "description": "",
                "columns": [
                    {
                        "__meta": {
                            "schema_version": -1,
                            "schema_name": "ColumnSpecsBase",
                            "schema_type": "Iced"
                        },
                        "name": "",
                        "type": "string",
                        "format": "%s",
                        "description": ""
                    },
                    {
                        "__meta": {
                            "schema_version": -1,
                            "schema_name": "ColumnSpecsBase",
                            "schema_type": "Iced"
                        },
                        "name": "timestamp",
                        "type": "string",
                        "format": "%s",
                        "description": "timestamp"
                    },
                    {
                        "__meta": {
                            "schema_version": -1,
                            "schema_name": "ColumnSpecsBase",
                            "schema_type": "Iced"
                        },
                        "name": "duration",
                        "type": "string",
                        "format": "%s",
                        "description": "duration"
                    },
                    {
                        "__meta": {
                            "schema_version": -1,
                            "schema_name": "ColumnSpecsBase",
                            "schema_type": "Iced"
                        },
                        "name": "iterations",
                        "type": "int",
                        "format": "%d",
                        "description": "iterations"
                    },
                    {
                        "__meta": {
                            "schema_version": -1,
                            "schema_name": "ColumnSpecsBase",
                            "schema_type": "Iced"
                        },
                        "name": "negative_log_likelihood",
                        "type": "double",
                        "format": "%.5f",
                        "description": "negative_log_likelihood"
                    },
                    {
                        "__meta": {
                            "schema_version": -1,
                            "schema_name": "ColumnSpecsBase",
                            "schema_type": "Iced"
                        },
                        "name": "objective",
                        "type": "double",
                        "format": "%.5f",
                        "description": "objective"
                    },
                    {
                        "__meta": {
                            "schema_version": -1,
                            "schema_name": "ColumnSpecsBase",
                            "schema_type": "Iced"
                        },
                        "name": "training_rmse",
                        "type": "double",
                        "format": "%.5f",
                        "description": "Training RMSE"
                    },
                    {
                        "__meta": {
                            "schema_version": -1,
                            "schema_name": "ColumnSpecsBase",
                            "schema_type": "Iced"
                        },
                        "name": "training_deviance",
                        "type": "double",
                        "format": "%.5f",
                        "description": "Training Deviance"
                    },
                    {
                        "__meta": {
                            "schema_version": -1,
                            "schema_name": "ColumnSpecsBase",
                            "schema_type": "Iced"
                        },
                        "name": "training_mae",
                        "type": "double",
                        "format": "%.5f",
                        "description": "Training MAE"
                    },
                    {
                        "__meta": {
                            "schema_version": -1,
                            "schema_name": "ColumnSpecsBase",
                            "schema_type": "Iced"
                        },
                        "name": "training_r2",
                        "type": "double",
                        "format": "%.5f",
                        "description": "Training r2"
                    }
                ],
                "rowcount": 1,
                "data": [
                    [
                        ""
                    ],
                    [
                        "2021-11-19 13:12:18"
                    ],
                    [
                        " 0.000 sec"
                    ],
                    [
                        0
                    ],
                    [
                        8873662.72730856
                    ],
                    [
                        1187.1120705429513
                    ],
                    [
                        10.578045694410048
                    ],
                    [
                        111.89505071302695
                    ],
                    [
                        7.232481248316213
                    ],
                    [
                        0.9057417968410254
                    ]
                ]
            },
            "reproducibility_information_table": [
                {
                    "__meta": {
                        "schema_version": 3,
                        "schema_name": "TwoDimTableV3",
                        "schema_type": "TwoDimTable"
                    },
                    "name": "Node Information",
                    "description": "",
                    "columns": [
                        {
                            "__meta": {
                                "schema_version": -1,
                                "schema_name": "ColumnSpecsBase",
                                "schema_type": "Iced"
                            },
                            "name": "",
                            "type": "string",
                            "format": "%s",
                            "description": ""
                        },
                        {
                            "__meta": {
                                "schema_version": -1,
                                "schema_name": "ColumnSpecsBase",
                                "schema_type": "Iced"
                            },
                            "name": "node",
                            "type": "int",
                            "format": "%d",
                            "description": "node"
                        },
                        {
                            "__meta": {
                                "schema_version": -1,
                                "schema_name": "ColumnSpecsBase",
                                "schema_type": "Iced"
                            },
                            "name": "h2o",
                            "type": "string",
                            "format": "%s",
                            "description": "h2o"
                        },
                        {
                            "__meta": {
                                "schema_version": -1,
                                "schema_name": "ColumnSpecsBase",
                                "schema_type": "Iced"
                            },
                            "name": "healthy",
                            "type": "string",
                            "format": "%s",
                            "description": "healthy"
                        },
                        {
                            "__meta": {
                                "schema_version": -1,
                                "schema_name": "ColumnSpecsBase",
                                "schema_type": "Iced"
                            },
                            "name": "last_ping",
                            "type": "string",
                            "format": "%s",
                            "description": "last_ping"
                        },
                        {
                            "__meta": {
                                "schema_version": -1,
                                "schema_name": "ColumnSpecsBase",
                                "schema_type": "Iced"
                            },
                            "name": "num_cpus",
                            "type": "int",
                            "format": "%d",
                            "description": "num_cpus"
                        },
                        {
                            "__meta": {
                                "schema_version": -1,
                                "schema_name": "ColumnSpecsBase",
                                "schema_type": "Iced"
                            },
                            "name": "sys_load",
                            "type": "double",
                            "format": "%.5f",
                            "description": "sys_load"
                        },
                        {
                            "__meta": {
                                "schema_version": -1,
                                "schema_name": "ColumnSpecsBase",
                                "schema_type": "Iced"
                            },
                            "name": "mem_value_size",
                            "type": "long",
                            "format": "%d",
                            "description": "mem_value_size"
                        },
                        {
                            "__meta": {
                                "schema_version": -1,
                                "schema_name": "ColumnSpecsBase",
                                "schema_type": "Iced"
                            },
                            "name": "free_mem",
                            "type": "long",
                            "format": "%d",
                            "description": "free_mem"
                        },
                        {
                            "__meta": {
                                "schema_version": -1,
                                "schema_name": "ColumnSpecsBase",
                                "schema_type": "Iced"
                            },
                            "name": "pojo_mem",
                            "type": "long",
                            "format": "%d",
                            "description": "pojo_mem"
                        },
                        {
                            "__meta": {
                                "schema_version": -1,
                                "schema_name": "ColumnSpecsBase",
                                "schema_type": "Iced"
                            },
                            "name": "swap_mem",
                            "type": "long",
                            "format": "%d",
                            "description": "swap_mem"
                        },
                        {
                            "__meta": {
                                "schema_version": -1,
                                "schema_name": "ColumnSpecsBase",
                                "schema_type": "Iced"
                            },
                            "name": "free_disc",
                            "type": "long",
                            "format": "%d",
                            "description": "free_disc"
                        },
                        {
                            "__meta": {
                                "schema_version": -1,
                                "schema_name": "ColumnSpecsBase",
                                "schema_type": "Iced"
                            },
                            "name": "max_disc",
                            "type": "long",
                            "format": "%d",
                            "description": "max_disc"
                        },
                        {
                            "__meta": {
                                "schema_version": -1,
                                "schema_name": "ColumnSpecsBase",
                                "schema_type": "Iced"
                            },
                            "name": "pid",
                            "type": "int",
                            "format": "%d",
                            "description": "pid"
                        },
                        {
                            "__meta": {
                                "schema_version": -1,
                                "schema_name": "ColumnSpecsBase",
                                "schema_type": "Iced"
                            },
                            "name": "num_keys",
                            "type": "int",
                            "format": "%d",
                            "description": "num_keys"
                        },
                        {
                            "__meta": {
                                "schema_version": -1,
                                "schema_name": "ColumnSpecsBase",
                                "schema_type": "Iced"
                            },
                            "name": "tcps_active",
                            "type": "string",
                            "format": "%s",
                            "description": "tcps_active"
                        },
                        {
                            "__meta": {
                                "schema_version": -1,
                                "schema_name": "ColumnSpecsBase",
                                "schema_type": "Iced"
                            },
                            "name": "open_fds",
                            "type": "int",
                            "format": "%d",
                            "description": "open_fds"
                        },
                        {
                            "__meta": {
                                "schema_version": -1,
                                "schema_name": "ColumnSpecsBase",
                                "schema_type": "Iced"
                            },
                            "name": "rpcs_active",
                            "type": "string",
                            "format": "%s",
                            "description": "rpcs_active"
                        },
                        {
                            "__meta": {
                                "schema_version": -1,
                                "schema_name": "ColumnSpecsBase",
                                "schema_type": "Iced"
                            },
                            "name": "nthreads",
                            "type": "int",
                            "format": "%d",
                            "description": "nthreads"
                        },
                        {
                            "__meta": {
                                "schema_version": -1,
                                "schema_name": "ColumnSpecsBase",
                                "schema_type": "Iced"
                            },
                            "name": "is_leader",
                            "type": "string",
                            "format": "%s",
                            "description": "is_leader"
                        },
                        {
                            "__meta": {
                                "schema_version": -1,
                                "schema_name": "ColumnSpecsBase",
                                "schema_type": "Iced"
                            },
                            "name": "total_mem",
                            "type": "long",
                            "format": "%d",
                            "description": "total_mem"
                        },
                        {
                            "__meta": {
                                "schema_version": -1,
                                "schema_name": "ColumnSpecsBase",
                                "schema_type": "Iced"
                            },
                            "name": "max_mem",
                            "type": "long",
                            "format": "%d",
                            "description": "max_mem"
                        },
                        {
                            "__meta": {
                                "schema_version": -1,
                                "schema_name": "ColumnSpecsBase",
                                "schema_type": "Iced"
                            },
                            "name": "java_version",
                            "type": "string",
                            "format": "%s",
                            "description": "java_version"
                        },
                        {
                            "__meta": {
                                "schema_version": -1,
                                "schema_name": "ColumnSpecsBase",
                                "schema_type": "Iced"
                            },
                            "name": "jvm_launch_parameters",
                            "type": "string",
                            "format": "%s",
                            "description": "jvm_launch_parameters"
                        },
                        {
                            "__meta": {
                                "schema_version": -1,
                                "schema_name": "ColumnSpecsBase",
                                "schema_type": "Iced"
                            },
                            "name": "os_version",
                            "type": "string",
                            "format": "%s",
                            "description": "os_version"
                        },
                        {
                            "__meta": {
                                "schema_version": -1,
                                "schema_name": "ColumnSpecsBase",
                                "schema_type": "Iced"
                            },
                            "name": "machine_physical_mem",
                            "type": "long",
                            "format": "%d",
                            "description": "machine_physical_mem"
                        },
                        {
                            "__meta": {
                                "schema_version": -1,
                                "schema_name": "ColumnSpecsBase",
                                "schema_type": "Iced"
                            },
                            "name": "machine_locale",
                            "type": "string",
                            "format": "%s",
                            "description": "machine_locale"
                        }
                    ],
                    "rowcount": 1,
                    "data": [
                        [
                            ""
                        ],
                        [
                            0
                        ],
                        [
                            "127.0.0.1:54321"
                        ],
                        [
                            "true"
                        ],
                        [
                            "1637323934623"
                        ],
                        [
                            24
                        ],
                        [
                            0.15560198
                        ],
                        [
                            182489
                        ],
                        [
                            25748649767
                        ],
                        [
                            0
                        ],
                        [
                            0
                        ],
                        [
                            420211589120
                        ],
                        [
                            999557169152
                        ],
                        [
                            34584
                        ],
                        [
                            8130
                        ],
                        [
                            "\u0000"
                        ],
                        [
                            -1
                        ],
                        [
                            "\u0000"
                        ],
                        [
                            24
                        ],
                        [
                            "true"
                        ],
                        [
                            1610612736
                        ],
                        [
                            25748832256
                        ],
                        [
                            "Java 11.0.9.1 (from AdoptOpenJDK)"
                        ],
                        [
                            "[-ea]"
                        ],
                        [
                            "Windows 10 10.0 (amd64)"
                        ],
                        [
                            102988681216
                        ],
                        [
                            "es_ES"
                        ]
                    ]
                },
                {
                    "__meta": {
                        "schema_version": 3,
                        "schema_name": "TwoDimTableV3",
                        "schema_type": "TwoDimTable"
                    },
                    "name": "Cluster Configuration",
                    "description": "",
                    "columns": [
                        {
                            "__meta": {
                                "schema_version": -1,
                                "schema_name": "ColumnSpecsBase",
                                "schema_type": "Iced"
                            },
                            "name": "",
                            "type": "string",
                            "format": "%s",
                            "description": ""
                        },
                        {
                            "__meta": {
                                "schema_version": -1,
                                "schema_name": "ColumnSpecsBase",
                                "schema_type": "Iced"
                            },
                            "name": "h2o_cluster_uptime",
                            "type": "long",
                            "format": "%d",
                            "description": "H2O cluster uptime"
                        },
                        {
                            "__meta": {
                                "schema_version": -1,
                                "schema_name": "ColumnSpecsBase",
                                "schema_type": "Iced"
                            },
                            "name": "h2o_cluster_timezone",
                            "type": "string",
                            "format": "%s",
                            "description": "H2O cluster timezone"
                        },
                        {
                            "__meta": {
                                "schema_version": -1,
                                "schema_name": "ColumnSpecsBase",
                                "schema_type": "Iced"
                            },
                            "name": "h2o_data_parsing_timezone",
                            "type": "string",
                            "format": "%s",
                            "description": "H2O data parsing timezone"
                        },
                        {
                            "__meta": {
                                "schema_version": -1,
                                "schema_name": "ColumnSpecsBase",
                                "schema_type": "Iced"
                            },
                            "name": "h2o_cluster_version",
                            "type": "string",
                            "format": "%s",
                            "description": "H2O cluster version"
                        },
                        {
                            "__meta": {
                                "schema_version": -1,
                                "schema_name": "ColumnSpecsBase",
                                "schema_type": "Iced"
                            },
                            "name": "h2o_cluster_version_age",
                            "type": "string",
                            "format": "%s",
                            "description": "H2O cluster version age"
                        },
                        {
                            "__meta": {
                                "schema_version": -1,
                                "schema_name": "ColumnSpecsBase",
                                "schema_type": "Iced"
                            },
                            "name": "h2o_cluster_name",
                            "type": "string",
                            "format": "%s",
                            "description": "H2O cluster name"
                        },
                        {
                            "__meta": {
                                "schema_version": -1,
                                "schema_name": "ColumnSpecsBase",
                                "schema_type": "Iced"
                            },
                            "name": "h2o_cluster_total_nodes",
                            "type": "int",
                            "format": "%d",
                            "description": "H2O cluster total nodes"
                        },
                        {
                            "__meta": {
                                "schema_version": -1,
                                "schema_name": "ColumnSpecsBase",
                                "schema_type": "Iced"
                            },
                            "name": "h2o_cluster_free_memory",
                            "type": "long",
                            "format": "%d",
                            "description": "H2O cluster free memory"
                        },
                        {
                            "__meta": {
                                "schema_version": -1,
                                "schema_name": "ColumnSpecsBase",
                                "schema_type": "Iced"
                            },
                            "name": "h2o_cluster_total_cores",
                            "type": "int",
                            "format": "%d",
                            "description": "H2O cluster total cores"
                        },
                        {
                            "__meta": {
                                "schema_version": -1,
                                "schema_name": "ColumnSpecsBase",
                                "schema_type": "Iced"
                            },
                            "name": "h2o_cluster_allowed_cores",
                            "type": "int",
                            "format": "%d",
                            "description": "H2O cluster allowed cores"
                        },
                        {
                            "__meta": {
                                "schema_version": -1,
                                "schema_name": "ColumnSpecsBase",
                                "schema_type": "Iced"
                            },
                            "name": "h2o_cluster_status",
                            "type": "string",
                            "format": "%s",
                            "description": "H2O cluster status"
                        },
                        {
                            "__meta": {
                                "schema_version": -1,
                                "schema_name": "ColumnSpecsBase",
                                "schema_type": "Iced"
                            },
                            "name": "h2o_internal_security",
                            "type": "string",
                            "format": "%s",
                            "description": "H2O internal security"
                        },
                        {
                            "__meta": {
                                "schema_version": -1,
                                "schema_name": "ColumnSpecsBase",
                                "schema_type": "Iced"
                            },
                            "name": "h2o_api_extensions",
                            "type": "string",
                            "format": "%s",
                            "description": "H2O API Extensions"
                        }
                    ],
                    "rowcount": 1,
                    "data": [
                        [
                            ""
                        ],
                        [
                            17645
                        ],
                        [
                            "Europe/Paris"
                        ],
                        [
                            "UTC"
                        ],
                        [
                            "3.32.0.4"
                        ],
                        [
                            "9 months and 17 days"
                        ],
                        [
                            "H2O_from_python_spokk_y01brs"
                        ],
                        [
                            1
                        ],
                        [
                            25748649767
                        ],
                        [
                            24
                        ],
                        [
                            24
                        ],
                        [
                            "locked, healthly"
                        ],
                        [
                            "'false'"
                        ],
                        [
                            "Amazon S3, Algos, AutoML, Core V3, TargetEncoder, Core V4"
                        ]
                    ]
                },
                {
                    "__meta": {
                        "schema_version": 3,
                        "schema_name": "TwoDimTableV3",
                        "schema_type": "TwoDimTable"
                    },
                    "name": "Input Frames Information",
                    "description": "",
                    "columns": [
                        {
                            "__meta": {
                                "schema_version": -1,
                                "schema_name": "ColumnSpecsBase",
                                "schema_type": "Iced"
                            },
                            "name": "",
                            "type": "string",
                            "format": "%s",
                            "description": ""
                        },
                        {
                            "__meta": {
                                "schema_version": -1,
                                "schema_name": "ColumnSpecsBase",
                                "schema_type": "Iced"
                            },
                            "name": "input_frame",
                            "type": "string",
                            "format": "%s",
                            "description": "Input Frame"
                        },
                        {
                            "__meta": {
                                "schema_version": -1,
                                "schema_name": "ColumnSpecsBase",
                                "schema_type": "Iced"
                            },
                            "name": "checksum",
                            "type": "long",
                            "format": "%d",
                            "description": "Checksum"
                        },
                        {
                            "__meta": {
                                "schema_version": -1,
                                "schema_name": "ColumnSpecsBase",
                                "schema_type": "Iced"
                            },
                            "name": "espc",
                            "type": "string",
                            "format": "%d",
                            "description": "ESPC"
                        }
                    ],
                    "rowcount": 2,
                    "data": [
                        [
                            "",
                            ""
                        ],
                        [
                            "training_frame",
                            "validation_frame"
                        ],
                        [
                            7794876669987372548,
                            -1
                        ],
                        [
                            "[0, 83, 162, 235, 310, 382, 460, 542, 616, 697, 773, 851, 934, 1010, 1092, 1171, 1251, 1327, 1403, 1480, 1559, 1638, 1712, 1793, 1868, 1948, 2025, 2102, 2184, 2255, 2330, 2411, 2486, 2562, 2640, 2719, 2802, 2883, 2964, 3039, 3115, 3198, 3273, 3353, 3429, 3506, 3588, 3665, 3746, 3816, 3896, 3979, 4053, 4127, 4201, 4281, 4360, 4435, 4503, 4583, 4663, 4744, 4821, 4890, 4977, 5059, 5141, 5212, 5289, 5370, 5454, 5528, 5608, 5688, 5767, 5847, 5916, 5996, 6078, 6159, 6234, 6305, 6390, 6464, 6538, 6613, 6684, 6765, 6840, 6925, 7001, 7078, 7161, 7242, 7323, 7402, 7475]",
                            "-1"
                        ]
                    ]
                }
            ],
            "training_metrics": {
                "__meta": {
                    "schema_version": 3,
                    "schema_name": "ModelMetricsRegressionGLMV3",
                    "schema_type": "ModelMetricsRegressionGLM"
                },
                "model": {
                    "__meta": {
                        "schema_version": 3,
                        "schema_name": "ModelKeyV3",
                        "schema_type": "Key<Model>"
                    },
                    "name": "GLM_V1.20211119",
                    "type": "Key<Model>",
                    "URL": "/3/Models/GLM_V1.20211119"
                },
                "model_checksum": 4448256697603718912,
                "frame": {
                    "__meta": {
                        "schema_version": 3,
                        "schema_name": "FrameKeyV3",
                        "schema_type": "Key<Frame>"
                    },
                    "name": "py_6_sid_803f",
                    "type": "Key<Frame>",
                    "URL": "/3/Frames/py_6_sid_803f"
                },
                "frame_checksum": -1867110722434159464,
                "description": null,
                "model_category": "Regression",
                "scoring_time": 1637323938224,
                "predictions": null,
                "MSE": mse,
                "RMSE": rmse,
                "nobs": nobs,
                "custom_metric_name": null,
                "custom_metric_value": 0.0,
                "r2": 0.9057417968410254,
                "mean_residual_deviance": 111.89505071302695,
                "mae": 7.232481248316213,
                "rmsle": 0.015670509048229338,
                "residual_deviance": 836415.5040798765,
                "null_deviance": 8873662.726938863,
                "AIC": 56486.90242269113,
                "null_degrees_of_freedom": 7474,
                "residual_degrees_of_freedom": 7471
            },
            "validation_metrics": null,
            "cross_validation_metrics": {
                "__meta": {
                    "schema_version": 3,
                    "schema_name": "ModelMetricsRegressionGLMV3",
                    "schema_type": "ModelMetricsRegressionGLM"
                },
                "model": {
                    "__meta": {
                        "schema_version": 3,
                        "schema_name": "ModelKeyV3",
                        "schema_type": "Key<Model>"
                    },
                    "name": "GLM_V1.20211119",
                    "type": "Key<Model>",
                    "URL": "/3/Models/GLM_V1.20211119"
                },
                "model_checksum": 4448256697603718912,
                "frame": {
                    "__meta": {
                        "schema_version": 3,
                        "schema_name": "FrameKeyV3",
                        "schema_type": "Key<Frame>"
                    },
                    "name": "py_6_sid_803f",
                    "type": "Key<Frame>",
                    "URL": "/3/Frames/py_6_sid_803f"
                },
                "frame_checksum": -1867110722434159464,
                "description": "2-fold cross-validation on training data (Metrics computed for combined holdout predictions)",
                "model_category": "Regression",
                "scoring_time": 1637323938229,
                "predictions": null,
                "MSE": 137.29634505748047,
                "RMSE": 11.717352305767735,
                "nobs": 7475,
                "custom_metric_name": null,
                "custom_metric_value": 0.0,
                "r2": 0.8843442430836354,
                "mean_residual_deviance": 137.29634505748047,
                "mae": 8.317474074051735,
                "rmsle": 0.01741955162201051,
                "residual_deviance": 1026290.1793046666,
                "null_deviance": 8874260.619064769,
                "AIC": 58016.14022134703,
                "null_degrees_of_freedom": 7474,
                "residual_degrees_of_freedom": 7471
            },
            "cross_validation_metrics_summary": {
                "__meta": {
                    "schema_version": 3,
                    "schema_name": "TwoDimTableV3",
                    "schema_type": "TwoDimTable"
                },
                "name": "Cross-Validation Metrics Summary",
                "description": "",
                "columns": [
                    {
                        "__meta": {
                            "schema_version": -1,
                            "schema_name": "ColumnSpecsBase",
                            "schema_type": "Iced"
                        },
                        "name": "",
                        "type": "string",
                        "format": "%s",
                        "description": ""
                    },
                    {
                        "__meta": {
                            "schema_version": -1,
                            "schema_name": "ColumnSpecsBase",
                            "schema_type": "Iced"
                        },
                        "name": "mean",
                        "type": "string",
                        "format": "%s",
                        "description": "mean"
                    },
                    {
                        "__meta": {
                            "schema_version": -1,
                            "schema_name": "ColumnSpecsBase",
                            "schema_type": "Iced"
                        },
                        "type": "string",
                        "format": "%s",
                        "description": "sd"
                    },
                    {
                        "__meta": {
                            "schema_version": -1,
                            "schema_name": "ColumnSpecsBase",
                            "schema_type": "Iced"
                        },
                        "name": "cv_1_valid",
                        "type": "string",
                        "format": "%s",
                        "description": "cv_1_valid"
                    },
                    {
                        "__meta": {
                            "schema_version": -1,
                            "schema_name": "ColumnSpecsBase",
                            "schema_type": "Iced"
                        },
                        "name": "cv_2_valid",
                        "type": "string",
                        "format": "%s",
                        "description": "cv_2_valid"
                    }
                ],
                "rowcount": 8,
                "data": [
                    [
                        "mae",
                        "mean_residual_deviance",
                        "mse",
                        "null_deviance",
                        "r2",
                        "residual_deviance",
                        "rmse",
                        "rmsle"
                    ],
                    [
                        "8.318414",
                        "137.30759",
                        "137.30759",
                        "4437130.5",
                        "0.8843413",
                        "513145.1",
                        "11.717734",
                        "0.017419022"
                    ],
                    [
                        "0.13249846",
                        "1.5853719",
                        "1.5853719",
                        "6586.5366",
                        "4.7759298E-4",
                        "1356.5074",
                        "0.067648396",
                        "6.597634E-5"
                    ],
                    [
                        "8.412105",
                        "138.42862",
                        "138.42862",
                        "4441787.5",
                        "0.884679",
                        "512185.9",
                        "11.76557",
                        "0.017372368"
                    ],
                    [
                        "8.224724",
                        "136.18657",
                        "136.18657",
                        "4432473.0",
                        "0.8840036",
                        "514104.28",
                        "11.6699",
                        "0.017465673"
                    ]
                ]
            },
            "status": null,
            "start_time": 1637323938202,
            "end_time": 1637323938228,
            "run_time": 26,
            "default_threshold": 0.5,
            "help": {
                "domains": "Domains for categorical columns",
                "__meta": "Metadata on this schema instance, to make it self-describing.",
                "standardized_coefficient_magnitudes": "Standardized Coefficient Magnitudes",
                "help": "Help information for output fields",
                "training_metrics": "Training data model metrics",
                "cross_validation_models": "Cross-validation models (model ids)",
                "cross_validation_predictions": "Cross-validation predictions, one per cv model (deprecated, use cross_validation_holdout_predictions_frame_id instead)",
                "lambda_best": "Lambda minimizing the objective value, only applicable with lambda search or when arrays of alpha and lambdas are provided",
                "cross_validation_metrics_summary": "Cross-validation model metrics summary",
                "status": "Job status",
                "reproducibility_information_table": "Model reproducibility information",
                "variable_importances": "Variable Importances",
                "model_summary": "Model summary",
                "end_time": "End time in milliseconds",
                "names": "Column names",
                "cross_validation_fold_assignment_frame_id": "Cross-validation fold assignment (each row is assigned to one holdout fold)",
                "lambda_1se": "Lambda best + 1 standard error. Only applicable with lambda search and cross-validation",
                "run_time": "Runtime in milliseconds",
                "lambda_max": "Starting lambda value used when lambda search is enabled.",
                "default_threshold": "Default threshold used for predictions",
                "scoring_history": "Scoring history",
                "dispersion": "Dispersion parameter, only applicable to Tweedie family (input/output) and fractional Binomial (output only)",
                "cross_validation_holdout_predictions_frame_id": "Cross-validation holdout predictions (full out-of-sample predictions on training data)",
                "alpha_best": "Alpha minimizing the objective value, only applicable when arrays of alphas are given ",
                "lambda_min": "Minimum lambda value calculated that may be used for lambda search.  Early-stop may happen and the minimum lambda value will not be used in this case.",
                "coefficients_table_multinomials_with_class_names": "Table of Coefficients with coefficients denoted with class names for GLM multinonimals only.",
                "model_category": "Category of the model (e.g., Binomial)",
                "random_coefficients_table": "Table of Random Coefficients for HGLM",
                "best_submodel_index": "submodel index minimizing the objective value, only applicable for arrays of alphas/lambda ",
                "column_types": "Column types",
                "original_names": "Original column names",
                "start_time": "Start time in milliseconds",
                "cross_validation_metrics": "Cross-validation model metrics",
                "validation_metrics": "Validation data model metrics",
                "coefficients_table": "Table of Coefficients"
            },
            "coefficients_table": {
                "__meta": {
                    "schema_version": 3,
                    "schema_name": "TwoDimTableV3",
                    "schema_type": "TwoDimTable"
                },
                "name": "Coefficients",
                "description": "glm coefficients",
                "columns": [
                    {
                        "__meta": {
                            "schema_version": -1,
                            "schema_name": "ColumnSpecsBase",
                            "schema_type": "Iced"
                        },
                        "name": "names",
                        "type": "string",
                        "format": "%s",
                        "description": "names"
                    },
                    {
                        "__meta": {
                            "schema_version": -1,
                            "schema_name": "ColumnSpecsBase",
                            "schema_type": "Iced"
                        },
                        "name": "coefficients",
                        "type": "double",
                        "format": "%5f",
                        "description": "Coefficients"
                    },
                    {
                        "__meta": {
                            "schema_version": -1,
                            "schema_name": "ColumnSpecsBase",
                            "schema_type": "Iced"
                        },
                        "name": "standardized_coefficients",
                        "type": "double",
                        "format": "%5f",
                        "description": "Standardized Coefficients"
                    }
                ],
                "rowcount": 4,
                "data": [
                    [
                        "Intercept",
                        "Time_Desv",
                        "Time_Max",
                        "Time_Min"
                    ],
                    [
                        144.0756814030426,
                        -0.27299846015826909,
                        0.605213488663403,
                        0.17119864143545317
                    ],
                    [
                        679.5701435067558,
                        -2.1084513009011749,
                        26.525762047909219,
                        7.759544618071356
                    ]
                ]
            },
            "random_coefficients_table": null,
            "coefficients_table_multinomials_with_class_names": null,
            "standardized_coefficient_magnitudes": {
                "__meta": {
                    "schema_version": 3,
                    "schema_name": "TwoDimTableV3",
                    "schema_type": "TwoDimTable"
                },
                "name": "Standardized Coefficient Magnitudes",
                "description": "standardized coefficient magnitudes",
                "columns": [
                    {
                        "__meta": {
                            "schema_version": -1,
                            "schema_name": "ColumnSpecsBase",
                            "schema_type": "Iced"
                        },
                        "name": "names",
                        "type": "string",
                        "format": "%s",
                        "description": "names"
                    },
                    {
                        "__meta": {
                            "schema_version": -1,
                            "schema_name": "ColumnSpecsBase",
                            "schema_type": "Iced"
                        },
                        "name": "coefficients",
                        "type": "double",
                        "format": "%5f",
                        "description": "Coefficients"
                    },
                    {
                        "__meta": {
                            "schema_version": -1,
                            "schema_name": "ColumnSpecsBase",
                            "schema_type": "Iced"
                        },
                        "name": "sign",
                        "type": "string",
                        "format": "%s",
                        "description": "Sign"
                    }
                ],
                "rowcount": 3,
                "data": [
                    [
                        "Time_Max",
                        "Time_Min",
                        "Time_Desv"
                    ],
                    [
                        26.5257625579834,
                        7.759544849395752,
                        2.1084513664245607
                    ],
                    [
                        "POS",
                        "POS",
                        "NEG"
                    ]
                ]
            },
            "variable_importances": {
                "__meta": {
                    "schema_version": 3,
                    "schema_name": "TwoDimTableV3",
                    "schema_type": "TwoDimTable"
                },
                "name": "Variable Importances",
                "description": "",
                "columns": [
                    {
                        "__meta": {
                            "schema_version": -1,
                            "schema_name": "ColumnSpecsBase",
                            "schema_type": "Iced"
                        },
                        "name": "variable",
                        "type": "string",
                        "format": "%s",
                        "description": "Variable"
                    },
                    {
                        "__meta": {
                            "schema_version": -1,
                            "schema_name": "ColumnSpecsBase",
                            "schema_type": "Iced"
                        },
                        "name": "relative_importance",
                        "type": "double",
                        "format": "%5f",
                        "description": "Relative Importance"
                    },
                    {
                        "__meta": {
                            "schema_version": -1,
                            "schema_name": "ColumnSpecsBase",
                            "schema_type": "Iced"
                        },
                        "name": "scaled_importance",
                        "type": "double",
                        "format": "%5f",
                        "description": "Scaled Importance"
                    },
                    {
                        "__meta": {
                            "schema_version": -1,
                            "schema_name": "ColumnSpecsBase",
                            "schema_type": "Iced"
                        },
                        "name": "percentage",
                        "type": "double",
                        "format": "%5f",
                        "description": "Percentage"
                    }
                ],
                "rowcount": 3,
                "data": [
                    [
                        "Time_Max",
                        "Time_Min",
                        "Time_Desv"
                    ],
                    [
                        26.5257625579834,
                        7.759544849395752,
                        2.1084513664245607
                    ],
                    [
                        1.0,
                        0.29252862504645996,
                        0.0794869275413153
                    ],
                    [
                        0.7288547116786598,
                        0.21321086666599235,
                        0.05793442165534788
                    ]
                ]
            },
            "lambda_best": 0.12740979773555953,
            "alpha_best": 0.25,
            "best_submodel_index": 0,
            "lambda_1se": 0.12740979773555953,
            "lambda_min": -1.0,
            "lambda_max": -1.0,
            "dispersion": 0.0
        },
        "compatible_frames": null,
        "typealgorithm": "Sklearn_Clustering_Ward",
        "typenamedata" : fileNameData.split('/')[2]

    }

    #Save json file
    with open(os.path.join(pathWorkSpace, params['modelName'] + '_informationColumnsStringToNumber.json'),"w") as fp:
            json.dump(nameColumnsObject, fp)



    with open(os.getcwd() + "/imagesup/" + params['userName'] +'/' + params['modelName'] + '/' + params['modelName'] + '.json', 'w') as  json_file:
        json.dump(data,  json_file)

    # New directory for save JSON file with information about columns used for create model
    os.mkdir(pathWorkSpace + '/experimental')

    # Directory of the files to be compressed for model manager
    images_directory_shap = pathWorkSpace + 'shap_plots'
    images_directory_root = pathWorkSpace
    csv_directory_pred_single = pathWorkSpace + params['modelName'] + "_data_regresion_prediction_single.csv"
    csv_directory_pred_complete = pathWorkSpace + params['modelName'] + "_data_regresion_prediction_complete.csv"
    csv_directory_pred_ward = pathWorkSpace + params['modelName'] + "_data_regresion_prediction_ward.csv"
    csv_directory_pred_average = pathWorkSpace + params['modelName'] + "_data_regresion_prediction_average.csv"

    csv_directory_data = pathWorkSpace + params['modelName'] + "_dataFinal.csv"
    csv_directory_metrics = pathWorkSpace + params['modelName'] + "_data_regresion_prediction.csv"
    json_file = os.path.join(pathWorkSpace, params['modelName'] + '_informationColumnsStringToNumber.json')
    pkl_file_single = pathWorkSpace + modelName + "_single_model.pkl"
    pkl_file_complete = pathWorkSpace + modelName + "_complete_model.pkl"
    pkl_file_average = pathWorkSpace + modelName + "_average_model.pkl"
    pkl_file_ward = pathWorkSpace + modelName + "_ward_model.pkl"

    zip_file = pathWorkSpace + modelName + '.zip'

    with zipfile.ZipFile(zip_file, 'w') as zipf:
        # Add image files to the "shapplots" folder
        shap_images = glob.glob(os.path.join(images_directory_shap, '*.png'))  
        for image in shap_images:
            zipf.write(image, arcname=os.path.join('images', os.path.basename(image)))

        # Add image files to the "image" folder
        images = glob.glob(os.path.join(images_directory_root, '*.png')) 
        for image in images:
            zipf.write(image, arcname=os.path.join('images', os.path.basename(image)))

         # Add CSV files to the "data" folder
        # csv_files = glob.glob(os.path.join(csv_directory, '*.csv'))  
        # for csv in csv_files:
        if os.path.exists(csv_directory_pred_single):
            zipf.write(csv_directory_pred_single, arcname=os.path.join('data', os.path.basename(pathWorkSpace + params['modelName'] + "_model_predictions.csv")))

        if os.path.exists(csv_directory_pred_complete):    
            zipf.write(csv_directory_pred_complete, arcname=os.path.join('data', os.path.basename(pathWorkSpace + params['modelName'] + "_model_predictions.csv")))

        if os.path.exists(csv_directory_pred_ward):    
            zipf.write(csv_directory_pred_ward, arcname=os.path.join('data', os.path.basename(pathWorkSpace + params['modelName'] + "_model_predictions.csv")))

        if os.path.exists(csv_directory_pred_average):    
            zipf.write(csv_directory_pred_average, arcname=os.path.join('data', os.path.basename(pathWorkSpace + params['modelName'] + "_model_predictions.csv")))


        zipf.write(csv_directory_data, arcname=os.path.join('data', os.path.basename(pathWorkSpace + params['modelName'] + "_model_data.csv")))
        # zipf.write(csv_directory_metrics, arcname=os.path.join('data', os.path.basename(csv)))


        # Add JSON file to the root of the compressed file
        zipf.write(json_file, arcname=os.path.basename(os.path.join(pathWorkSpace, params['modelName'] + '_information.json')))

        # Add PKL file to the root of the compressed file
        if os.path.exists(pkl_file_single):
            zipf.write(pkl_file_single, arcname=os.path.basename(pathWorkSpace + params['modelName'] + "_model.pkl"))

        if os.path.exists(pkl_file_complete):
            zipf.write(pkl_file_complete, arcname=os.path.basename(pathWorkSpace + params['modelName'] + "_model.pkl"))

        if os.path.exists(pkl_file_average):
            zipf.write(pkl_file_average, arcname=os.path.basename(pathWorkSpace + params['modelName'] + "_model.pkl"))

        if os.path.exists(pkl_file_ward):
            zipf.write(pkl_file_ward, arcname=os.path.basename(pathWorkSpace + params['modelName'] + "_model.pkl"))

    
    # Move file because the this file works bad for load models in mymodels
    shutil.move(pathWorkSpace + modelName + '_informationColumnsStringToNumber.json', pathWorkSpace + 'experimental/' + modelName + '_informationColumnsStringToNumber.json' )


    shutil.make_archive(pathWorkSpace + modelName, "tar", pathWorkSpace)

    loadingJSON(userName, 90, 'Completing final operations', False)

    time.sleep(2.0);
    loadingJSON(params['userName'], 0, 'Awaiting data, please be patient', False)

    # Delete dataset
    os.remove(fileNameData)


'''
fuction for bar loading in frond-end 
'''
def loadingJSON(user, time, state, error):

    pathFileJSONLoadingPath = os.getcwd() + "/settings/" + user +'/'
    nameFileJSON = 'loading.json' 
    fileJSONLoadingPath = pathFileJSONLoadingPath + nameFileJSON
    data = {}

    with open(os.path.join(pathFileJSONLoadingPath, nameFileJSON),"w") as fp:
        json.dump(data, fp)

    with open(fileJSONLoadingPath) as json_file:
        json_decoded = json.load(json_file)

    json_decoded['progress'] = time
    json_decoded['state'] = str(state)
    json_decoded['error'] = error


    with open(fileJSONLoadingPath, 'w') as  json_file:
        json.dump(json_decoded,  json_file)

'''
Fucntion for draw the skatter Plot
'''
def generateSkatterPlot(numberCluster, dataCluster, classColumn, typeWard, label_encoder, cancer, pathWorkSpace, params,affinity_p,compute_full_tree_p,compute_distances_p):

    from sklearn.cluster import AgglomerativeClustering
    # Obtener el número de muestras y características de tus datos
    n_samples, n_features = dataCluster.shape  # Ajusta X con tus datos

    # Asegurarse de que n_components esté dentro del rango válido
    n_components = min(n_samples, n_features)  # O ajustar a un valor más adecuado según tus necesidades

    #delete class column for no add to predict we need predict this column
    data = dataCluster.drop(classColumn,axis=1)

    print('++++++++++++++++++++++++++++++ components: ')
    print(n_components)
    ###########################################
    preprocessor = Pipeline(
            [
                ("scaler", MinMaxScaler()),
                ("pca", PCA(n_components=2, random_state=42)), #PCA(n_components=2, random_state=42)),
            ]
        )

    clusterer = Pipeline(
    [
        (
            "ward", 
            AgglomerativeClustering(
                    n_clusters=numberCluster, 
                    linkage=typeWard,
                    affinity=affinity_p,
                    compute_full_tree=compute_full_tree_p,
                    compute_distances=compute_distances_p)
        ),
    ]
    )



    pipe = Pipeline(
        [
            ("preprocessor", preprocessor),
            ("clusterer", clusterer)
        ]
    )

    pipe.fit(data)

    preprocessed_data = pipe["preprocessor"].transform(data)
    
    print('++++++++++++++++++++++++++++++++++++++++++++++++++++++++')
    print(preprocessed_data)

    predicted_labels = pipe["clusterer"]["ward"].labels_

    silhouette_score(preprocessed_data, predicted_labels)

    pcadf = pd.DataFrame(
        pipe["preprocessor"].transform(data),
        columns=["X", "Y"],
    )

    print(dataCluster[classColumn])

    pcadf["predicted_cluster"] = pipe["clusterer"]["ward"].labels_
    pcadf["true_label"] = label_encoder.inverse_transform(label_encoder.fit_transform(dataCluster[classColumn]))

    #Change prediction true and of class int to name for leyends in graphics
    for idx,ty in enumerate(cancer):
        pcadf['predicted_cluster'][pcadf['predicted_cluster'] == idx] = ty
        pcadf['true_label'][pcadf['true_label'] == idx] = ty

    for i in range(0, 100):    
        print(pcadf['predicted_cluster'][i], type(pcadf['predicted_cluster'][i]))
        print(pcadf['true_label'][i], type(pcadf['true_label'][i]))


    pcadf['predicted_cluster'] = pcadf['predicted_cluster'].astype(str)
    pcadf['true_label']= pcadf['true_label'].astype(str)

    # Removes outliers from column X and Y
    pcadf = remove_outliers(pcadf, 'X')   
    pcadf = remove_outliers(pcadf, 'Y')   

    #Transform dataframe pandas to dataframe h2o for export to file csv
    pcadf.to_csv(pathWorkSpace + params['modelName'] + "_data_regresion_prediction_" + typeWard + ".csv", index=False, sep=',', float_format=float_formatter)

    # Comparar las dos columnas y calcular el porcentaje de aciertos
    hits = (pcadf['predicted_cluster'] == pcadf['true_label']).sum()
    total = len(pcadf)
    hits_percentage = (hits / total) * 100

    print(f"Model: {hits_percentage}:%" )
   

    pickle.dump(clusterer, open(pathWorkSpace + params['modelName'] + "_" + typeWard + "_model.pkl", "wb"))
    # prediction = clusterer.fit_predict(raw_data[1:10])

    # print (type + ': ' ,prediction)

    from matplotlib.lines import Line2D

    # custom = []
    # colors = ['b','r','g','y','m','c']

    # for clus in range(len(label_encoder.classes_)):
    #     custom.append(Line2D([], [], marker='.', color=colors[clus], linestyle='None'))


    plt.figure(figsize=(30, 10))

    for idx, column in enumerate(pcadf):
        print(column)

    filled_markers = ('o', 'v', '^', '<', '>', '8', 's', 'p', '*', 'h', 'H', 'D', 'd', 'P', 'X')

    scat = sns.scatterplot(
        "X",
        "Y",
        s=50,
        data=pcadf,
        hue="predicted_cluster",
        style="true_label",
        palette="Set2",
        markers=filled_markers,
    )

    scat.set_title(
        "Clustering results Pan-Cancer "
    )
    plt.legend(bbox_to_anchor=(1.05, 1), loc=2, borderaxespad=0.0)
    plt.savefig(pathWorkSpace + params['modelName'] + "_Clustering_results_Pan-Cancer_" + typeWard  + ".png")

    return pipe, hits_percentage


def metrics (pipe, dataset,pathWorkSpace,params,typeWard):

    df = pd.read_csv(pathWorkSpace + params['modelName'] + "_data_regresion_prediction_" + typeWard + ".csv")
     # Obtain the coordinates of the centroids and labels
    labelsPipe =  pipe["clusterer"]["ward"].labels_
    centroids = pd.DataFrame(dataset).groupby(labelsPipe).mean().values
    # Calculate MSE and RMSE of the clusters
    metr = [];
    mse = mean_squared_error(dataset, centroids[labelsPipe])
    rmse = np.sqrt(mse)
    # Calculate the NOBS
    nobs = dataset.shape[0]

    metr.append(mse)
    metr.append(rmse)
    metr.append(nobs)

    return metr


'''
This code defines a remove_outliers function that takes a DataFrame and the name of a numeric column as arguments. 
The function calculates the mean and the standard deviation of the data in this column and uses these values to define 
the limits for the normal values. It then selects the rows that have values outside these limits and removes them from the original DataFrame. 
removed from the original DataFrame.

'''
def remove_outliers(df, column):
    # Calculates the mean and standard deviation of the column
    mean = df[column].mean()
    std = df[column].std()
    
    # Defines limits for normal values
    lower_bound = mean - 2 * std
    upper_bound = mean + 2 * std
    
    # Selects rows that have out-of-range values in the column
    outliers = df.loc[(df[column] < lower_bound) | (df[column] > upper_bound)]
    
    # Removes selected rows from the original DataFrame
    df = df.drop(outliers.index)
    
    return df


# Defines a format function to display numbers in their full decimal form.
def float_formatter(x):
    return f'{x:.15f}'

'''
This method takes an array as input and creates a new dictionary object. It then iterates through each element in the array and 
uses a regular expression to extract the first word and the second word separated by an underscore. If the extracted words match 
the pattern "([A-Za-z]+)snomed([0-9]+)", the method creates a new key-value pair in the dictionary object with the extracted 
first word as the key and the extracted second word as the value. Finally, the method returns the dictionary object.
'''
def generate_object(array):
    object = {}
    
    for elem in array:
        match = re.match(r'([A-Za-z_0-9]+)_SNOMED_([0-9]+)', elem)
        if match:
            key = match.group(1)
            value = match.group(2)
            #value = int(match.group(2))
            object[key] = value
    
    return object


######################################

if __name__ == "__main__":
    if (len(sys.argv) > 1):
        dataSysArgv = {'modelName'                  : sys.argv[1],
                       'userName'                   : sys.argv[2],
                       'fileNameData'               : "../uploads/" + sys.argv[3],
                       'clusterK'                   : sys.argv[4],
                       'choseDeleteColumnsString'   : sys.argv[5],
                       'classColumn'                : sys.argv[6],
                       'columnsPredict'             : sys.argv[7],
                       'nameClases'                 : sys.argv[8],
                       'affinity'                   : sys.argv[9],
                       'compute_full_tree'          : sys.argv[10],
                       'compute_distances'          : sys.argv[11],
                       'link'                       : sys.argv[12],
                       'valuesNan'                  : sys.argv[13],
                       'advancedModel'              : sys.argv[14]

                       }
                   
        print("Using parameters from SysArgV {} )".format(dataSysArgv)) 
        main(dataSysArgv)

    else:
        print("\n\n\nThis script requires some arguments introduced by the command line" + \
              "\n [modelName, userName, pathClasses, ]\n\n")
        
        # values for testing
        PARAMS = {'modelName'                 : 'Ward.20221025',
                  'userName'                  : 'pepe45',
                  'fileNameData'              : "../uploads/pancreatic-cancer.csv" ,
                  'clusterK'                  : '3',
                  'choseDeleteColumnsString'  : 'sample_id,patient_cohort,stage,benign_sample_diagnosis',
                  'classColumn'               : 'diagnosis',
                  'columnsPredict'            : 'creatinine,LYVE1,REG1B,TFF1,REG1A',
                  'nameClases'                : 'no_pancreatic_disease,benign_hepatobilliary_disease,pancreatic_ductal_adenocarcinoma',
                  'affinity'                  : 'euclidean', 
                  'compute_full_tree'         : 'auto',
                  'compute_distances'         : 'False',
                  'link'                      : 'ward',
                  'valuesNan'                 : 'mean',

                      
                       }
        main(PARAMS)
