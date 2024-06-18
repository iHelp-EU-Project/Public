'''
@version 26.07.19
@author Armando Aguayo Mendoza, armando.aguayo@informationcatalyst.com
@author Jose Gil Lopez, jose.gil@informationcatalyst.com

'''
import os 
import sys

import matplotlib

import matplotlib.pyplot as plt
import numpy as np
import seaborn as sns

import json
import time


matplotlib.use('Agg')



def doHeatPlot(corrMatrix,imagesPath):
    '''
    This is an Axes-level function and will draw the heatmap into the currently-active Axes
    This will draw a correlation matrix
    '''
    # Generate a mask for the upper triangle
    mask = np.zeros_like(corrMatrix, dtype=np.bool)
    mask[np.triu_indices_from(mask)] = True
    
    cmap = sns.diverging_palette(10, 220, sep=80, n=7)
        
    # Draw the heatmap with the mask and correct aspect ratio
    sns.heatmap(corrMatrix, 
                mask=mask, 
                cmap=cmap, 
                vmax=1, 
                center=0,
                square=True, 
                linewidths=.5, 
                cbar_kws = {"shrink": .5} )
        
    # Add title and axis names
    plt.title("Correlation matrix")#  +os.getcwd())
    plt.xlabel('Machine sensors')
    plt.ylabel('Machine sensors')
        
    
    # Saving the graphic, if you use show() before the file will be saved empty
    plt.savefig(imagesPath + "heatmap.png")
    plt.close()




def doPairplot(panda_trainData,imagesPath):
    '''
    Create a grid of Axes such that each variable in the frame
    The diagonal Axes are treated differently, 
    drawing a plot to show the univariate distribution of the data 
    for the variable in that column
    '''
    sns.set(style="ticks", color_codes=True)
    sns.pairplot( panda_trainData, 
                  kind="reg", 
                  dropna=True, 
                  aspect=.8, 
                  height=2, 
                  diag_kind="kde");
        

        
    # Saving the graphic, if you use show() before the file will be saved empty
    plt.savefig(imagesPath + "pairplot.png")
    plt.close()





def main(params):
    import h2o
    h2o.init()

    try: 
        loadingJSON(params['userName'], 3, 'Awaiting data, please be patient', False)
        time.sleep(2.0)

        loadingJSON(params['userName'], 10, 'Starting', False)
        time.sleep(2.0)

        # PAth save files and images
        csvPath = os.getcwd() + "/imagesup/"  + params['userName'] +'/' + params['modelName'] +'/'
        #Remove old files
        if(os.path.isfile(csvPath + params['modelName'] + "_data_matrix.csv")):
                os.remove(csvPath + params['modelName'] + "_data_matrix.csv")
                        
        if(os.path.isfile(csvPath + params['modelName'] + "_data_prediction.csv")):
                os.remove(csvPath + params['modelName'] + "_data_prediction.csv")
                
        if(os.path.isfile(csvPath + params['modelName'] + "_data_regresion.csv")):
                os.remove(csvPath + params['modelName'] + "_data_regresion.csv")
                
        if(os.path.isfile(csvPath + params['modelName'] + "_dataGLMBinoMatizConfusionPandas.csv")):
                os.remove(csvPath + params['modelName'] + "_dataGLMBinoMatizConfusionPandas.csv")
        
        
        #Importing the dataset for training
        trainData = h2o.import_file( params['dataFile'] )
        
        phase1 = trainData

        loadingJSON(params['userName'], 60, 'Building dataset', False)
        time.sleep(2.0);

        #Control int or string
        if(have_(params['columnId'])):
            columnID = params['columnId'] 
        else:
            try: 
                columnID = int(params['columnId'])
            except ValueError:
                columnID = params['columnId']    
        
        #Select data propierties choose users    
        work = phase1[phase1[params['Ids'] ] == columnID]
        
        
        #Control int or string
        if(have_(params['directionId'])):
            directionID = params['directionId'] 
        else:    
            try: 
                directionID = int(params['directionId'])
            except ValueError:
                directionID = params['directionId']    
        
        #Select data propierties choose users    
        work_final = work[work[params['onrearId']] == directionID ]
    
        # Select correct columns for file csv D3.js Graphics 
        work_delete_ids =work_final.drop(params['Ids'])
        work_delete_onrearId = work_delete_ids.drop(params['onrearId'])
        loadingJSON(params['userName'], 75, 'Building data chooseen users', False)
        time.sleep(2.0)    


        h2o.export_file(work_delete_onrearId, path = csvPath + params['modelName'] + "_data_regresion.csv" ,force=True)
        
        loadingJSON(params['userName'], 85, 'Building data regression', False)
        time.sleep(2.0)    
        print(work_delete_onrearId)
    
        
        print("Correlation Matrix: ")

        loadingJSON(params['userName'], 100, 'Ending', False)
        time.sleep(2.0);
        loadingJSON(params['userName'], 0, 'Awaiting data, please be patient', False)

    except (RuntimeError, TypeError, NameError, EOFError):

        loadingJSON(params['userName'], 0, 'Awaiting data, please be patient', True)
        time.sleep(3.0);
        loadingJSON(params['userName'], 0, 'Awaiting data, please be patient', False)

    except (h2o.exceptions.H2OResponseError):

        loadingJSON(params['userName'], 0, 'Error in algorithm, sorry, please try again, thanks', True)
        time.sleep(3.0);
        loadingJSON(params['userName'], 0, 'Awaiting data, please be patient', False)

    except (h2o.exceptions.H2OError):

        loadingJSON(params['userName'], 0, 'Error in data input, sorry, please try again, thanks', True)
        time.sleep(3.0);
        loadingJSON(params['userName'], 0, 'Awaiting data, please be patient', False)

# Function to know if they are "integers" or "strings" return bool  
def have_(numero):
    numero = str(numero)
    primer_indice = numero.find("_")
    if primer_indice is -1:
        return False
    return primer_indice is numero.rfind("_")  

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
 
if __name__ == '__main__':
    if (len(sys.argv) > 1):
        dataSysArgv = {'modelName' : sys.argv[1],
                       'dataFile' : "../uploads/" + sys.argv[2],
                       'Ids' : sys.argv[3],
                       'columnId'       : sys.argv[4],
                       'onrearId'  : sys.argv[5],
                       'directionId' : sys.argv[6],
                       'userName'    : sys.argv[7]
                        }
        main(dataSysArgv)
    else:
        print("\n\n\nThis script requires some arguments introduced by the command line" + \
              "\n [datasetsPath, trainingDatasetName, TestingDatasetName, NoOfErros]\n\n")
        
        # values for testing
        PARAMS = {'modelName': "TabberMachine_Anomalies",
                  'dataFile': r"C:\Users\spokk\Desktop\datasets\Cylinders20181119_v3.csv", 
                  'columnId' : "4_3",
                  'Ids'      : "ID_CYL",
                  'onrearId' : "Movement_Type",
                  'directionId' : "A"}
        main(PARAMS)