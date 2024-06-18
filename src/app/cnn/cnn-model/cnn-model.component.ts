import { Component, OnInit, Input, ViewChild, AfterViewInit } from '@angular/core';
// import { UploadFileComponent } from '../../uploadfile/uploadfile.component';
import { HttpClient, HttpEventType, HttpResponse } from '@angular/common/http';
// import { Observable } from 'rxjs';
import { ToastrService } from 'ngx-toastr';
import { UserService } from '../../services/index_services';
import {BackendscriptsService} from '../../services/backendscripts.service';
import { User } from '../../models/index_models';
import { ActivatedRoute } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { TranslateService } from '@ngx-translate/core';
import { LanguageApp } from '../../services/LanguageApp';
import { TranslationService } from '../../services/translation.service';
import { text } from 'body-parser';
import { KeycloakService } from 'keycloak-angular';
import { KeycloakProfile } from 'keycloak-js';

@Component({
  selector: 'app-cnn-model',
  templateUrl: './cnn-model.component.html',
  styleUrls: ['./cnn-model.component.css']
})
export class CnnModelComponent implements OnInit, AfterViewInit {

  @Input() modelName: string;
  @Input() dateModelCreateModels: string;
  @Input() classes: { id: string, files: string }[];
  @Input() layerConvolution: number;
  @Input() layer: number;


  currentUser: User;
  host: any;
  files: any[] = [];
  message = '';
  currentFile?: File;
  viewFiles = false;
  loadImages = false;
  workflow = true;
  nameClases = '';
  viewPrediction = false;
  predic: any = [];
  Images;

  nameModelSaved = '';
  dateModelSaved = '';

  receivedParameters: string;
  mymodels = false;
  viewflex = false;
  loss;
  accuracy;
  public isLoggedIn = false;
  public userProfile: KeycloakProfile | null = null;

  constructor(private http: HttpClient,
              private toastr: ToastrService,
              private userService: UserService,
              private backEndPython: BackendscriptsService,
              private route: ActivatedRoute,
              private translate: TranslateService,
              private transLang: TranslationService,
              private readonly keycloak: KeycloakService) {

    // this.currentUser = JSON.parse(localStorage.getItem('currentUser'));
    this.currentUser = { id: '', username: '', firstName: '', lastName: '' };
    this.host = self.location.host.split(':')[0];





  }


  // tslint:disable-next-line:typedef
  async ngOnInit() {
    // this.loadAllUsers();

    this.isLoggedIn = await this.keycloak.isLoggedIn();

    if (this.isLoggedIn) {
      this.userProfile = await this.keycloak.loadUserProfile();
      this.currentUser.id = this.userProfile.id;
      this.currentUser.username = this.userProfile.username;
      this.currentUser.firstName = this.userProfile.firstName;
      this.currentUser.lastName = this.userProfile.lastName;

    }
    this.route.params.subscribe(params =>
      // tslint:disable-next-line:no-string-literal
      this.receivedParameters = params['namemodels']);

    if (typeof this.receivedParameters !== 'undefined') {
      this.nameModelSaved = this.receivedParameters.split('.')[0];
      this.dateModelSaved = this.receivedParameters.split('.')[1];
      this.mymodels = true;
      this.workflow = false;
    }
    this.getClassJSON();
  }



  /**
   * on file drop handler
   */
  onFileDropped($event): any {
    this.prepareFilesList($event);
  }

  /**
   * handle file from browsing
   */
  fileBrowseHandler(files): any {
    this.prepareFilesList(files);
  }

  /**
   * Delete file from files list
   * @param index (File index)
   */
  deleteFile(index: number): any {
    this.files.splice(index, 1);
  }

  /**
   * Get information saved in the JSON file by algorithm  CNN what build the model
   */
  getClassJSON(): void{

    this.userService.uploadJSONClasses(this.currentUser,
      this.modelName = this.workflow ? this.modelName : this.nameModelSaved + '.' + this.dateModelSaved, 'test')
      .subscribe(
        data => {
          for (let i = 0; i <= data.class.length - 1; i++){
            this.nameClases += ',' + data.class[i].id;
          }
          this.Images = data.TypeImage;
          this.accuracy = (Math.round(parseFloat(data.Accuracy) * 100) / 100)  * 100 ;
          (this.accuracy) < 0 ? this.accuracy = 0 : (this.accuracy > 100) ? this.accuracy = 100 : this.accuracy = this.accuracy.toFixed(2);
          this.loss = (Math.round(parseFloat(data.Loss) * 100) / 100)  * 100;
          (this.loss) < 0 ? this.loss = 0 : (this.loss > 100) ? this.loss = 100 : this.loss = this.loss.toFixed(2);

        },
        error => {
          console.log('Error in upload clases');
        });
  }



