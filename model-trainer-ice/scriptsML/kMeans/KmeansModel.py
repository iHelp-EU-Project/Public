# -*- coding: utf-8 -*-
'''
@version 26.07.19
@author Armando Aguayo Mendoza, armando.aguayo@informationcatalyst.com
@author Jose Gil Lopez, jose.gil@informationcatalyst.com
'''
import glob
import h2o
from h2o.estimators.kmeans import H2OKMeansEstimator
import sys
import re

# import imp
import os
import matplotlib.pyplot as plt
import math as math
import zipfile
import shutil
import json
import time
import pandas as pd
import seaborn as sns
import copy
import numpy as np
import shap
from sys import platform

from timeit import default_timer as timer
from datetime import timedelta
from datetime import datetime
sys.path.append('../shapPlots')
import shapPlots.shapPlots as shapPlots

#Elbow Curve
def diagnostics_from_clusteringmodel(model):
    total_within_sumofsquares = model.tot_withinss()
    number_of_clusters = len(model.centers())
    number_of_dimensions = len(model.centers()[0])
    number_of_rows = sum(model.size())
    #AIC and BIC are both measures of the relative quality of a statistical model.
    aic = total_within_sumofsquares + 2 * number_of_dimensions * number_of_clusters
    bic = total_within_sumofsquares + math.log(number_of_rows) * number_of_dimensions * number_of_clusters
    
    return {'Clusters':number_of_clusters,
            'Total Within SS':total_within_sumofsquares, 
            'AIC':aic, 
            'BIC':bic}





# Function to know if they are "integers" or "strings" return bool
def have_(numero):
    numero = str(numero)
    primer_indice = numero.find("_")
    if primer_indice is -1:
        return False
    return primer_indice is numero.rfind("_")  


 

