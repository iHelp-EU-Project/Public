# -*- coding: utf-8 -*-
"""
Created on Wed Jan 26 09:49:10 2022

@author: spokk

ward
"""

import numpy as np
import matplotlib.pyplot as plt
import pandas as pd
import sys


from sklearn.preprocessing import StandardScaler,MinMaxScaler
from sklearn.cluster import KMeans
from sklearn import metrics
from scipy.spatial.distance import cdist,pdist

import pickle


from sklearn import datasets

def main(params):

    data = datasets.load_diabetes()



    Clustering_data = data["data"]
    #Clustering_data

    X = MinMaxScaler().fit_transform(Clustering_data)
    #X
    y=data['target']

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
    plt.show()


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
    plt.show()
    '''
    Single
    '''
    type(Clustering_data)


    from scipy.cluster.hierarchy import dendrogram, linkage, cophenet

    linked1 = linkage(X, 'single')

    labelList = range(len(X))

    plt.figure(figsize=(64, 32))  
    dendrogram(linked1,
            labels = labelList,
            distance_sort = 'descending',
            show_leaf_counts = True)
    plt.show()  


    c1=cophenet(linked1,pdist(X))
    c1

    # save the model
    pickle.dump(model, open("Single_model.pkl", "wb"))

    '''


    Complete

    '''

    from scipy.cluster.hierarchy import dendrogram, linkage

    linked2 = linkage(X, 'complete')

    labelList = range(len(X))

    plt.figure(figsize=(64, 32))  
    dendrogram(linked2,
            labels = labelList,
            distance_sort = 'descending',
            show_leaf_counts = True)
    plt.show()  

    c2=cophenet(linked2,pdist(X))
    c2

    from sklearn.cluster import AgglomerativeClustering
    clusters=[]
    cluster = AgglomerativeClustering(n_clusters=5, affinity='euclidean', linkage='complete')  
    clusters.append(cluster.fit_predict(X)) 
    plt.figure(figsize=(10, 7))  
    plt.scatter(X[:,4], X[:,9], c=cluster.labels_, cmap='rainbow') 
    plt.show()

    # save the model
    pickle.dump(cluster, open("Complete_model.pkl", "wb"))

    '''
    Wards
    '''

    from scipy.cluster.hierarchy import dendrogram, linkage,cophenet

    linked3 = linkage(X, 'ward')

    labelList = range(len(X))

    plt.figure(figsize=(64, 32))  
    dendrogram(linked3,
            labels = labelList,
            distance_sort = 'descending',
            show_leaf_counts = True)
    plt.show()  


    c3=cophenet(linked3,pdist(X))
    c3

    from sklearn.cluster import AgglomerativeClustering
    clusters=[]
    cluster = AgglomerativeClustering(n_clusters=3, affinity='euclidean', linkage='ward')  
    clusters.append(cluster.fit_predict(X)) 
    plt.figure(figsize=(10, 7))  
    plt.scatter(X[:,4], X[:,9], c=cluster.labels_, cmap='rainbow') 
    plt.show()
    # save the model

    pickle.dump(cluster, open("Wards_model.pkl", "wb"))

    '''

    Average

    '''


    from scipy.cluster.hierarchy import dendrogram, linkage

    linked4 = linkage(X, 'average')

    labelList = range(len(X))

    plt.figure(figsize=(64, 32))  
    dendrogram(linked4,
            labels = labelList,
            distance_sort = 'descending',
            show_leaf_counts = True)
    max_d=100
    plt.show()  


    c4=cophenet(linked4,pdist(X))
    c4

    from sklearn.cluster import AgglomerativeClustering
    clusters=[]
    cluster = AgglomerativeClustering(n_clusters=7, affinity='euclidean', linkage='average')  
    clusters.append(cluster.fit_predict(X)) 
    plt.figure(figsize=(10, 7))  
    plt.scatter(X[:,4], X[:,9], c=cluster.labels_, cmap='rainbow') 
    plt.show()

    # save the model
    import pickle
    pickle.dump(cluster, open("Average_model.pkl", "wb"))



if __name__ == "__main__":
    if (len(sys.argv) > 1):
        dataSysArgv = {'modelName'          : sys.argv[1],
                       'userName'           : sys.argv[2],
                       'pathClasses'        : sys.argv[3],
                      
                       }
                   
        print("Using parameters from SysArgV {} )".format(dataSysArgv)) 
        main(dataSysArgv)
