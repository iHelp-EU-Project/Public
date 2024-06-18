import { Injectable } from '@angular/core';
import { HttpClient, HttpRequest, HttpEvent, HttpHeaders, HttpParams } from '@angular/common/http';
import { BehaviorSubject, Subject, Observable, ReplaySubject } from 'rxjs';


import { User } from '../models/index_models';
import { finalize } from 'rxjs/operators';
import { KeycloakService } from 'keycloak-angular';



@Injectable({
 providedIn: 'root'
})
export class UserService {

    host: any;


    constructor(private http: HttpClient,
                private keycloakService: KeycloakService,
                ) {

         this.host = self.location.host.split(':')[0].toLocaleLowerCase();
    }

   

    create(user: User): any {
        const headers = this.accessHeaders();

        return this.http.post('/users/register/', user, {headers})
    //     .subscribe((req) => {
    //         console.log(req);
    //         return req;
    // })
    ;
    }

    createworkspace(user: User): any {
        const headers = this.accessHeaders();

        return this.http.post('/users/newworkspace/', user, {headers})
    //     .subscribe((req) => {
    //         console.log(req);
    //         return req;
    // })
    ;
    }

    createdirectorymodel(user: string, namemodel: string): any {
        const headers = this.accessHeaders();

        const modelName = {
            username: user,
            model: namemodel
        };
        return this.http.post('/users/createdirectorymodel/', modelName, {headers})
    //     .subscribe((req) => {
    //         console.log(req);
    //         return req;
    // })
    ;
    }
    update(id: string, user: User): any {
        const headers = this.accessHeaders();

        return this.http.put('/users/' + id, user, {headers})
    //     .subscribe((req) => {
    //         console.log(req);
    //         return req;
    // })
    ;
    }

    delete(id: string): any {
        const headers = this.accessHeaders();

        return this.http.delete('/users/' + id, {headers})
    //     .subscribe((req) => {
    //         console.log(req);
    //         return req;
    // })
    ;
    }

    deleteworkspace(user: User): any {
        const headers = this.accessHeaders();

        // user.password = 'fakePassword';
        return this.http.post('/users/deleteworkspace/', user, {headers})
    //     .subscribe((req) => {
    //         console.log(req);
    //         return req;
    // })
    ;
    }

    deleteModels(user: User, model: string): any {
        const headers = this.accessHeaders();

        const params = {
                username: user.username,
                modelsname: model
            };
        return this.http.post('/users/files/deleteModels/', params, {headers})
    //     .subscribe((req) => {
    //         console.log(req);
    //         return req;
    // })
    ;
    }

    testmysqldb(): any {
        const headers = this.accessHeaders();

        return this.http.get('/users/mysqltestdb/', {headers})
    //     .subscribe((req) => {
    //         console.log(req);
    //         return req;
    // })
    ;
    }

    readFilesJSONDirectory(user: User): Observable<string> {
        const headers = this.accessHeaders();

        return this.http.get('/users/readFilesJSONDirectory/', {
            responseType: 'text',
            params: {
                username: user.username
            }, headers
        });
    }

    downloadJSONFiles(user: User, model: string, pathdirectory: string): any {
        const headers = this.accessHeaders();

        return this.http.get('/users/downloadJSONFiles/', {
            responseType: 'text', params: {
                username: user.username,
                modelsname: model,
                directory: pathdirectory
            }, headers
        })
    //     .subscribe((req) => {
    //         console.log(req);
    //         return req;
    // })
    ;
    }

     readSettingsJSONFiles(user: User, model: string, pathdirectory: string): any {
        const headers = this.accessHeaders();

        return this.http.get('/users/readSettingsJSONFiles/', {
            responseType: 'text', params: {
                username: user.username,
                modelsname: model,
                directory: pathdirectory
            }, headers
        })
    //     .subscribe((req) => {
    //         console.log(req);
    //         return req;
    // })
    ;
    }

     readLoadingJSON(user: User, model: string, pathdirectory: string): any {
        const headers = this.accessHeaders();

        return this.http.get('/users/readLoadingJSON/', {
            responseType: 'text', params: {
                username: user.username,
                modelsname: model,
                directory: pathdirectory
            }, headers
        })
    //     .subscribe((req) => {
    //         console.log(req);
    //         return req;
    // })
    ;
    }