def doOtherModel(params):
    
    h2o.init() 
    # Path for save  images and files csv
    imagesPath = os.getcwd() + "/imagesup/" + params['userName'] +'/' + params['modelName'] +'/'
    modelPath  = os.getcwd() + "/imagesup/" + params['userName'] +'/' + params['modelName'] +'/'
    csvPath = os.getcwd() + "/imagesup/" + params['userName'] +'/' + params['modelName'] +'/'
    
    trainDataName = params['trainData']
    print(trainDataName)
    modelName = params['modelName']
    print(modelName)
    columnsIdPrediction = params['columnsIdPrediction']
    print(columnsIdPrediction)
    split = int(params['split']) / 100 # formatting datasets partition type 
    print(split)
    seed = int(params['seed'])
    print(seed)
    ini = params['init']
    print(ini)
    maxi = int(params['max_iteration'])
    print(maxi)
    cluster_k = int(params['cluster_k'])
    print('cluster: ',cluster_k)
    sta_temp = params['standardize']
    print(sta_temp)
    standari = sta_temp[0].upper()=='T'
    print(standari)
    deleteColumns = params['deleteColumns']
    print(deleteColumns)
    class_column = params['class_column']
    print(class_column)
    username_agg  = params['userName']
    print(username_agg)
    valuesClassColumn  = params['valuesClassColumn'].replace(' ', '_')
    print(valuesClassColumn)
    valuesNan = params['valuesNan']
    print('valuesNan:' , valuesNan)
    advancedModel = params['advancedModel']
    print('advancedModel:' , advancedModel)  

    


    #Aggregator
    aggregator_cluster_size_1 = []
    aggregator_cluster_size_2 = []
    aggregator_cluster_size_3 = []
    for i in range(0, cluster_k):
        aggregator_cluster_size_1.append(50)# 
        aggregator_cluster_size_2.append(25)# = '25, 25, 25'.split(',')
        aggregator_cluster_size_3.append(12)# = '12, 12, 12'.split(',')
    
    cancer = valuesClassColumn #['no pancreatic disease', 'benign hepatobilliary disease', 'pancreatic ductal adenocarcinoma' ]

    start = timer()
    end = timer()

    # Getting the current date and time
    dt = datetime.now()

    # getting the timestamp
    ts = datetime.timestamp(dt)

    # JSON of pregress bar in frond-end
    loadingJSON(username_agg, 20, 'Awaiting data, please be patient', False)
    time.sleep(2.0)

    df = pd.read_csv(trainDataName,  na_values = ['no info', '.'])
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
                        'version' : '',
                        'advancedModel': '',
                        'modelAccuracy': '',
 '                      snomedDict': {}
    } #Object save information about columns} #Object save information about columns
    columnsItems = []
    df_text = df # save copy for regression data
    columsAll = []
    deleteColumnsArray = deleteColumns.split(',')
    # set the predictor columns names
    predictors = columnsIdPrediction.split(',')

    # Save the dictionary SNOMED in JSON for DSS and model manager
    array = predictors
    result = generate_object(array)
    print(result)
    nameColumnsObject['snomedDict'] = result




    # predictors =  lis 
    gr = ''

    #Save all the original  information the items and columns in JSON file for deploy model
    for x in df.columns:
        columsAll.append(x)
        if df[x].dtypes == 'object':# just if items of columns is object
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


    nameColumnsObject['predictorClasses'] = predictors
    nameColumnsObject['all'] = columsAll
    nameColumnsObject['deleteColumns'] = deleteColumnsArray
    nameColumnsObject['classesStringToNumberDictionary'] = classesStringTONumberDictionariTmp
    nameColumnsObject['algorithmUsed'] = 'Kmeans'
    nameColumnsObject['libraryUsed'] = 'h2o'    
    nameColumnsObject['targetClass'] = class_column
    nameColumnsObject['description'] = 'K-Means falls in the general category of clustering algorithms. Clustering is a form of unsupervised learning that tries to find structures in the data without using any labels or target values. Clustering partitions a set of observations into separate groupings such that an observation in a given group is more similar to another observation in the same group than to another observation in a different group.'
    nameColumnsObject['libraryVersion'] = h2o.__version__
    nameColumnsObject['titleOfPlot'] = 'Prediction'
    nameColumnsObject['version'] = '1.0'
    nameColumnsObject['advancedModel'] = advancedModel

    


    # for idx,target_list in enumerate(columnsItems):
    #     if idx != 0:
    #         columnsItems.pop(idx)

        

    
        
    print(df.T)

    
    #Change strings columns we can assign numbers like '0' and '1' respectively so that our algorithms can work on the data.
    for x in df.columns:
        if df[x].dtypes == 'object':# just if items of columns is object
            for idx ,val in enumerate(df[x].unique().tolist()):
                df[x][df[x] == val] = idx  # change the strings per numbers to each column
                
    print(df.T)
    
    
    # Importing the datasets and assign them to internal attributes
    trainData = h2o.H2OFrame(df)   #h2o.import_file(trainDataName)


    print(trainData)
    print(trainData.columns[0])

    if platform == "linux" or platform == "linux2":
        trainData.rename(columns={trainData.columns[0]:trainData.columns[0].replace('ï»¿','')})
    else:    
        trainData.rename(columns={trainData.columns[0]:trainData.columns[0].replace('Ã¯Â»Â¿','')})

    print(trainData.columns[0])

    trainData = trainData.drop([0], axis=0)


    print(trainData)

    #Datatest for testing model builded
    datatest = trainData.as_data_frame(use_pandas=True,header=True);
    datos = datatest.sample(n=200)
    rango_array = datos[class_column][0:].values
    rango_array = rango_array.tolist()


    #delete columns chosen
    if deleteColumnsArray[0] != '_':
        for column in deleteColumnsArray:
            print(column)
            trainData = trainData.drop(column)

    

    print(trainData.columns)


   
    loadingJSON(username_agg, 30, 'Building dataset', False)
    time.sleep(2.0)

    
    # Remove old images and csv
    if(os.path.isfile(imagesPath + params['modelName'] + "_Predictor_boxplot.png")):
            os.remove(imagesPath + params['modelName'] + "_Predictor_boxplot.png")
    
    if(os.path.isfile(imagesPath + params['modelName'] + "_Phase_boxplot.png")):
            os.remove(imagesPath + params['modelName'] + "_Phase_boxplot.png")
            
    if(os.path.isfile(imagesPath + params['modelName'] + "_Cluster_boxplot.png")):
            os.remove(imagesPath + params['modelName'] + "_Cluster_boxplot.png")
            
    if(os.path.isfile(imagesPath + params['modelName'] + "_matrix_confusion.png")):
            os.remove(imagesPath + params['modelName'] + "_matrix_confusion.png")
    
    if(os.path.isfile(csvPath + params['modelName'] + "_data_matrix.csv")):
            os.remove(csvPath + params['modelName'] + "_data_matrix.csv")
                    
    if(os.path.isfile(csvPath + params['modelName'] + "_data_prediction.csv")):
            os.remove(csvPath + params['modelName'] + "_data_prediction.csv")
            

    if(os.path.isfile(csvPath + params['modelName'] + "_dataGLMBinoMatizConfusionPandas.csv")):
            os.remove(csvPath + params['modelName'] + "_dataGLMBinoMatizConfusionPandas.csv")
    

   

    
    loadingJSON(username_agg, 45, 'Building data chooseen users', False)
    time.sleep(2.0)    
    
    trainData_Regresion = h2o.H2OFrame(df_text)#regression data

    if platform == "linux" or platform == "linux2":
        trainData_Regresion.rename(columns={trainData_Regresion.columns[0]:trainData_Regresion.columns[0].replace('ï»¿','')})
    else:    
        trainData.rename(columns={trainData.columns[0]:trainData.columns[0].replace('Ã¯Â»Â¿','')})

    

    print(trainData_Regresion.columns[0])

    trainData_Regresion = trainData_Regresion.drop([0], axis=0)

    shapPlots.ShapClassPlots(modelName,username_agg, trainDataName,deleteColumns,columnsIdPrediction,class_column, trainData_Regresion, cancer)
        
    #export in csv regression data
    h2o.export_file(trainData_Regresion, path = csvPath + params['modelName'] + "_data_regresion.csv" ,force=True)
    print('********************************************************************************************************', len(class_column))
    if len(predictors) < 3:


        df = trainData.as_data_frame(use_pandas=True,header=True);



        df[class_column] = df[class_column].astype("category")
        print(df)

        groups = df.groupby(class_column)
        fig, ax = plt.subplots(1,1,figsize=(20,15))


        loadingJSON(username_agg, 55, 'Building prediction columns', False)
        time.sleep(2.0)   

        for name, group in groups:
            #print(name, group)
            #ax[0].plot(group['age'], group['sex'],group['stage'], group['plasma_CA19_9'],group[predictors[0]], group[predictors[1]],group['LYVE1'], group['REG1A'],group['TFF1'], marker='o', linestyle='', ms=7, label=cancer[int(name)])
            ax.scatter(group[predictors[0]], group[predictors[1]],label=cancer[int(name)])
            

        fig.suptitle("Original Pancreatic Cancer dataset - " + predictors[0] + " & " + predictors[1], fontsize=20)    
        ax.legend(numpoints=1)

        plt.savefig(imagesPath + modelName + "_Original_Pancreatic_Cancer2 -" + predictors[0] + " & " + predictors[1] + ".png")     #  create original images plots      


        # load data to h2o
        data_h2o_df = h2o.H2OFrame(df)

        # run h2o Kmeans to estimate good start points
        h2o_df = H2OKMeansEstimator(k=cluster_k, init=ini, standardize=standari)

        start = timer()
        h2o_df.train(x=[predictors[0],predictors[1]], training_frame=data_h2o_df)
        #h2o_df.train(x=["age","sex","stage","plasma_CA19_9",predictors[0],predictors[1],"LYVE1","REG1A","TFF1"], training_frame=data_h2o_df)

        end = timer()

        user_points = h2o.H2OFrame(h2o_df.centers())

        # show details
        h2o_df.show()
        print("Time:", timedelta(seconds=end-start))

        # run h2o constrained Kmeans
        h2o_co_df = H2OKMeansEstimator(k=cluster_k, user_points=user_points, cluster_size_constraints=aggregator_cluster_size_1, standardize=standari)

        start = timer()
        h2o_co_df.train(x=[predictors[0],predictors[1]], training_frame=data_h2o_df)
        #h2o_co_df.train(x=["age","sex","stage","plasma_CA19_9",predictors[0],predictors[1],"LYVE1","REG1A","TFF1"], training_frame=data_h2o_df)
        end = timer()

        # show details
        h2o_co_df.show()
        time_h2o_co_df = timedelta(seconds=end-start)
        print("Time:", time_h2o_co_df)

        from h2o.estimators.aggregator import H2OAggregatorEstimator

        # original data size 500, constraints [50, 50, 50 ]
        # aggregated data size 250, constaints [5, 10, 5]

        params = {
            "target_num_exemplars": 500,
            "rel_tol_num_exemplars": 0.5,
            "categorical_encoding": "eigen"
        }
        agg = H2OAggregatorEstimator(**params)

        start = timer()
        agg.train(x=[predictors[0],predictors[1],class_column], training_frame=data_h2o_df)
        #agg.train(x=["age","sex","stage","plasma_CA19_9",predictors[0],predictors[1],"LYVE1","REG1A","TFF1"], training_frame=data_h2o_df)
        data_agg_12_df = agg.aggregated_frame

       

        # run h2o Kmeans
        h2o_co_agg_12_df = H2OKMeansEstimator(k=cluster_k, user_points=user_points, cluster_size_constraints=aggregator_cluster_size_2, standardize=standari)

        h2o_co_agg_12_df.train(x=[predictors[0],predictors[1]],training_frame=data_agg_12_df)
        #h2o_co_agg_12_df.train(x=["age","sex","stage","plasma_CA19_9",predictors[0],predictors[1],"LYVE1","REG1A","TFF1"],training_frame=data_agg_12_df)
        end = timer()

        # show details
        h2o_co_agg_12_df.show()
        time_h2o_co_agg_12_df = timedelta(seconds=end-start)
        print("Time:", time_h2o_co_agg_12_df)


        # original data size 500, constraints [50, 50, 50]
        # aggregated data size 250, constaints [2, 5, 2]

        params = {
            "target_num_exemplars": 250,
            "rel_tol_num_exemplars": 0.5,
            "categorical_encoding": "eigen"
        }
        agg_14 = H2OAggregatorEstimator(**params)

        start = timer()
        agg_14.train(x=[predictors[0],predictors[1],class_column], training_frame=data_h2o_df)
        #agg_14.train(x=["age","sex","stage","plasma_CA19_9",predictors[0],predictors[1],"LYVE1","REG1A","TFF1"], training_frame=data_h2o_df)
        data_agg_14_df = agg_14.aggregated_frame

        loadingJSON(username_agg, 60, 'Building the graphics', False)
        time.sleep(2.0)

        # run h2o Kmeans
        h2o_co_agg_14_df = H2OKMeansEstimator(
                                            model_id = modelName + str(cluster_k),
                                            k=cluster_k,
                                            user_points=user_points,
                                            cluster_size_constraints=aggregator_cluster_size_3,
                                            standardize=standari)

        #h2o_co_agg_14_df.train(x=["age","sex","stage","plasma_CA19_9",predictors[0],predictors[1],"LYVE1","REG1A","TFF1"],training_frame=data_agg_14_df)
        h2o_co_agg_14_df.train(x=[predictors[0],predictors[1]],training_frame=data_agg_14_df)

        performace = h2o_co_agg_14_df.model_performance()
        print('h2o_co_agg_14_df Performance: ' , performace)

        mse = h2o_co_agg_14_df.mse()  ##score(train[:, predictors])
        rmse = h2o_co_agg_14_df.rmse() 
        # Calculate the NOBS
        nobs = data_agg_14_df.as_data_frame().shape[0]


        h2o_co_agg_14_df.download_mojo(path = modelPath , get_genmodel_jar = True)# Create MOJO Model
        h2o.download_pojo(h2o_co_agg_14_df, modelPath , get_jar = True)# Create POJO Model
        h2o.download_model(h2o_co_agg_14_df, modelPath);


        h2o.export_file(h2o.H2OFrame(pd.DataFrame([diagnostics_from_clusteringmodel(h2o_co_agg_14_df)])), path = csvPath + "Cluster_boxplot.csv" ,force=True) 
        plt.savefig(imagesPath + "Cluster_boxplot.png")   #  create images plots        
        end = timer()
        trainDataPre = trainData
        predic = h2o_co_agg_14_df.predict(trainDataPre)
        trainDataPre["Predicted"] = predic["predict"].asfactor()
        h2o.export_file(trainDataPre.tail(7500), path = csvPath + modelName + "1_Predictor_boxplot.csv" ,force=True) 

        drpo = trainDataPre[:, predictors].tail(1500)
        drpo['Predicted'] =  trainDataPre["Predicted"].tail(1500)
        print('------');
        print(drpo);
        print('------');
        h2o.export_file(drpo, path = csvPath + modelName + "_Phase_boxplot.csv" ,force=True) 

        #only include 1000 to 1500 lines per performance    
        final = trainDataPre[predictors].tail(1000)

        # Create csv for the plots d3.js
        h2o.export_file(trainDataPre["Predicted"], path = csvPath + modelName + "1_data_prediction.csv" ,force=True)
        h2o.export_file(final, path = csvPath + modelName + "1_data_matrix.csv" ,force=True)

        # show details
        h2o_co_agg_14_df.show()
        time_h2o_co_agg_14_df = timedelta(seconds=end-start)
        print("Time:", time_h2o_co_agg_14_df)

        # This creates a "groupby" object (not a dataframe object) 
        # and you store it in the week_grouped variable.
        groups = df.groupby(class_column)
        fig, ax = plt.subplots(1,1,figsize=(20,15))

        print('****************************************************************************************************************************************************************************')


        print('****************************************************************************************************************************************************************************')
        head = True;

        finalPlotsColumns = []
        for columns in predictors:
            finalPlotsColumns.append(columns)

        finalPlotsColumns.append(class_column);

      

        for name, group in groups:
            #ax[0].plot(group['age'], group['sex'], marker='o', linestyle='', ms=7, label=cancer[int(name)])
            ax.scatter(group[predictors[0]], group[predictors[1]],label=cancer[int(name)])
            with open(imagesPath + modelName + "_Original_Pancreatic_Cancer2.csv", 'a', newline='', encoding='utf-8') as f:
                if head:
                    group[finalPlotsColumns].to_csv(f, index=False, mode='a')
                    head = False
                else:
                    group[finalPlotsColumns].to_csv(f, index=False, header=False, mode='a')
        

        fig.suptitle("Original Pancreatic cancer dataset - " + predictors[0] + " & " + predictors[1], fontsize=20)
        ax.legend(numpoints=1)

        plt.savefig(imagesPath + modelName + "_Original_Pancreatic_Cancer2 - " + predictors[0] + " & " + predictors[1] + ".png")     #  create original images plots      


        data_agg_df_12_df = data_agg_12_df.as_data_frame()
        data_agg_df_12_df[class_column] = data_agg_df_12_df[class_column].astype("category")

        groups = data_agg_df_12_df.groupby(class_column)
        fig, ax = plt.subplots(1,1,figsize=(20,15))
        head = True;

        for name, group in groups:
            #ax.plot(group['age'], group['sex'],group['stage'], group['plasma_CA19_9'],group[predictors[0]], group[predictors[1]],group['LYVE1'], group['REG1A'],group['TFF1'], marker='o', linestyle='', ms=7, label=cancer[int(name)])
            ax.scatter(group[predictors[0]], group[predictors[1]],label=cancer[int(name)])
            with open(imagesPath + modelName + "_Aggregated_1_2_size_.csv", 'a', newline='', encoding='utf-8') as f:
                if head:
                    group[finalPlotsColumns].to_csv(f, index=False, mode='a')
                    head = False
                else:
                    group[finalPlotsColumns].to_csv(f, index=False, header=False, mode='a')
        

        fig.suptitle("Aggregated (1/2 size) Pancreatic Cancer Dataset", fontsize=20)  
        ax.legend(numpoints=1)

        plt.savefig(imagesPath + modelName + "_Aggregated_1_2_size_.png")     #  create original images plots      
        # h2o.export_file(drpo, path = imagesPath + modelName + "_Aggregated_1_2_size_ -" + predictors[0] + " & " + predictors[1] + ".csv" ,force=True) 


        data_agg_df_14_df = data_agg_14_df.as_data_frame()
        data_agg_df_14_df[class_column] = data_agg_df_14_df[class_column].astype("category")

        groups = data_agg_df_14_df.groupby(class_column)
        fig, ax = plt.subplots(1,1,figsize=(20,15))

        head = True

        for name, group in groups:
            #ax.plot(group['age'], group['sex'],group['stage'], group['plasma_CA19_9'],group[predictors[0]], group[predictors[1]],group['LYVE1'], group['REG1A'],group['TFF1'], marker='o', linestyle='', ms=7, label=cancer[int(name)])
            ax.scatter(group[predictors[0]], group[predictors[1]],label=cancer[int(name)])
            with open(imagesPath + modelName + "_Aggregated_1_4_size_.csv", 'a', newline='', encoding='utf-8') as f:
                if head:
                    group[finalPlotsColumns].to_csv(f, index=False, mode='a')
                    head = False
                else:
                    group[finalPlotsColumns].to_csv(f, index=False, header=False, mode='a')

        fig.suptitle("Aggregated (1/4 size) df Dataset - " + predictors[0] + " & " + predictors[1], fontsize=20)  
        ax.legend(numpoints=1)

        plt.savefig(imagesPath + modelName + "_Aggregated_1_4_size_ - " + predictors[0] + " & " + predictors[1] + ".png")     #  create original images plots  
    


        df["pred"] = h2o_df.predict(data_h2o_df).as_data_frame()['predict'].astype("category")

        groups = df.groupby('pred')
        fig, ax = plt.subplots(1,1,figsize=(20,15))

        head = True

        pred = columnsIdPrediction.split(',')
        pred.append('pred')

        for name, group in groups:
            #ax.plot(group['age'], group['sex'],group['stage'], group['plasma_CA19_9'],group[predictors[0]], group[predictors[1]],group['LYVE1'], group['REG1A'],group['TFF1'], marker='o', linestyle='', ms=7, label=cancer[int(name)])
            ax.scatter(group[predictors[0]], group[predictors[1]],label=cancer[int(name)])
            with open(imagesPath + modelName + "_Predictions_of_standard_K-means.csv", 'a', newline='', encoding='utf-8') as f:
                if head:
                    group[pred].to_csv(f, index=False, mode='a')
                    head = False
                else:
                    group[pred].to_csv(f, index=False, header=False, mode='a')

        fig.suptitle("Predictions of standard K-means - " + predictors[0] + " & " + predictors[1], fontsize=20)  
        ax.legend(numpoints=1)

        plt.savefig(imagesPath + modelName + "_Predictions_of_standard_K-means- " + predictors[0] + " & " + predictors[1] + ".png")     #  create original images plots      


        df["pred"] = h2o_df.predict(data_h2o_df).as_data_frame()['predict'].astype("category")

        groups = df.groupby('pred')
        fig, ax = plt.subplots(1,1,figsize=(20,15))

        hea = True

        for name, group in groups:
            #ax.plot(group['age'], group['sex'],group['stage'], group['plasma_CA19_9'],group[predictors[0]], group[predictors[1]],group['LYVE1'], group['REG1A'],group['TFF1'], marker='o', linestyle='', ms=7, label=cancer[int(name)])
            ax.scatter(group[predictors[0]], group[predictors[1]],label=cancer[int(name)])
            with open(imagesPath + modelName + "_Predictions_of_standard_K-means_2.csv", 'a', newline='', encoding='utf-8') as f:
                if head:
                    group[pred].to_csv(f, index=False, mode='a')
                    head = False
                else:
                    group[pred].to_csv(f, index=False, header=False, mode='a')

        fig.suptitle("Predictions of standard K-means - " + predictors[0] + " & " + predictors[1], fontsize=20)  
        ax.legend(numpoints=1)

        plt.savefig(imagesPath + modelName + "_Predictions_of_standard_K-means- " + predictors[0] + " & " + predictors[1] + "2.png")     #  create original images plots      

 
        df["co_pred"] = h2o_co_df.predict(data_h2o_df).as_data_frame()['predict'].astype("category")
        groups = df.groupby('co_pred')
        fig, ax = plt.subplots(1,1,figsize=(20,15))

        head = True

        co_pred = columnsIdPrediction.split(',')
        co_pred.append('co_pred')

        # testdf = pd.DataFrame(df.groupby('co_pred'))
        # final = testdf.reset_index().to_csv('CCCC_output_summary.csv', sep='', header=True, index=False)
        # print(final)

        for name, group in groups:
            #ax.plot(group['age'], group['sex'],group['stage'], group['plasma_CA19_9'],group[predictors[0]], group[predictors[1]],group['LYVE1'], group['REG1A'],group['TFF1'], marker='o', linestyle='', ms=7, label=cancer[int(name)])
            ax.scatter(group[predictors[0]], group[predictors[1]],label=cancer[int(name)])
            # group.to_csv(imagesPath + modelName + "_Predictions_of_Constrained_K-means_trained_Pancreatic-Cancer_Dataset_2_1.csv", index=False, mode='a', sep=',')
            with open(imagesPath + modelName + "_Predictions_of_Constrained_K-means_trained_Pancreatic-Cancer_Dataset_2.csv", 'a', newline='', encoding='utf-8') as f:
                if head:
                    group[co_pred].to_csv(f, index=False, mode='a')
                    head = False
                else:
                    group[co_pred].to_csv(f, index=False, header=False, mode='a') 

        fig.suptitle("Predictions of Constrained K-means trained with whole Pancreatic-Cancer Dataset - " + predictors[0] + " & " + predictors[1], fontsize=20)  
        ax.legend(numpoints=1)

        plt.savefig(imagesPath + modelName + "_Predictions_of_Constrained_K-means_trained_Pancreatic-Cancer_Dataset_" + predictors[0] + " & " + predictors[1] + "2.png")     #  create original images plots      


        df["co_pred_1/2"] = h2o_co_agg_12_df.predict(data_h2o_df).as_data_frame()['predict'].astype("category")

        groups = df.groupby('co_pred_1/2')
        fig, ax = plt.subplots(1,1,figsize=(20,15))

        head = True

        co_pred_1_2 = columnsIdPrediction.split(',')
        co_pred_1_2.append('co_pred_1/2')

        print('$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$', co_pred_1_2)

        for name, group in groups:
            #ax.plot(group['age'], group['sex'],group['stage'], group['plasma_CA19_9'],group[predictors[0]], group[predictors[1]],group['LYVE1'], group['REG1A'],group['TFF1'], marker='o', linestyle='', ms=7, label=cancer[int(name)])
            ax.scatter(group[predictors[0]], group[predictors[1]],label=cancer[int(name)])
            with open(imagesPath + modelName + "_Predictions_of_Constrained_K-means_trained_with_aggregated_1_2_of_size_Pancreatic-Cancer_Dataset_2.csv", 'a', newline='', encoding='utf-8') as f:
                if head:
                    group[finalPlotsColumns].to_csv(f, index=False, mode='a')
                    head = False
                else:
                    group[finalPlotsColumns].to_csv(f, index=False, header=False, mode='a')  

        fig.suptitle("Predictions of Constrained K-means trained with aggregated (1/2 of size) Pancreatic-Cancer Dataset - " + predictors[0] + " & " + predictors[1], fontsize=20)  
        ax.legend(numpoints=1)


        plt.savefig(imagesPath + modelName + "_Predictions_of_Constrained_K-means_trained_with_aggregated_1_2_of_size_Pancreatic-Cancer_Dataset_" + predictors[0] + " & " + predictors[1] + "2.png")     #  create original images plots      


        df["co_pred_1/4"] = h2o_co_agg_14_df.predict(data_h2o_df).as_data_frame()['predict'].astype("category")

        groups = df.groupby('co_pred_1/4')
        fig, ax = plt.subplots(1,1,figsize=(20,15))

        head = True

        co_pred_1_4 = columnsIdPrediction.split(',')
        co_pred_1_4.append('co_pred_1/4')

        for name, group in groups:
            #ax.plot(group[predictors[0]], group[predictors[1]], marker='o', linestyle='', ms=7, label=cancer[int(name)])
            ax.scatter(group[predictors[0]], group[predictors[1]],label=cancer[int(name)])
            with open(imagesPath + modelName + "_Predictions_of_Constrained_K-means_trained_with_aggregated_1_4_of_size_Pancreatic-Cancer_Dataset_2.csv", 'a', newline='', encoding='utf-8') as f:
                if head:
                    group[co_pred_1_4].to_csv(f, index=False, mode='a')
                    head = False
                else:
                    group[co_pred_1_4].to_csv(f, index=False, header=False, mode='a')  

        fig.suptitle("Predictions of Constrained K-means trained with aggregated (1/4 of size) Pancreatic-Cancer Dataset  - " + predictors[0] + " & " + predictors[1], fontsize=20)  
        ax.legend(numpoints=1)

        plt.savefig(imagesPath + modelName + "_Predictions_of_Constrained_K-means_trained_with_aggregated_1_4_of_size_Pancreatic-Cancer_Dataset_" + predictors[0] + " & " + predictors[1] + "2.png")     #  create original images plots      

        # save information for plots in JSON
        nameColumnsObject['modelContainerFile'] = modelName + "_model.h2o"


        plotAvailable['typeOfPlot'] = 'scatterPlot'
        plotAvailable['xAxis'] = predictors[0]
        plotAvailable['yAxis'] = predictors[1]
        plotAvailable['value'] = 'cluster'

        nameColumnsObject['plotAvailable'] = [plotAvailable]


        loadingJSON(username_agg, 80, 'Buiding predictions', False)
        time.sleep(2.0)    

        #Difference between result centroids calculated based on all data and aggregated data

        centers_co_df = pd.DataFrame(h2o_co_df.centers())
        centers_co_df["algo"] =  class_column #"diagnosis"
        centers_co_agg_12_df = pd.DataFrame(h2o_co_agg_12_df.centers())
        centers_co_agg_12_df["algo"] =  class_column + "_co_agg_1/2"
        centers_co_agg_14_df = pd.DataFrame(h2o_co_agg_14_df.centers())
        centers_co_agg_14_df["algo"] =  class_column + "_co_agg_1/4"


        centers_all_df = pd.concat([centers_co_df, centers_co_agg_12_df, centers_co_agg_14_df])
        print(centers_all_df)

        groups = centers_all_df.groupby('algo')
        fig, ax = plt.subplots(1,1,figsize=(20,15))

        head = True

        algo = columnsIdPrediction.split(',')
        algo.append('algo')

        for name, group in groups:
            ax.plot(group[0], group[1], marker='o', linestyle='', ms=7, label=name)
            with open(imagesPath + modelName + "_Centroids_of_Constrained_K-means_.csv", 'a', newline='', encoding='utf-8') as f:
                if head:
                    group.to_csv(f, index=False, mode='a')
                    head = False
                else:
                    group.to_csv(f, index=False, header=False, mode='a')  

        fig.suptitle("Centroids of Constrained K-means", fontsize=20)  
        ax.legend(numpoints=1)
        plt.savefig(imagesPath + modelName + "_Centroids_of_Constrained_K-means_" + predictors[0] + " & " + predictors[1] + ".png")     #  create original images plots     
 

    
    else:

        print(columnsIdPrediction)

        loadingJSON(username_agg, 55, 'Building prediction columns', False)
        time.sleep(2.0)   

        nc = trainData.ncol
        print(nc)

        work_final = trainData #.drop(0)

        print(work_final) 
        
        # split into train and validation sets
        train, valid = work_final.split_frame(ratios = [split], seed = seed)
        
    
        

    

        can_seaborn = True
        can_pandas = True

    
        if can_seaborn:
            sns.set()
        
        # if can_seaborn:
        #     sns.set_context("notebook")
        #     sns.pairplot(work_final.as_data_frame(True), vars=predictors, hue=class_column)
            #sns.pairplot(work_final.as_data_frame(True), vars=predictors, hue=predictors[0] ,palette="husl",kind="reg")
            #sns.pairplot(work_final.as_data_frame(True), vars=predictors, hue="Phase" ,palette="husl",kind="reg")
            
                    
        
        plt.savefig(imagesPath + params['modelName'] + "_Phase_boxplot.png")     #  create images plots    
        plt.close() 

        # sns.scatterplot(x='creatinine', y='age', data=work_final.as_data_frame(True), hue=class_column)
        # plt.savefig(imagesPath + params['modelName'] + "_scatter_boxplot.png")     #  create images plots     


        # # beautifying the labels
        # plt.xlabel('value')
        # plt.ylabel('density')
        # plt.savefig(imagesPath + params['modelName'] + "diagnosis.png")     #  create images plots     

        groups = work_final.as_data_frame(use_pandas=True, header=True).groupby(class_column)
        fig, ax = plt.subplots(1,1,figsize=(20,15))


        for name, group in groups:
            print(name, group)
            for column in group.columns:
                print(column)
                ax.plot(group[class_column], group[column], marker='o', linestyle='', ms=7, label=name)

        


        fig.suptitle("Original cancer dataset", fontsize=20)
        ax.legend(numpoints=1)

        plt.savefig(imagesPath + params['modelName'] + "_original_dataset.png")     #  create original images plots      


        loadingJSON(username_agg, 60, 'Building the graphics', False)
        time.sleep(2.0)

        #specify user point automatically, where each row represents an initial cluster center.
        # dataframe_UserPoints = H2OKMeansEstimator(k=cluster_k, init=ini, standardize=standari)
        # dataframe_UserPoints.train(x=predictors, training_frame=train)
        # user_points = h2o.H2OFrame(dataframe_UserPoints.centers())
        # print(user_points)
            
        #Train K-Means by range cluster 2-5
        dle_m = [H2OKMeansEstimator(  model_id = modelName + str(clusters), #+ '_' + str(clusters) + '.h2o', ihelp
                                        k=clusters,
                                        init=ini, #Random
                                        seed=seed,
                                        standardize=standari,
                                        # user_points=user_points,
                                        # estimate_k=True,
                                        max_iterations =  maxi) for clusters in range(1,cluster_k)]

    

        
        
        # Create train models kmeans in all clusters
        for idx,estimator in enumerate(dle_m):
            estimator.train(x=predictors,
                            training_frame = train[:, predictors],
                            validation_frame=valid[:, predictors]) # x all columns   work_final
            
            mse = estimator.mse()  ##score(train[:, predictors])
            rmse = estimator.rmse() 
            # Calculate the NOBS
            nobs = train[:, predictors].as_data_frame().shape[0]


            print('Metrics: ', mse, rmse, nobs)

            estimator.download_mojo(path = modelPath , get_genmodel_jar = True)# Create MOJO Model
            h2o.download_pojo(estimator, modelPath , get_jar = True)# Create POJO Model
            h2o.download_model(estimator, modelPath);


            # estimator.explain(valid)
            # estimator.leader.explain(valid)
            # m = estimator.fit(valid[:, predictors])
            # explainer = shap.Explainer(m,train[:, predictors])
            # shap_values = explainer(train[:, predictors])
            # print(shap_values.shape) # (8658, 3)
            # print(shap_values.shape == data_h2o_df.shape) # True
            # print(type(shap_values)) # <class 'shap._explanation.Explanation'>

            # shap.plots.waterfall(shap_values[1])

            # modelH20_Path = h2o.save_model(model=estimator, path=modelPath, force=True)
            # path = os.path.dirname(os.path.abspath(modelH20_Path))
            # os.rename(modelH20_Path, os.path.join(path,'f'+modelPath))
            performace = estimator.model_performance()
            print('Performance: ' , performace)

            
           

            # estimator.show()
            # print('AUC: #################################################', estimator.auc)
            # print('R2: #################################################', estimator.r2)
            # print('Performance: #################################################', estimator.model_performance())

            # print('Betweenss: #################################################',estimator.betweenss())
            # print('Totss: #################################################',estimator.totss())

            # #estimator.show()
            # print('Fit: #################################################', estimator.fit(valid))
            # print the total within cluster sum-of-square error for the validation dataset
            # print("sum-of-square error for valid:", estimator.tot_withinss(valid = True))
            # print('Frame Centers per column', h2o.H2OFrame(estimator.centers()))
        
        #python.exe .\kMeans\KmeansModel.py  'Clustering_H2O.20220301' 'pancreatic-cancer.csv' 'age,sex,stage,plasma_CA19_9,creatinine,REG1B,LYVE1,REG1A,TFF1' '75' '3' 'PlusPlus' '3' '3' 'True' 'pepe45' 'sample_id,patient_cohort,sample_origin'
        

        loadingJSON(username_agg, 70, 'Buiding model', False)
        time.sleep(2.0)    
        
        #Draw Plots   
        if can_pandas:
            diagnostics = pd.DataFrame( [diagnostics_from_clusteringmodel(model) for model in dle_m])
            diagnostics.set_index('Clusters', inplace=True)
    
        #Draw Plots
        if can_pandas:
            diagnostics.plot(kind='line');
        loadingJSON(username_agg, 75, 'Draw plots', False)
        time.sleep(2.0)    
                
        h2o.export_file(h2o.H2OFrame(pd.DataFrame([diagnostics_from_clusteringmodel(model) for model in dle_m])), path = csvPath + "Cluster_boxplot.csv" ,force=True) 
        plt.savefig(imagesPath + "Cluster_boxplot.png")   #  create images plots        
        
        
        # clusters = 4 #cluster_k
        # predic = dle_m[clusters-2].predict(trainData)
        # trainData["Predicted"] = predic["predict"].asfactor()

        for idx,cluster in enumerate(dle_m):
            predic = cluster.predict(trainData)
            trainData["Predicted_" + str(idx)] = predic["predict"].asfactor()
            plt.figure(figsize=(20,10))

            delete = deleteColumnsArray
    
            delete.append(class_column)
            
            # Quitar la columna que no deseas utilizar en la predicción
            datos_sin_columna = datos.drop(delete, axis=1)

            # Obtener los nombres de las características
            nombres_caracteristicas = datos_sin_columna.columns

            # Asignar los nombres de las características a los datos de entrada
            datos_sin_columna.columns = nombres_caracteristicas
            print(datos_sin_columna)

            # Convert Pandas DataFrame to H2O Frame
            h2o_frame_datos_sin_columna = h2o.H2OFrame(datos_sin_columna)

            prediction = cluster.predict(h2o_frame_datos_sin_columna)

            # Convert H2O Frame to Pandas DataFrame
            prediction = prediction.as_data_frame()

            # print(prediction)
            # arrayCancer= str(cancer).split(',') 
            # print(type (cancer))


            # prediction['predict'] = prediction['predict'].map(lambda x: arrayCancer[x])

            # print(prediction)
            # print(rango_array)

            # # Iterar por las filas del DataFrame
            # for index, row in rango_array.iterrows():
            #     pre = row['predict']
              
            #     print(f'Nombre: {pre}')

            # print(prediction)

            # for x in prediction:
            #     print('Prediction: ',  cancer[x] )
            
            
            count = 0
            for idxj, x in prediction.iterrows():
                pre = x['predict']
                if str(pre) == str(rango_array[idxj]):
                    count += 1
                print(str(pre) + ' -> ' +  str(rango_array[idxj]))
                
            print('############################# Accuracy: ' + str(count) + ' of ' + str(len(prediction)) + ' predictions')
            hits = calculate_so_per_100_hits(count, len(prediction)) 

            print(f"Model: {hits}:%" )
            nameColumnsObject['modelAccuracy'] = str(hits)

        
            #Draw plots in png
            # if can_seaborn:
            # sns.pairplot(trainData.as_data_frame(True), vars=predictors,  hue="Predicted_"+ str(idx))#, palette="husl",kind="reg")
            # plt.savefig(imagesPath + params['modelName'] + "_Phase_boxplot_predicted_" + str(idx) + ".png", dpi=300)     #  create images plots      

            # plt.close() 

            # sns.scatterplot(x='creatinine', y='REG1B', data=trainData.as_data_frame(True), hue="diagnosis")
            # plt.savefig(imagesPath + params['modelName'] + "_scatter_boxplot.png")     #  create images plots    

            # plt.close() 

            
            # Create csv for the plots d3.js          
            h2o.export_file(trainData.tail(7500), path = csvPath + params['modelName'] + str(idx) + "_Predictor_boxplot.csv" ,force=True) 
            plt.savefig(imagesPath + params['modelName'] + str(idx) + "_Predictor_boxplot.png")   #  create images plots  

            
            #only include 1000 to 1500 lines per performance    
            final = trainData[predictors].tail(1000)

            
        
            
            drpo = work_final[:, predictors].tail(1500)
            drpo['Predicted_'+ str(idx)] =  trainData["Predicted_" + str(idx)].tail(1500)
            print('------');
            print(drpo);
            print('------');

            loadingJSON(username_agg, 80, 'Buiding predictions', False)
            time.sleep(2.0)    
            
            # Create csv for the plots d3.js
            h2o.export_file(trainData.tail(7500), path = csvPath + params['modelName'] + str(idx) + "_Predictor_boxplot.csv" ,force=True) 

            h2o.export_file(drpo, path = csvPath + params['modelName'] + "_Phase_boxplot.csv" ,force=True) 

            # Create csv for the plots d3.js
            h2o.export_file(trainData["Predicted_"+ str(idx)], path = csvPath + params['modelName'] + "1_data_prediction.csv" ,force=True)            
            # h2o.export_file(trainData["Predicted_"+ str(idx)], path = csvPath + params['modelName'] + str(idx) + "_data_prediction.csv" ,force=True)

            h2o.export_file(final, path = csvPath + params['modelName'] + str(idx) + "_data_matrix.csv" ,force=True)
            print("\n\nPredictions: \n %(predictions)s" % {'predictions':trainData["Predicted_"+ str(idx)].as_data_frame(use_pandas=True, header=True)})

            #plotting the results:
            # model = h2o.load_model(modelPath +'/'+ params['modelName'] + '2')
            # #predict the labels of clusters.
            label = h2o.as_list(trainData["Predicted_"+ str(idx)], use_pandas=True)
            df_temp_t = h2o.as_list(trainData, use_pandas=True)
            print('h2o Frame: ',label)
            labls = []

            # x = df_temp_t['creatinine']      
            # y = df_temp_t['age']     
            # fit = np.polyfit(x, y, deg=1)
            # df_temp_t.plot(kind='scatter', x='creatinine', y='age',figsize=(10, 6), color='darkblue')
            # # plot line of best fit
            # plt.plot(x, fit[0] * x + fit[1], color='red') # recall that x is the Years
            # plt.annotate('y={0:.0f} x + {1:.0f}'.format(fit[0], fit[1]), xy=(2000, 150000))

            # plt.title('Predicted with Diagnosis')
            # plt.xlabel('age')
            # plt.ylabel('creatinine')

            

            # plt.show()

            print(trainData)
            df_temp_t.set_index('Predicted_0', inplace=True)
            print(df_temp_t)

            # pre = df_temp_t.loc[[0,1,2], ['age','sex','stage','benign_sample_diagnosis','plasma_CA19_9','creatinine','LYVE1','REG1B','TFF1','REG1A','Predicted']]
            # pre.head()

            # pre.plot(kind='area',
            #          stacked=False,
            #          figsize=(20, 10))

            # df_tras = df_temp_t.loc[[0,1,2,3],['REG1A']]
            # df_tras.head()
            # df_tras.plot(kind='bar', figsize=(15, 6), rot=0)
            # # add a title to the histogram
            # plt.title('Histogram of age from Blood patiens')
            # # add y-label
            # plt.ylabel('diagnosis')
            # # add x-label
            # plt.xlabel('Prediction')

            for index , t in enumerate(label):
                if index != 0:
                    item = int(t[0])
                    labls.append(item)
                    print(item)

            # expression = "Predicted_" + str(idx)
            # expression = 'diagnosis'


            # label = np.array(labls)
            # print('np array: ',type(label), label)
            # # #Getting unique labels
            # u_labels = np.unique( trainData[expression].as_data_frame())
            # print('np: ', type(u_labels), u_labels)

          
            print(trainData)

            groups = trainData.as_data_frame(use_pandas=True,header=True).groupby(class_column)
            fig, ax = plt.subplots(1,1,figsize=(20,15))


            for name, group in groups:
                # plt.scatter(group[p == name , 0] , group[p == name , 1], label = name)
                dataConcat = []
                for column in columnsIdPrediction.split(','):
                    dataConcat.append(group[column])
                ax.plot(pd.concat(dataConcat, axis=0), marker='o', linestyle='', ms=7, label=name)
                pd.concat(dataConcat, axis=0).to_csv(imagesPath + params['modelName'] + str(idx) + "_scatter_original_" + str(name) + ".csv", index=False)

            fig.suptitle("Original dataset", fontsize=20)    
            ax.legend(numpoints=1)
            plt.savefig(imagesPath + params['modelName'] + str(idx) + "_scatter_original.png")   #  create images plots  

            gr = trainData.columns[len(trainData.columns) - 1]
            print(gr)
            groups = trainData.as_data_frame(use_pandas=True,header=True).groupby(gr)
            fig, ax = plt.subplots(1,1,figsize=(20,15))


            for name, group in groups:
                # plt.scatter(group[p == name , 0] , group[p == name , 1], label = name)
                dataConcat = []
                for column in columnsIdPrediction.split(','):
                    dataConcat.append(group[column])
                ax.plot(pd.concat(dataConcat, axis=0), marker='o', linestyle='', ms=7, label=name)
                pd.concat(dataConcat, axis=0).to_csv(imagesPath + params['modelName'] + str(idx) + "_scatter_Predicted_" + str(name) + ".csv", index=False)

            fig.suptitle("Standard Model", fontsize=20)    
            ax.legend(numpoints=1)
            plt.savefig(imagesPath + params['modelName'] + str(idx) + "_scatter_Predicted.png")   #  create images plots  

            # # First convert to a pandas df, then to a numpy array
            # num_array = trainData.as_data_frame().to_numpy()
            # print(num_array.shape)


            # # Reshape the array
            # reshaped_array = num_array.reshape(-1 , 209, 11, 11)

            # # Check the dimensions of the reshaped array
            # print(reshaped_array.shape)
            # # The output should be: (1000, 5, 5) 

            # print(reshaped_array)
            df_np = np.array(trainData.as_data_frame().to_numpy())
            print('Array Final: ',  df_np)
            plt.close('all')



            #plotting the results:
            # for i in u_labels:
            #     print(df_np[label == i , 0])
            #     plt.scatter(df_np[label == i , 0] , df_np[label == i , 1] , df_np[label == i , 2], label = i)
            #     # plt.scatter(centroids[:,0] , centroids[:,1] , s = 80, color = 'k')
            # plt.legend()
            # plt.savefig(imagesPath + params['modelName'] + "_Predictor_np.png")   #  create images plots  

            # plt.show()  
            
             

    predictors = columnsIdPrediction.split(',')
    print(predictors)

    if len(predictors) < 3:
        nameFile = str(modelName).replace('.','_') + str(cluster_k) + '.zip' # + '_2_h2o.zip' ihelp
    else:   
        nameFile = str(modelName).replace('.','_') + '2.zip' # + '_2_h2o.zip' ihelp


    print(nameFile)
    path_zip = imagesPath + nameFile
    path_extraction = imagesPath
    password = None
    file_zip = zipfile.ZipFile(path_zip, "r")

    loadingJSON(username_agg, 90, 'Ordering model, predictions and anomalies', False)
    time.sleep(2.0) 

    #Generate all images of columns and predaict in image for view 
    # for col in df_temp_t.columns:
    #     for co in df_temp_t.columns:
    #         plt.figure(figsize=(12, 6))
    #         sns.set_style('ticks')
    #         sns.regplot(x=col, y=co, data=df_temp_t, color='green', marker='+')
    #         plt.savefig(imagesPath + params['modelName'] + "_"+ str(col) + "_"+  str(co) +".png")   #  create images plots  

    # save information for plots in JSON
    nameColumnsObject['modelContainerFile'] =  modelName + str(idx) + "_scatter_" + gr + ".csv"

    plotAvailable['typeOfPlot'] = 'scatterPlot'
    plotAvailable['xAxis'] = 'X'
    plotAvailable['yAxis'] = ''
    plotAvailable['cluster'] = ''

    nameColumnsObject['plotAvailable'] = [plotAvailable]

    #Save json file
    with open(os.path.join(imagesPath, modelName + '_informationColumnsStringToNumber.json'),"w") as fp:
            json.dump(nameColumnsObject, fp)


    try:
        print(file_zip.namelist())
        file_zip.extractall(pwd=password, path=path_extraction)
    except:
        pass
    file_zip.close()

    shutil.move(imagesPath + 'experimental/modelDetails.json', imagesPath + modelName + '.json' )
     
    with open(imagesPath + modelName + '.json') as json_file:
        json_decoded = json.load(json_file)

        json_decoded['output']['training_metrics']['MSE'] = mse
        json_decoded['output']['training_metrics']['RMSE'] = rmse
        json_decoded['output']['training_metrics']['nobs'] = nobs


        json_decoded['typealgorithm'] = 'kmeans'
        json_decoded['typenamedata'] = trainDataName.split('/')[2]

    with open(imagesPath + modelName + '.json', 'w') as  json_file:
        json.dump(json_decoded,  json_file)


    # Directory of the files to be compressed for model manager
    images_directory_shap = imagesPath + 'shap_plots'
    images_directory_root = imagesPath
    csv_directory_pred = imagesPath + params['modelName'] + "1_data_prediction.csv"
    csv_directory_data = imagesPath + params['modelName'] + "_data_regresion.csv"
    csv_directory_metrics = imagesPath + params['modelName'] + "_data_regresion_prediction.csv"    
    json_file = os.path.join(imagesPath, params['modelName'] + '_informationColumnsStringToNumber.json')
    pkl_file = imagesPath + str(modelName).replace('.','_') + "1.zip"
    zip_file = imagesPath +  modelName + '.zip'

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

        zipf.write(csv_directory_pred, arcname=os.path.join('data', os.path.basename(imagesPath + params['modelName'] + "_model_predictions.csv")))
        zipf.write(csv_directory_data, arcname=os.path.join('data', os.path.basename(imagesPath + params['modelName'] + "_model_data.csv")))
        # zipf.write(csv_directory_metrics, arcname=os.path.join('data', os.path.basename(csv)))

        # Add JSON file to the root of the compressed file
        zipf.write(json_file, arcname=os.path.basename(os.path.join(imagesPath, params['modelName'] + '_information.json')))

        # Add PKL file to the root of the compressed file
        zipf.write(pkl_file, arcname=os.path.basename(os.path.join(imagesPath, params['modelName'] + '_model.h2o')))

    shutil.move(imagesPath + modelName + '_informationColumnsStringToNumber.json', imagesPath + 'experimental/' + modelName + '_informationColumnsStringToNumber.json' )

    shutil.make_archive(imagesPath + modelName, "tar", imagesPath)

    # Delete dataset
    os.remove(trainDataName)


