'''
@version 26.07.19
@author Armando Aguayo Mendoza, armando.aguayo@informationcatalyst.com
@author Jose Gil Lopez, jose.gil@informationcatalyst.com
'''
import sys

import matplotlib
import json
import time
import os


matplotlib.use('Agg')

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
 

def main(params):
    import h2o
    h2o.init()
    try:
    
        loadingJSON(params['userName'], 3, 'Awaiting data, please be patient', False);
        time.sleep(2.0);
        loadingJSON(params['userName'], 10, 'Starting', False);
        time.sleep(2.0);
        #Importing the dataset for training
        trainData = h2o.import_file( params['trainData'] )
    
    
        phase1 = trainData
        
        #Control int or string
        if(have_(params['columnId'])):
            columnID = params['columnId'] 
        else:
            try: 
                columnID = float(params['columnId'])
            except ValueError:
                columnID = params['columnId']    
        
        
        work = phase1[phase1[params['Ids'] ] == columnID]    
        work_delete_column = work.drop(params['Ids']) 
    
        loadingJSON(params['userName'], 60, 'Building dataset', False);
        time.sleep(2.0);
        print(work_delete_column)
        
        print("END-TESTING")
        
        print(work_delete_column)
        
        print("END-TRAIN")
        
        loadingJSON(params['userName'], 100, 'Ending', False);
        time.sleep(2.0);
        loadingJSON(params['userName'], 0, 'Awaiting data, please be patient', False);

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
    
   
    
def have_(numero):
    numero = str(numero)
    primer_indice = numero.find("_")
    if primer_indice is -1:
        return False
    return primer_indice is numero.rfind("_")      
  
    
if __name__ == '__main__':
    if (len(sys.argv) > 1):
        dataSysArgv = {'modelName' : sys.argv[1],
                       'trainData' : "../uploads/" + sys.argv[2],
                       'machineName': sys.argv[3],
                       'Ids'        : sys.argv[4],
                       'columnId' : sys.argv[5] ,
                       'userName' : sys.argv[6]
                        }
        main(dataSysArgv)
    else:
        print("\n\n\nThis script requires some arguments introduced by the command line" + \
              "\n [datasetsPath, trainingDatasetName, TestingDatasetName, NoOfErros]\n\n")
        
        # values for testing
        PARAMS = {'modelName'   : "TabberMachine_Anomalies",
                  'trainData'   : "C:\\Users\\spokk\\Desktop\\datasets\\Cylinders20181119_v3.csv",
                  'machineName' : "TestOmprotDataSetIDsPePe",
                  'Ids'         : "ID_CYL",
                  'columnId'   : "4_3" }
        main(PARAMS)

