import { Component, OnInit } from '@angular/core';
import { ResizedEvent } from 'angular-resize-event';
import { TranslateService } from '@ngx-translate/core';
import { TranslationService } from '../../services/translation.service';
import { DatePipe } from '@angular/common';


import { HttpClient, HttpEventType, HttpResponse } from '@angular/common/http';
// import { Observable } from 'rxjs';
import { ToastrService } from 'ngx-toastr';
import { AlertService, UserService, AuthenticationService, FileService } from '../../services/index_services';
import {BackendscriptsService} from '../../services/backendscripts.service';
import { User } from '../../models/index_models';
import { TimeLoadingService } from '../../services/timeloading.service';
import { LanguageApp } from '../../services/LanguageApp';
import { FormBuilder, Validators } from '@angular/forms';
import { Observable } from 'rxjs';
import { stringify } from '@angular/compiler/src/util';
import { ThrowStmt } from '@angular/compiler';

import { KeycloakService } from 'keycloak-angular';
import { KeycloakProfile } from 'keycloak-js';


@Component({
  selector: 'app-cnn',
  templateUrl: './cnn.component.html',
  styleUrls: ['./cnn.component.css']
})
export class CnnComponent implements OnInit {

  width: number;
  height: number;
  currentUser: User;
  host: any;
  dtOptions: any;
  public formGroup = this.fb.group({
   file: [null, Validators.required]
 });
   informationWorkflow: string;

 selectedFiles: FileList;
  currentFile?: File;
  progress = 0;
  message = '';
  modelnameDate = '';

  fileInfos?: Observable<any>;
  modelName: string = null;
  pipeDate: string = null;
  buttonsGroup = true;
  inputFileTrain = false;
  choseClassCheckBox = [];
  numberchooseRadio = [];
  endRadioCheckBox = false;
  choseImages = false;
  numberImages = 0;
  selecClasses = true;
  imagesViewMenu = false;
  color = [
          '#9400d3', '#dc143c', '#87ceeb', '#006400', '#808080',
          '#ff00ff', '#ffa500', '#ffff00', '#ff0000', '#00ff7f'
          // '#80B300', '#809900', '#E6B3B3', '#6680B3', '#66991A',
          // '#FF99E6', '#CCFF1A', '#FF1A66', '#E6331A', '#33FFCC',
          // '#66994D', '#B366CC', '#4D8000', '#B33300', '#CC80CC',
          // '#66664D', '#991AFF', '#E666FF', '#4DB3FF', '#1AB399',
          // '#E666B3', '#33991A', '#CC9999', '#B3B31A', '#00E680',
          // '#4D8066', '#809980', '#E6FF80', '#1AFF33', '#999933',
          // '#FF3380', '#CCCC00', '#66E64D', '#4D80CC', '#9900B3',
          // '#E64D66', '#4DB380', '#FF4D4D', '#99E6E6', '#6666FF'
        ];

  typeClassFiles: Array<string[]> = [];
  classFilesJSON: { id: string, files: string }[] = [];
  nextNeu = false;
  numberClasses = 0;
  countFilesImages = [];
  activation;
  Images;
  BatchNormalization;
  layerConvolution = 0;
  Dropout;
  DropoutConv;
  Dense;
  DenseConv;
  DenseSimple;
  layer = 0;
  numberNeuros;
  epochs;
  loadImages = false;
  accuracy; // = 0.70 * 100;
  loss; //  = 0.20 * 100;
  densePerLayerConv = [];
  densePerLayer = [];
  gifTimer = false;
  imgWidth;
  imgHeight;
  batchSize;



  learningRate;
  momentum;
  lossCompile;
  kernelInitializer;
  seed;
  shuffle;
  colorMode;
  classMode;
  featurewiseCenter;

  endRadioCheck = true;

  private fileName;

  public isLoggedIn = false;
  public userProfile: KeycloakProfile | null = null;



  constructor(private http: HttpClient,
              private toastr: ToastrService,
              private userService: UserService,
              private backEndPython: BackendscriptsService,
              private translate: TranslateService,
              private authenticationService: AuthenticationService,
              private datePipe: DatePipe,
              private alertService: AlertService,
              private transLang: TranslationService,
              public loading: TimeLoadingService,
              private fb: FormBuilder,
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

    // this.fileInfos = this.userService.getFiles(this.currentUser,  this.modelName + '.' + this.pipeDate, 'pepe45');

    for (let i = 0; i <= 10; i++) {
      this.typeClassFiles.push([]);
      this.classFilesJSON.push({ id: '', files: '' });
      this.countFilesImages[i] = 0;

    }

  }

