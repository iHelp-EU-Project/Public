import h2o
from h2o.estimators.kmeans import H2OKMeansEstimator
import sys

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
import json
import copy
import numpy as np


# h2o.init()



df = pd.read_csv("../../uploads/pancreatic-cancer.csv",  na_values = ['no info', '.'])
print(df.columns)
df = df.iloc[: , 1:]
print(df.columns)
nameColumnsObject = { 'columns': '' , 'list' : [] }
columnsItems = []

#Save all the original  information the items and columns in LSON file for deploy model
for x in df.columns:
    if df[x].dtypes == 'object':# just if items of columns is object
        df[x] = df[x].replace(np.nan, 'none', regex=True)    
        df.dropna(subset=[x], inplace=True)# clean null and NaN        
        nameColumnsObject['columns'] = x #save name column 
        nameColumnsObject['list'] =  df[x].unique().tolist()#save unique values of column
        columnsItems.append(copy.copy(nameColumnsObject))    
        #print(nameColumnsObject)
        #print(x +':', df[x].dtypes,',List: ' , df[x].unique().tolist())
    else:
        df.dropna(subset=[x], inplace=True)# clean null and NaN        

    
#Save json file
with open(os.path.join('.', 'model.json'),"w") as fp:
        json.dump(columnsItems, fp)
    
print(df.T)

df = df.replace(np.nan, 'none', regex=True)

#Change strings columns we can assign numbers like ‘0’ and ‘1’ respectively so that our algorithms can work on the data.
for x in df.columns:
    if df[x].dtypes == 'object':# just if items of columns is object
        for idx ,val in enumerate(df[x].unique().tolist()):
            print(idx, val)
            df[x][df[x] == val] = idx  # change the strings per numbers to each column
            
print(df.T)

# hdf = h2o.H2OFrame(df)

# hdf.asnumeric()

# print(hdf.columns_by_type('categorical'))