  /**
   *  upload process
   */
  uploadFilesSimulator(index: number): any {
    let im = 0;

    this.viewflex = true;

    // this.getClassJSON();

    Array.from(this.files).forEach(file => {
      this.currentFile = file; // this.selectedFiles.item(0);
      let originale: string;
      let result: string;
      const namefile = file.name.replace(/[^a-z0-9]/gi, '_').toLowerCase() + '.png';
      if (this.workflow){
        originale = 'http://' +  this.host + ':3001/users/files/downloadPicturesModelsTest/' + namefile +
                        '?username=' + this.currentUser.username + '&modelsname=' + this.modelName ;
        result =  'http://' +  this.host + ':3001/users/files/downloadPicturesModelsTest/' + namefile.split('.')[0] + '_prediction.png' +
                        '?username=' + this.currentUser.username + '&modelsname=' + this.modelName;
      }else{
        originale = 'http://' +  this.host + ':3001/users/files/downloadPicturesModelsTest/' + namefile +
                        '?username=' + this.currentUser.username + '&modelsname=' + this.nameModelSaved + '.' + this.dateModelSaved;
        result = 'http://' +  this.host + ':3001/users/files/downloadPicturesModelsTest/' + namefile.split('.')[0] + '_prediction.png' +
                        '?username=' + this.currentUser.username + '&modelsname=' + this.nameModelSaved + '.' + this.dateModelSaved;
      }
      this.userService.uploadFilesTest(
        this.currentUser, this.modelName = this.workflow ? this.modelName :  this.nameModelSaved + '.' + this.dateModelSaved ,
       'test', this.currentFile).subscribe(
        event => {
          if (event.type === HttpEventType.UploadProgress) {
            file.progress = Math.round(100 * event.loaded / event.total);
          } else if (event instanceof HttpResponse) {
            this.message = event.body.message;
            // this.fileInfos = this.userService.getFiles(this.currentUser,  this.modelName + '.' + this.dateModelSaved, 'pepe45');
            // if (im === 0){
            const imageFile = document.getElementById(file.name + '.png') as HTMLElement;
            const imgGif = (document.createElement('img') as HTMLImageElement);
            imgGif.src = 'assets/images/833.gif';
            imgGif.width = 50;
            imgGif.height = 50;
            imageFile.appendChild(imgGif);
            this.runningScriptsTensorCNNModel(file.name.replace(/[^a-z0-9]/gi, '_').toLowerCase()  + '.png',
                                             this.nameClases.slice(1), im, result, imgGif);
            im++;
              // setTimeout(() => {
              //   const imageFile = document.getElementById(file.name) as HTMLElement;
              //   // const imgOriginale = (document.createElement('img') as HTMLImageElement);
              //   // imgOriginale.src = originale;
              //   // imgOriginale.width = 200;
              //   // imgOriginale.height = 200;
              //   // imgOriginale.style.marginRight = '50%';
              //   const imgResult = (document.createElement('img') as HTMLImageElement);
              //   imgResult.src = result;
              //   imgResult.width = 320;
              //   imgResult.height = 200;
              //   imgResult.setAttribute('class', 'drop');
              //   // imgResult.style.marginRight = '20%';
              //   // imgResult.style.marginBottom = '20%';
              //   // imgResult.style.marginTop = '20%';
              //   imgResult.align = 'center';
              //   const h = document.createElement('H5') as HTMLElement;
              //   h.style.textAlign = 'center';
              //   h.style.color = 'red';
              //   const textResult = document.createTextNode(this.predic[im]);
              //   h.appendChild(textResult);
              //   // imageFile.appendChild(imgOriginale);
              //   imageFile.appendChild(h);
              //   imageFile.appendChild(imgResult);
              //   this.viewPrediction = true;
              // }, Array.from(this.files).length * 10000);


            // }else{
            //    this.runningScriptsTensorCNNModel(file.name, this.nameClases.slice(1), im, result);
            //    im++;
            // }
          }
        },
        err => {
          this.message = 'Could not upload the file!';
          this.currentFile = undefined;
        },
        add => {

        });

    });
    this.viewflex = false;
    this.viewFiles = true;

  }