   public onFileChange(event): any {
   const reader = new FileReader();

   if (event.target.files && event.target.files.length) {
     this.fileName = event.target.files[0].name;
     const [file] = event.target.files;
     reader.readAsDataURL(file);

     reader.onload = () => {
       this.formGroup.patchValue({
         file: reader.result
       });
     };
   }
 }

//  public onSubmit(): void {
//    this.userService.UploadPictures(this.currentUser, 'model', './../../../user_upload', this.fileName, this.formGroup.get('file').value);
//  }

createArrayForConvolutionLayersInput(): void{

  this.densePerLayerConv = [];
  for (let index = 1; index <= this.layerConvolution; index++) {
     this.densePerLayerConv.push(index);
  }

}

createArrayForNeuronalLayersInput(): void{

  this.densePerLayer = [];
  for (let index = 1; index <= this.layer; index++) {
     this.densePerLayer.push(index);
  }

}



getcreateArrayForConvolutionLayersInputDropout(): void{

  this.DropoutConv = '';
  for (let index = 1; index <= this.layerConvolution; index++) {
    const value = document.getElementById(index + '_layerConvDropout') as HTMLInputElement;
    this.DropoutConv += ',' +  value.value;
  }

  this.DropoutConv = this.DropoutConv.substring(1);

}

getcreateArrayForConvolutionLayersInputDense(): void{


  this.DenseConv = '';
  for (let index = 1; index <= this.layerConvolution; index++) {
    const value = document.getElementById(index + '_layerConvDense') as HTMLInputElement;
    this.DenseConv += ',' +  value.value;
  }

  this.DenseConv = this.DenseConv.substring(1);

}

getcreateArrayForNeuronalLayersInputDropout(): void{

  this.Dropout = '';
  for (let index = 1; index <= this.layer; index++) {
    const value = document.getElementById(index + '_layerDropout') as HTMLInputElement;
    this.Dropout += ',' +  value.value;
  }

  this.Dropout = this.Dropout.substring(1);

}

getcreateArrayForNeuronalLayersInputDense(): void{

  this.Dense = '';
  for (let index = 1; index <= this.layer; index++) {
    const value = document.getElementById(index + '_layerDense') as HTMLInputElement;
    this.Dense += ',' +  value.value;
  }

  this.Dense = this.Dense.substring(1);
}


getAllInformationLayer(): void{
  this.getcreateArrayForConvolutionLayersInputDropout();
  this.getcreateArrayForConvolutionLayersInputDense();
  this.getcreateArrayForNeuronalLayersInputDropout();
  this.getcreateArrayForNeuronalLayersInputDense();
}



selectFile(event): void {
  this.endRadioCheck = false;
  this.buttonsGroup = false;
  this.inputFileTrain = true;
  this.selectedFiles = event.target.files;
}



upload(): void {

  this.createWorkSpace();
  this.imagesViewMenu = true;
  this.endRadioCheck = true;
  let formData = new FormData();


  Array.from(this.selectedFiles).forEach(file => {
    this.progress = 0;
    formData = new FormData();
    this.currentFile = file; // this.selectedFiles.item(0);
    formData.append('file', file);

    // this.userService.updateFiletoUploads(formData);

    this.userService.uploadFiles(this.currentUser, this.modelnameDate, this.currentFile).subscribe(
      event => {
        if (event.type === HttpEventType.UploadProgress) {
          this.progress = Math.round(100 * event.loaded / event.total);
        } else if (event instanceof HttpResponse) {
          this.message = event.body.message;
          this.fileInfos = this.userService.getFiles(this.currentUser,  this.modelName + '.' + this.pipeDate, 'cnn');
        }
      },
      err => {
        this.progress = 0;
        this.message = 'Could not upload the file!';
        this.currentFile = undefined;
      });
    this.selectedFiles = undefined;

  });
}