def main(params):

    # try:
    loadingJSON(params['userName'], 3, 'Awaiting data, please be patient', False)
    time.sleep(2.0)

    loadingJSON(params['userName'], 10, 'Starting', False)
    time.sleep(2.0)

    doOtherModel(params)

    loadingJSON(params['userName'], 100, 'Ending', False)
    time.sleep(2.0);
    loadingJSON(params['userName'], 0, 'Awaiting data, please be patient', False)

    # except (RuntimeError, TypeError, NameError, EOFError):

    #     loadingJSON(params['userName'], 0, 'Awaiting data, please be patient', True)
    #     time.sleep(3.0);
    #     loadingJSON(params['userName'], 0, 'Awaiting data, please be patient', False)

    # except (h2o.exceptions.H2OResponseError):

    #     loadingJSON(params['userName'], 0, 'Error in algorithm, sorry, please try again, thanks', True)
    #     time.sleep(3.0);
    #     loadingJSON(params['userName'], 0, 'Awaiting data, please be patient', False)

    # except (h2o.exceptions.H2OError):

    #     loadingJSON(params['userName'], 0, 'Error in data input, sorry, please try again, thanks', True)
    #     time.sleep(3.0);
    #     loadingJSON(params['userName'], 0, 'Awaiting data, please be patient', False)
 
   

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
This method takes two arguments, aciertos and total
, which represent the number of hits and the total 
number of attempts made by a player, respectively. 
It calculates the percentage of hits by dividing the number 
of hits by the total number of attempts and multiplying by 100. The 
math.ceil() function is then used to round up the result to the
nearest integer. The resulting value is returned as the output of the method.
'''
def calculate_so_per_100_hits(aciertos, total):
    hits = (aciertos / total) * 100
    return math.ceil(hits)

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

 
if __name__ == "__main__":
    if (len(sys.argv) > 1):
        dataSysArgv = {'modelName'           : sys.argv[1],
                       'trainData'           : "../uploads/" + sys.argv[2],
                       'columnsIdPrediction' : sys.argv[3],
                       'split'               : sys.argv[4],
                       'seed'                : sys.argv[5],
                       'init'                : sys.argv[6],
                       'max_iteration'       : sys.argv[7],
                       'cluster_k'           : sys.argv[8],
                       'standardize'         : sys.argv[9],
                       'userName'            : sys.argv[10],
                       'deleteColumns'       : sys.argv[11],
                       'class_column'        : sys.argv[12],
                       'valuesClassColumn'   : sys.argv[13],
                       'valuesNan'           : sys.argv[14],
                       'advancedModel'       : sys.argv[15]   




                       }
                    #     dataSysArgv = {'modelName'           : sys.argv[1],
                    #    'trainData'           : "../uploads/" + sys.argv[2],
                    #    'Ids'                 : sys.argv[3],
                    #    'columnId'            : sys.argv[4],
                    #    'onrearId'            : sys.argv[5],
                    #    'directionId'         : sys.argv[6],
                    #    'columnsIdPrediction' : sys.argv[7],
                    #    'split'               : sys.argv[8],
                    #    'seed'                : sys.argv[9],
                    #    'init'                : sys.argv[10],
                    #    'max_iteration'       : sys.argv[11],
                    #    'cluster_k'           : sys.argv[12],
                    #    'standardize'         : sys.argv[13],
                    #    'userName'            : sys.argv[14]

                    #    }
        print("Using parameters from SysArgV {} )".format(dataSysArgv)) 
        main(dataSysArgv)
    else:
        print("\n\n\nThis script requires some arguments introduced by the command line" + \
              "\n [datasetsPath, trainingDatasetName, TestingDatasetName, NoOfErros]\n\n")
        
        FACTORY_PARAMS = {'modelName'                   : "TabberMachine_Anomalies",
                          'trainData'                   : r"C:\Users\spokk\Desktop\datasets\Cylinders20181119_v3.csv",
                          'columnId'                    : "4_3",
                          'Ids'                         : "ID_CYL",
                          'onrearId'                    : "Movement_Type",
                          'directionId'                 : "A",
                          'columnsIdPrediction'         : "Cycle_Start_Mean,Cycle_Start_Desv,Cycle_Start_Max,Cycle_Start_Min",
                          'split'                       : "75",
                          'seed'                        : "2",
                          'init'                        : "PlusPlus",
                          'max_iteration'               : "5",
                          'cluster_k'                   : "4",
                          'standardize'                 : "True",
                          'valuesNan'                   : 'mean',

                          }
                        
        print("Using parameters from dataHeart")
        main(FACTORY_PARAMS)            