     readLoadingJSONValuesColumsToSelect(user: User, model: string, pathdirectory: string): any {
        const headers = this.accessHeaders();

        return this.http.get('/users/readLoadingJSONValuesColumsToSelect/', {
            responseType: 'text', params: {
                username: user.username,
                modelsname: model,
                directory: pathdirectory
            }, headers
        })
    //     .subscribe((req) => {
    //         console.log(req);
    //         return req;
    // })
    ;
    }


    writeSettingsJSONFiles(user: User, language: string, pathdirectory: string): any {
        const headers = this.accessHeaders();

        return this.http.get('/users/writeSettingsJSONFiles/', {
            responseType: 'text', params: {
                username: user.username,
                modelsname: language,
                directory: pathdirectory,
            }, headers
        })
    //     .subscribe((req) => {
    //         console.log(req);
    //         return req;
    // })
    ;
    }

    upload(form: FormData): any{
        return this.http.post('/users/upload/', form);
    }

    datasetCVSTOJSON(user: User, model: string, pathdirectory: string): any {

        const headers = this.accessHeaders();

        return this.http.get('/users/datasetCVSTOJSON/', {
            responseType: 'text', params: {
                username: user.username,
                modelsname: model,
                directory: pathdirectory
            }, headers
        })
    //     .subscribe((req) => {
    //         console.log(req);
    //         return req;
    // })
    ;
    }

    selectUniqueValues(user: User, model: string): any {
        const headers = this.accessHeaders();

        return this.http.get('/users/selectUniqueValues/', {
            responseType: 'text', params: {
                username: user.username, // user name
                filename: model, // file name
            }, headers
        })
    //     .subscribe((req) => {
    //         console.log(req);
    //         return req;
    // })
    ;
    }

    selectUniqueValuesPost(user: User, model: string, col: string[]): any {

        const headers = this.accessHeaders();

        return this.http.post('/users/selectUniqueValuesPost/', col, {
            responseType: 'json',
            params: {
                username: user.username, // user name
                filename: model, // file name

            }, headers
        });
    }

    uploadFiles(user: User, model: string, file: File): any {// Observable<HttpEvent<any>> {
        const formData: FormData = new FormData();

        formData.append('file', file);
        const headers = this.accessHeaders();


        return this.http.post('/users/filesupload', formData, {
            responseType: 'json',
            reportProgress: true,
            observe : 'events',
            params: {
                username: user.username,
                modelsname: model,
            }, headers });
  }

  getFiles(user: User, model: string, pathdirectory: string): Observable<any> {

    const params = new HttpParams();
    params.set('username', user.username);
    params.set('modelsname', model);
    params.set('directory', pathdirectory);
    const headers = this.accessHeaders();



    return this.http.get('/users/files/getListFiles/',
            {
            responseType: 'json', params: {
                username: user.username,
                modelsname: model,
                directory: pathdirectory
            }, headers });
  }

  uploadClasses(user: User, model: string, pathdirectory: string, files: string): any {

        const params = new HttpParams();
        params.set('username', user.username);
        params.set('modelsname', model);
        params.set('directory', pathdirectory);
        params.set('classfiles', files);
        const headers = this.accessHeaders();



        return this.http.get('/users/uploadClasses/',   {
        responseType: 'text',
        params: {
                username: user.username,
                modelsname: model,
                directory: pathdirectory,
                classfiles: files
            }, headers
        });


  }

   uploadFilesTest(user: User, model: string, pathdirectory: string, file: File): any {// Observable<HttpEvent<any>> {
        const formData: FormData = new FormData();

        formData.append('file', file);
        const headers = this.accessHeaders();


        return this.http.post('/users/files/uploadFilesTest/', formData, {
            responseType: 'json',
            reportProgress: true,
            observe : 'events',
            params: {
                username: user.username,
                modelsname: model,
                directory: pathdirectory
            }, headers });
  }

   uploadJSONClasses(user: User, model: string, pathdirectory: string): any {// Observable<HttpEvent<any>> {
        const headers = this.accessHeaders();

        return this.http.get('/users/files/uploadJSONClasses/', {
            responseType: 'json', params: {
                username: user.username,
                modelsname: model,
                directory: pathdirectory
            }, headers
        });
  }

    // download file JSON the leyend of the coluns changed string to number for have one best model
   getLeyendJSONColumnsObject(user: User, model: string): any {// Observable<HttpEvent<any>> {
        const headers = this.accessHeaders();

        return this.http.get('/users/files/getLeyendJSONColumnsObject/', {
            responseType: 'json', params: {
                username: user.username,
                modelsname: model,
            }, headers
        });
  }