  uploadClasses(): void {


    this.pipeDate = this.datePipe.transform(Date.now(), 'yyyyMMdd');


    for (let j = 0; j <= this.typeClassFiles.length - 1; j++) {

      for (let i = 0; i <= this.typeClassFiles[j].length - 1; i++) {

        if (i === 0) {
          const position = j;
          const nameClass = (document.getElementById(position + '_name') as HTMLInputElement).value;
          // console.log(nameClass);
          if (!nameClass) {
            this.classFilesJSON[j].id = this.typeClassFiles[j][i];
          }else {
            this.classFilesJSON[j].id = (document.getElementById(position + '_name') as HTMLInputElement).value;
          }
        } else {
          this.classFilesJSON[j].files += ',' + this.typeClassFiles[j][i];

        }



      }

      this.classFilesJSON[j].files = this.classFilesJSON[j].files.slice(1);
      // console.log(this.classFilesJSON[j].files);

    }

    this.classFilesJSON.shift();
    // console.log(this.classFilesJSON);

    const fileJSON: string = JSON.stringify(this.classFilesJSON);

    // console.log(fileJSON);


    this.userService.uploadClasses(this.currentUser, this.modelnameDate , './user_upload', fileJSON).subscribe(
      event => {
        if (event.type === HttpEventType.UploadProgress) {
          this.progress = Math.round(100 * event.loaded / event.total);
        } else if (event instanceof HttpResponse) {
          this.message = event.body.message;
          this.fileInfos = this.userService.getFiles(this.currentUser, 'pepe45', 'pepe45');
        }
      },
      err => {
        this.progress = 0;
        this.message = 'Could not upload the file!';
        this.currentFile = undefined;
      });
  //   this.selectedFiles = undefined;

  // });
    this.selecClasses = false;
    this.backEndPython.loginAutorization(this.currentUser, this.isLoggedIn.toString());
    setTimeout(() => {
      this.runningScriptsTensorCNN();
    }, 1500);

  }

  addInputs(): any{

    this.numberClasses = parseInt((document.getElementById('member') as HTMLInputElement).value, 10);


    this.numberClasses = this.numberClasses > 10 ? this.numberClasses = 10 : this.numberClasses;

    for (let i = 1 ; i <= this.numberClasses ; i++){
        this.typeClassFiles[i].push('Class_' + i);
        this.numberchooseRadio.push(i);
    }

    this.endRadioCheckBox =  true;


}



  AddTypeFileList(event, input): void {

    const numberClass = this.choseClassCheckBox.find((item) =>  item.id );

    if (numberClass){

      const numberEnd = numberClass.id;
      const styleElem = document.getElementById(input) as HTMLInputElement;
      styleElem.style.boxShadow  = '0 0 25px ' + this.color[numberEnd - 1];

      if (event.target.checked === true) {
          const styleElemBorder = document.getElementById(input) as HTMLInputElement;
          styleElemBorder.style.boxShadow  = '0 0 25px ' + this.color[numberEnd - 1];
          this.typeClassFiles[numberEnd].push(event.target.name);
          // console.log(this.typeClassFiles);
          this.numberImages++;
          //  this.countFilesImages[numberEnd - 1]++;
      }

      if (event.target.checked === false) {

        const styleElemClean = document.getElementById(input) as HTMLInputElement;
        styleElemClean.style.boxShadow  = '0 0 25px #ffffff';

        this.typeClassFiles.forEach(element => {
          // element.splice(element.indexOf(event.target.name), 1);
          for (let i = element.length - 1 ; i >= 1 ; i--) {
            if (element[i] === event.target.name) {
                element.splice(i, 1);
                // break;       //<-- Uncomment  if only the first term has to be removed
            }
        }
        });
        // this.typeClassFiles[numberEnd].splice(this.typeClassFiles[numberEnd].indexOf(event.target.name));
        // console.log(this.typeClassFiles);

        this.choseImages = true;
        this.numberImages--;
        // this.countFilesImages[numberEnd - 1]--;

        }
      this.choseImages =  this.numberImages < (30 * this.numberClasses) ? false : true;
    }
  }

 OnCheckboxSelectKmeansChooseIDBuildModels(id, event): any {


      this.choseClassCheckBox = [];
      if (event.target.checked === true) {
        // tslint:disable-next-line: object-literal-shorthand
        this.choseClassCheckBox.push({ id: id, checked: event.target.checked });
      }
      if (event.target.checked === false) {
        this.choseClassCheckBox = this.choseClassCheckBox.filter((item) => item.id !== id);
      }


      this.numberchooseRadio.forEach(it => {
        if (it !== id){
          // tslint:disable-next-line:no-string-literal
          document.getElementById(it)['checked'] = false;
        }else{
          // it.id = true;
          // tslint:disable-next-line:no-string-literal
          document.getElementById(it)['checked'] = true;
        }
      });
  }