  /**
   * Call function to the python server that executes the algorithm
   */
  runningScriptsTensorCNNModel(filename: string, classes: string, num: number, result: string, imgGif: HTMLImageElement): void {
    this.backEndPython.loginAutorization(this.currentUser, this.isLoggedIn.toString());
    setTimeout(() => {

      console.log(filename);
      console.log(classes);
      try {
        // tslint:disable-next-line:max-line-length
        this.http.get(`/scripts/ScriptsCNNModelTensor/${this.workflow ? this.modelName : this.nameModelSaved + '.' + this.dateModelSaved}/${this.currentUser.username}/${filename}/${classes}`, { responseType: 'text' })
          .subscribe(data => {
            this.predic.push(data as string);

            this.processLineByLine(num);

            const imageFile = document.getElementById(filename) as HTMLElement;
            // imageFile.parentNode.removeChild(document.getElementById(filename + '_gif'));

            // const imgOriginale = (document.createElement('img') as HTMLImageElement);
            // imgOriginale.src = originale;
            // imgOriginale.width = 200;
            // imgOriginale.height = 200;
            // imgOriginale.style.marginRight = '50%';
            const imgResult = (document.createElement('img') as HTMLImageElement);
            imgResult.src = result;
            imgResult.width = 320;
            imgResult.height = 200;
            imgResult.setAttribute('class', 'drop');
            // imgResult.style.marginRight = '20%';
            // imgResult.style.marginBottom = '20%';
            // imgResult.style.marginTop = '20%';
            imgResult.align = 'center';
            const h = document.createElement('H5') as HTMLElement;
            h.style.textAlign = 'center';
            h.style.color = 'blue';
            const textResult = document.createTextNode(this.predic[num]);
            h.appendChild(textResult);
            // imageFile.appendChild(imgOriginale);
            imageFile.appendChild(h);
            imageFile.appendChild(imgResult);

            this.viewPrediction = true;
            // console.log(this.predic);
            imageFile.removeChild(imgGif);


          });
      } catch (error) {
        console.log('Error');
      }
    }, 1500);


  }

  /**
   * Search line with noum prediction for view in Web UI
   */
  processLineByLine(num: number): void {
    const arrlines = this.predic[num].split(/\r?\n/);
    for (let i = 0, strLen = arrlines.length; i < strLen; i++) {
        for (let j = i + 1; j < strLen; j++) {
          if (arrlines[j].includes('prediction')) {
            this.predic[num] = arrlines[j].split(':')[1];
            break;
          }
        }
        break;
      }
    }



  ngAfterViewInit(): void {

    if (this.workflow){
      setTimeout(() => {
        this.loadImages = true;
      }, (this.layer + this.layerConvolution) * 10000 );
    }else {
      this.loadImages = true;
    }

  }

  /**
   * Convert Files list to normal array list
   * @param files (Files List)
   */
  prepareFilesList(files: Array<any>): any {
    for (const item of files) {
      item.progress = 0;
      const blob = item.slice(0, item.size, 'image/png');
      const newName = new File([blob], item.name.replace(/[^a-z0-9]/gi, '_').toLowerCase(), {type: 'image/png'});
      this.files.push(newName);
    }
    this.uploadFilesSimulator(0);
  }

  /**
   * format bytes
   * @param bytes (File size in bytes)
   * @param decimals (Decimals point)
   */
  formatBytes(bytes, decimals): any {
    if (bytes === 0) {
      return '0 Bytes';
    }
    const k = 1024;
    const dm = decimals <= 0 ? 0 : decimals || 2;
    const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    return parseFloat((bytes / Math.pow(k, i)).toFixed(dm)) + ' ' + sizes[i];
  }

   deleteModels(): void{


    this.userService.deleteModels(this.currentUser, this.nameModelSaved + '.' + this.dateModelSaved)
      .subscribe(
        file => {
          console.log(file);
          this.showToaster(this.nameModelSaved + '.' + this.dateModelSaved + '  ' + this.translate.instant('ModelDeleted.text'));

        },
        error => {
          this.showToasterError(this.translate.instant('Error.text') + this.nameModelSaved + '.' + this.dateModelSaved
           + this.translate.instant('ModelDeletedError.text'));
        });



  }

  // Information OK
  showToaster(message: string): any {
    this.toastr.success(message);
  }
  // Information no OK
  showToasterError(message: string): any {
    this.toastr.error(message);
  }

}
