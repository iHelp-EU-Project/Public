# -*- coding: utf-8 -*-
"""
Created on Wed May 18 13:59:53 2022

@author: spokk
"""

import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from sklearn.model_selection import train_test_split
import xgboost as xgb
#np.random.seed(0)
from sklearn.model_selection import train_test_split
from sklearn import preprocessing
from sklearn.ensemble import RandomForestRegressor
import h2o
from h2o.estimators import H2OKMeansEstimator
from h2o.automl import H2OAutoML
from h2o.estimators import H2OGradientBoostingEstimator
import sys
import os
import math as math
import zipfile
import shutil
import json
import time
import seaborn as sns
import copy
import shap
import cv2
from PIL import Image
import glob
from IPython.core.display import display 
import matplotlib

from plotly.offline import download_plotlyjs, init_notebook_mode, plot, iplot
sys.path.append('../shapPlotsDynamic')
# from dynamic.dynamicShapPlots import summary_plot_plotly_fig as sum_plot
import shapPlotsDynamic.dynamicShapPlots as sum_plot 
import shapPlotsDynamic.shap_plots as plots 

import warnings
warnings.filterwarnings('ignore')
matplotlib.use('Agg')

class ShapClassPlots(object):

    def __init__(self, modelName, user, datafile,deleteColumns, columnsIdPrediction, class_column,trainData, cancer):
        import shap
        plt.ion()
        deleteColumns = deleteColumns #'sample_id,patient_cohort,sample_origin'
        columnsIdPrediction = columnsIdPrediction#'age,sex,stage,plasma_CA19_9,creatinine,REG1B,LYVE1,REG1A,TFF1'#'creatinine,REG1B'
        class_column = class_column #'diagnosis'
        columnsItems = []
        classCancer = cancer
        imagesPath = os.getcwd() + "/imagesup/" + user +'/' + modelName +'/'
        modelPath  = os.getcwd() + "/imagesup/" +  user +'/' + modelName +'/'
        csvPath = os.getcwd() + "/imagesup/" +  user +'/' + modelName +'/'
    
        try:
            os.mkdir(imagesPath + "shap_plots")
        except OSError as e:
            print (e)
        # h2o.init()
        # load JS visualization code to notebook
        # shap.initjs()

        df = trainData.as_data_frame(use_pandas=True,header=True);



        df[class_column] = df[class_column].astype("category")


        # features = ['fixed acidity', 'volatile acidity', 
        #             'citric acid', 'residual sugar', 'chlorides', 
        #             'free sulfur dioxide', 'total sulfur dioxide', 
        #             'density', 'pH', 'sulphates', 'alcohol']
        #classes = 'quality'
        print(columnsIdPrediction)
        features = columnsIdPrediction.split(',') # ['age', 'sex', 'stage', 'benign_sample_diagnosis', 'plasma_CA19_9', 'creatinine', 'LYVE1', 'REG1B', 'TFF1', 'REG1A']
        print('#############################################')
        print(features)
        print('#############################################')
        classes = class_column
        Y = df[classes]
        X =  df[features]

        df_shap_dynamic = df[features]
        df_shap_dynamic[classes] = df[classes]

        print('########################', df_shap_dynamic)

        
        X_train, X_test, Y_train, Y_test = train_test_split(X, Y, test_size = 0.2, random_state = 1234)
        xgb_model = xgb.XGBRegressor(random_state=42)
        xgb_model.fit(X_train, Y_train)

        # The SHAP Values
        explainer = shap.Explainer(xgb_model)
        shap_values = explainer(X_test)
        f = plt.figure()
        shap.plots.bar(shap_values, max_display=10, show=False) # default is max_display=12
        f.savefig(imagesPath + "shap_plots/Mean_SHAP.png", bbox_inches='tight', dpi=600)

        f = plt.figure()
        shap.summary_plot(shap_values, X_test,  plot_type='violin', show=False)
        f.savefig(imagesPath + "shap_plots/summary_plot_violin.png", bbox_inches='tight', dpi=600)

        f = plt.figure()
        shap.summary_plot(shap_values, X_test,  plot_type='dot', show=False)
        f.savefig(imagesPath + "shap_plots/summary_plot_dot.png", bbox_inches='tight', dpi=600)

       
        if len(features) < 10:
            # plotly_fig = plots.shap_summary_plot(shap_values, X_test ,feature_names=features ,max_display=20)  #sum_plot.summary_plot_plotly_fig(df, target=classes)
            plotly_fig = sum_plot.summary_plot_plotly_fig(df_shap_dynamic, target=classes, max_display = 40)#####################################################
            # plotly_fig = sum_plot.summary_plot_plotly_fig(shap_values,df_shap_dynamic, target=features)
            # # plt.show()
            init_notebook_mode(connected=True)
            iplot(plotly_fig, show_link=False)############################################################################       
            # To save the figure:
            plot(plotly_fig, show_link=False, filename=imagesPath + "shap_plots/summary_plot_dot.html", auto_open=False)########################################

        # shap.plots.bar(shap_values.cohorts(0).abs.mean(0))
        print(len(shap_values))
        f = plt.figure()
        shap.plots.heatmap(shap_values[1:100], show=False)
        f.savefig(imagesPath + "shap_plots/heatmap.png", bbox_inches='tight', dpi=600)


        # shap.plots.heatmap(shap_values[101:208])
        f = plt.figure()
        shap.plots.waterfall(shap_values[1]) # For the first observation
        f.savefig(imagesPath + "shap_plots/waterfall.png", bbox_inches='tight', dpi=600)

        # for idx,sv in enumerate(shap_values):
        #     f = plt.figure()
        #     shap.plots.bar(sv, max_display=10, show=False) # For the first observation
        #     f.savefig(imagesPath + "shap_plots/bar_" + str(idx)  + ".png", bbox_inches='tight', dpi=600)

        forcePlot = []
        explainer = shap.TreeExplainer(xgb_model)
        shap_valuesd = explainer.shap_values(X_test)
        shap.initjs()
        def p(j):
            forcePlot.append("bar_Force_"+ str(j) + ".png")
            return(shap.force_plot(explainer.expected_value, shap_valuesd[j,:], X_test.iloc[j,:],show=False, matplotlib=True).savefig(imagesPath + "shap_plots/bar_Force_"+ str(j) + ".png", bbox_inches='tight', dpi=100))

        num_pictures = 0
        if len(shap_valuesd) >= 150:
            num_pictures = 150
        else:
            num_pictures = len(shap_valuesd)

        # for i in range(len(shap_valuesd)):
        for i in range(num_pictures):#len(shap_valuesd)):
           p(i)



        print(forcePlot)
       
        cols = int(len(shap_values)/3)
        rows = int(len(shap_values)/3)
        i = 0
        
        input_images_path = imagesPath +  'shap_plots/'
        files_names = os.listdir(input_images_path)
        # print(files_names)
        img = []


        for file_name in files_names:
            #print(file_name)
           
            if 'bar_Force_' in file_name:
                image_path = input_images_path + "/" + file_name
                # print(image_path)
                image=Image.open(image_path)# Read image
                # image=image.resize((200,200), Image.ANTIALIAS)# Resize image
                # image.save(file_name , 'PNG', quality=90)
                # image=Image.open(image_path)# Read image with new size
                
                # cv2.namedWindow("output", cv2.WINDOW_NORMAL)    # Create window with freedom of dimensions
                # image = cv2.imread(image_path)                  # Read image
                # imS = cv2.resize(image, (14400, 9000))            # Resize image
                # f = str(file_name)
                # print(f)
                img.append(image)#add image
                #os.remove(image_path)#delete

           
        numberImages = 5
        if len(img) < 5:
            numberImages = len(img)
            
        fig, axs = plt.subplots(nrows=numberImages, ncols=1,figsize=(12,25))
        fig.subplots_adjust(hspace=0.4, wspace=0.4)
        for idx, ax in enumerate(axs):
            ax.imshow(img[idx])
            ax.axis('off')
            # fig.add_subplot(rows,3,i+1, ax)    # the number of images in the grid is 5*5 (25)
            # i += 1
            



        fig.savefig(imagesPath + "shap_plots/bar_Force.png", dpi=1000)
        Image.MAX_IMAGE_PIXELS = None
        image=Image.open(imagesPath + "shap_plots/bar_Force.png")
        image=image.resize((200,200), Image.ANTIALIAS)
        image.save(file_name , 'PNG', quality=90)
        plt.close(fig)





        fig, ax = plt.subplots(nrows=1, ncols=len(features),figsize=(64,12))
        fig.subplots_adjust(hspace=0.4, wspace=0.4)

        
        for idx,column in enumerate(features):
            #SHAP scatter plots
            shap.plots.scatter(shap_values[:,column],ax=ax[idx],color=shap_values,show=False)
    

        fig.savefig(imagesPath + "shap_plots/weight.png", bbox_inches='tight', dpi=600)
        plt.close(fig)

      
        f = plt.figure()
        expected_value = explainer.expected_value
      

        f = plt.figure()
        shap.plots.bar(shap_values.cohorts(4).abs.mean(0),show=False)

        fig = plt.gcf() # gcf means "get current figure"
        fig.set_figheight(11)
        fig.set_figwidth(9)
        #plt.rcParams['font.size'] = '12'
        ax = plt.gca() #gca means "get current axes"
        leg = ax.legend(bbox_to_anchor=(0., 1.02, 1., .102))
        for l in leg.get_texts(): l.set_text(l.get_text().replace('Class', 'Klasse'))
        #plt.show()
        plt.savefig(imagesPath + "shap_plots/legend.png", bbox_inches='tight', dpi=600)
        plt.close()


       


        shap_values = explainer.shap_values(X_test)
        fig = plt.figure(figsize=(256,125))
        fig.subplots_adjust(hspace=0.4, wspace=0.4)
        fig2= plt.figure(figsize=(15,10))
        decisionPlot = []

       
        cols = int(len(shap_values)/3)
        rows = int(len(shap_values)/2) + 1
        # fig_logic, ax = plt.subplots(nrows=1, ncols=6,figsize=(64,12))
        print(cols, rows)
        for idx, shpValues in enumerate(shap_values[0:50]):
            fig2= plt.figure(figsize=(15,10))
            n = 1 + idx
            fig.add_subplot(5,cols,n)
            shap.decision_plot(expected_value, shpValues, X_test, show=False, title='The Observation #'  + str(idx) ,auto_size_plot=False)
            decisionPlot.append('Observation_'  + str(idx) + ".png")
            fig2.savefig(imagesPath + "shap_plots/Observation_"+ str(idx) +".png", dpi=100)
            plt.close(fig2)
            fig.tight_layout()
            # if idx > 50:
            #     break
          


        fig.savefig(imagesPath + "shap_plots/Observation.png")
        plt.close(fig)

        print(decisionPlot) 

        f = plt.figure()
        shap.decision_plot(expected_value, shap_values[0:50],features,link='logit')
        f.savefig(imagesPath + "shap_plots/decision_plot_all.png", bbox_inches='tight', dpi=600)


        f = plt.figure()
        shap.decision_plot(expected_value, shap_values[0:50],features)
        f.savefig(imagesPath + "shap_plots/decision_plot_all_n.png", bbox_inches='tight', dpi=600)


        print(df[classes])
        # Multiclass
        df['Multiclass'] = df[classes]#np.where(df[classes]>1, 'Best', np.where(df[classes]>0, 'Premium','Value'))# 2 = 'Best', 1 = 'Premium', 0 = 'Value'
        Y = df['Multiclass']
        X = df[features]
        X_train, X_test, Y_train, Y_test = train_test_split(X, Y, test_size = 0.2, random_state = 1234)
        class_columnArray = str(classCancer).split(',')

        objective = "multi:softprob"
        if len(class_columnArray) == 2:
            objective = "binary:logistic"

        xgb_model = xgb.XGBClassifier(objective=objective, random_state=42)
        xgb_model.fit(X_train, Y_train)

        multiclass_actual_pred = pd.DataFrame(xgb_model.predict_proba(X_test)).round(2)
        multiclass_actual_pred['Actual'] = Y_test.values
        multiclass_actual_pred['Pred'] = xgb_model.predict(X_test)
        cancer = cancer.split(',')
        cancer.append('Pred')
        cancer.append('Actual')
        multiclass_actual_pred.columns = cancer #['2 - pancreatic ductal adenocarcinoma','1 - benign hepatobilliary disease','0 - no pancreatic disease','Pred','Actual']
        multiclass_actual_pred.head()

        pd.crosstab(multiclass_actual_pred['Actual'],multiclass_actual_pred['Pred'])

        import shap
        string_Title_Label_Multiclass= 'The Summary Plot for the Multiclass Model'+'\n'
        
        for idx, class_c in enumerate(class_columnArray):
            string_Title_Label_Multiclass += ' Class ' + str(idx) + ' - ' + str(class_c) + ', ';  


        explainer = shap.TreeExplainer(xgb_model)
        shap_values = explainer.shap_values(X_test,approximate=True)
        plt.figure()
        # plt.title('The Summary Plot for the Multiclass Model'+'\n'+'Class 2 - pancreatic ductal adenocarcinoma, Class 1 - benign hepatobilliary disease, Class 0 - no pancreatic disease')
        plt.title(string_Title_Label_Multiclass, fontdict={'fontsize': 8})
        shap.summary_plot(shap_values, X_test, plot_type="bar", show=False, max_display=X_test.shape[1])
        # get the current axis
        ax = plt.gca()
        # add the legend
        ax.legend(bbox_to_anchor=(1.05, 1), loc=2, borderaxespad=0.)
        plt.savefig(imagesPath + "shap_plots/Multiclass.png", bbox_inches='tight', dpi=600)
        plt.close()



       
        np.shape(shap_values) #  classes, observations, variables 
        plot_type = "bar"
        fig = plt.figure(figsize=(20,10))

        if len(class_columnArray) > 2:
            for idx, class_c in enumerate(class_columnArray[:2]):   
                ax = fig.add_subplot(131 + idx) 
                ax.title.set_text('Class ' + str(idx) + ' - ' + str(class_c))
                ax.title.set_fontsize(7)  # set font size to 20
                shap.summary_plot(shap_values[idx], X_test, plot_type=plot_type, show=False)
                ax.set_xlabel(r'SHAP values', fontsize=11)
                plt.subplots_adjust(wspace = 5)



        # ax1 = fig.add_subplot(132)
        # ax1.title.set_text('C1 - benign hepatobilliary disease')
        # shap.summary_plot(shap_values[1], X_test, plot_type="bar", show=False)
        # plt.subplots_adjust(wspace = 5)
        # ax1.set_xlabel(r'SHAP values', fontsize=11)

        # ax2 = fig.add_subplot(133)
        # ax2.title.set_text('C0 -  no pancreatic disease')
        # shap.summary_plot(shap_values[0], X_test, plot_type="bar", show=False)
        # ax2.set_xlabel(r'SHAP values', fontsize=11)

        # plt.tight_layout(pad=3) # You can also use plt.tight_layout() instead of using plt.subplots_adjust() to add space between plots
        #plt.show()

        plt.savefig(imagesPath + "shap_plots/Shap_Multiclass.png", bbox_inches='tight', dpi=600)
        plt.close('all')

        nameColumnsObject = { 'Observation': [], 'BarForce' : [] } #Object save information about columns

        nameColumnsObject['Observation'] = decisionPlot
        nameColumnsObject['BarForce'] = forcePlot





        #Save json file
        with open(os.path.join(imagesPath + 'shap_plots/', modelName + '_shapValues.json'),"w") as fp:
                json.dump(nameColumnsObject, fp)

    def __str__(self):
        msg = "end"
                   
        return msg 