 createWorkSpace(): any {

         this.pipeDate = this.datePipe.transform(Date.now(), 'yyyyMMdd');
         this.modelnameDate = this.modelName + '.' + this.pipeDate;

         this.userService.createdirectorymodel(this.currentUser.username.toString(), this.modelnameDate )
            .subscribe(
                data => {
                    // set success message and pass true paramater to persist the message after redirecting to the login page
                    this.alertService.success(this.translate.instant('CreateContainer.text'), true);
                    this.showToaster(this.translate.instant('WorkspaceCreated.text'));
                },
                error => {
                    this.alertService.error(error);
                    this.showToasterError(this.translate.instant('NoWorkspaceCreated.text'));
                });



    }



runningScriptsTensorCNN(): void {


      this.gifTimer = true;
      if (this.Images === 'SimpleImages'){
        this.layerConvolution = 0;
        this.Dropout = '0';
        this.layer = 0;
        this.DropoutConv = '0';
        this.DenseConv = '0';
        this.Dense = this.DenseSimple;
        this.learningRate = '0';
        this.momentum = '0';
        this.lossCompile = '0';
        this.kernelInitializer = '0';
        this.seed = '0';
        this.shuffle = '0';
        this.colorMode = '0';
        this.classMode = '0';
        this.featurewiseCenter = '0';
      }else if (  this.Images === 'TransferLearning'){

        this.layerConvolution = 0;
        this.Dropout = '0';
        this.layer = 0;
        this.DropoutConv = '0';
        this.DenseConv = '0';
        this.Dense = this.DenseSimple;

      } else {
        this.getAllInformationLayer();

      }

      try {
        this.http.get(`/scripts/ScriptsCNNTensor/${this.modelName}/${this.currentUser.username}/${this.currentUser.username}/${this.Images}/${this.BatchNormalization}/${this.layerConvolution}/${this.Dropout}/${this.Dense}/${this.layer}/${this.DropoutConv}/${this.DenseConv}/${this.activation}/${this.epochs}/${this.batchSize}/${this.imgHeight}/${this.imgWidth}/${this.learningRate}/${this.momentum}/${this.lossCompile}/${this.kernelInitializer}/${this.seed}/${this.shuffle}/${this.colorMode}/${this.classMode}/${this.featurewiseCenter}`, {responseType: 'text'})
          .subscribe(data => {
            this.informationWorkflow = data as string;

            const arrlines = this.informationWorkflow.split(/\r?\n/);
            for (let i = 0, strLen = arrlines.length; i < strLen; i++) {
              if (arrlines[i].includes('AccuracyLoss:')) {
                const AccuracyLoss = arrlines[i].split(':')[1].split(',');
                this.accuracy = (Math.round(parseFloat(AccuracyLoss[0]) * 100) / 100)  * 100 ;
                // tslint:disable-next-line:max-line-length
                (this.accuracy) < 0 ? this.accuracy = 0 : (this.accuracy > 100) ? this.accuracy = 100 : this.accuracy = this.accuracy.toFixed(2);
                this.loss = (Math.round(parseFloat(AccuracyLoss[1]) * 100) / 100)  * 100;
                (this.loss) < 0 ? this.loss = 0 : (this.loss > 100) ? this.loss = 100 : this.loss = this.loss.toFixed(2);

                break;
              }
            }

            this.loadImages = true;
            this.gifTimer = false;
          });
      } catch (error) {

      }

      this.nextNeu = true;
  }






  onResized(event: ResizedEvent): void {
    this.width = event.newWidth;
    this.height = event.newHeight;
  }

   tableLanguage(): void{
      if (this.transLang.activeLang === 'es'){
      this.dtOptions = { pagingType: 'full_numbers', pageLength: 10, language: LanguageApp.spanishdatatables };
    }else{
      this.dtOptions = { pagingType: 'full_numbers', pageLength: 10};
    }
  }

     showToaster(message: string): any{
      this.toastr.success(message);
    }

     showToasterError(message: string): any{
      this.toastr.error(message);
  }

}