   // Compress in zip file all directory models generated in train
   compressDirectoryModelforSendToModelManager(user: User, model: string): any {
        const headers = this.accessHeaders();

        return this.http.get('/users/files/compressDirectoryModelforSendToModelManager/', {
            responseType: 'text', params: {
                username: user.username,
                modelsname: model,
            }, headers
        });
  }

  // Get item classes of columns class in file object
   compressItemClases(user: User, model: string): any {
        const headers = this.accessHeaders();

        return this.http.get('/users/files/compressItemClases/', {
            responseType: 'json', params: {
                username: user.username,
                modelsname: model,
            }, headers
        });
  }

  // Get item classes of columns class in file object
   getFilesGeneratedPicturesOfScriptsAlgorithm(user: User, model: string): any {
        const headers = this.accessHeaders();

        return this.http.get('/users/files/getFilesGeneratedPicturesOfScriptsAlgorithm/', {
            responseType: 'json', params: {
                username: user.username,
                modelsname: model,
            }, headers
        });
  }

  //
    execDockerCompose(user: User, model: string, type: string): any {
        const headers = this.accessHeaders();

        return this.http.get('/users/files/execDockerCompose/', {
            responseType: 'text', params: {
                username: user.username,
                modelsname: model,
                directory: type

            }, headers
        });
    }
    // Login Autorization back-end python
    loginAutorization(user: User, isLoggedIn: string): any {
        const headers = this.accessHeaders();

        return this.http.get('/users/files/loginAutorization/', {
            responseType: 'text', params: {
                username: user.username,
                modelsname: isLoggedIn
            }, headers
        });
    }
    // Logout Autorization back-end python
    logoutAutorization(user: User, isLoggedIn: string): any {
        const headers = this.accessHeaders();

        return this.http.get('/users/files/logoutAutorization/', {
            responseType: 'text', params: {
                username: user.username,
                modelsname: isLoggedIn
            }, headers});

}

     // Upload files of inputs
    updateFiletoUploads(formData: FormData): any {
        const headers = this.accessHeaders();

        return this.http.post('/users/updateFiletoUploads/', formData, {
            responseType: 'json',
            reportProgress: true,
            observe : 'events',
            headers })
            .subscribe((req) => {
            console.log('ArchivoUser: ' + req);
            return req
            ;
       }
       );

    }

    // public getTokenKeyCloak() 
    // {
    //     let keycloakUrl='https://keycloak.ihelp-project.eu/realms/ihelp/protocol/openid-connect/token';
    //     let headers = new HttpHeaders();
    //     headers=headers.append('Content-Type','application/x-www-form-urlencoded');
    //     const params = new HttpParams()
    //     .append('client_id', 'ihelp')
    //     .append('username', 'pepe45')
    //     .append('password', 'pepe45')
    //     .append('grant_type', 'password');
    //     let response=this.http.post<any>(keycloakUrl,params, {headers:headers});
    //     return response;
    // }

    

    accessHeaders(): HttpHeaders{
        const headers = new HttpHeaders();
        // this is the important step. You need to set content type as null
        headers.set('Content-Type', 'multipart/form-data; boundary=<calculated when request is sent>');
        headers.set('Accept', '*/*');
        headers.set('Access-Control-Allow-Origin', '*');
        headers.set('Access-Control-Allow-Headers', 'Authorization, X-API-KEY, Origin, X-Requested-With, Content-Type, Accept, Access-Control-Allow-Request-Method');
        headers.set('Access-Control-Allow-Methods', 'GET, POST, OPTIONS, PUT, DELETE');
        headers.set('Allow', 'GET, POST, OPTIONS, PUT, DELETE');

       
       

        return headers;
    }

    uploadFilesGAN(user: User, model: string, className: string, file: File): any {// Observable<HttpEvent<any>> {
        const formData: FormData = new FormData();

        formData.append('file', file);
        const headers = this.accessHeaders();


        return this.http.post('/users/files/uploadFilesGAN/', formData, {
            responseType: 'json',
            reportProgress: true,
            observe : 'events',
            params: {
                username: user.username,
                modelsname: model,
                classname: className
            }, headers });
  }

  compressFilesGAN(user: User, model: string, pathdirectory: string): any {

    const params = new HttpParams();
    params.set('username', user.username);
    params.set('modelsname', model);
    params.set('directory', pathdirectory);
    const headers = this.accessHeaders();



    return this.http.get('/users/files/compressFilesGAN/',   {
    responseType: 'text',
    params: {
            username: user.username,
            modelsname: model,
            directory: pathdirectory,
        }, headers
    });


}


}
