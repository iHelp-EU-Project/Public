import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { ResizedEvent } from 'angular-resize-event';
import { UserService } from '../services/index_services';
import { User } from '../models/index_models';
import { ToastrService } from 'ngx-toastr';
import { Injectable } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { LanguageApp } from '../services/LanguageApp';
import { TranslationService } from '../services/translation.service';
import { KeycloakService } from 'keycloak-angular';
import { KeycloakProfile } from 'keycloak-js';




@Component({
  selector: 'app-mymodels',
  templateUrl: './mymodels.component.html',
  styleUrls: ['./mymodels.component.css']
})

export class MymodelsComponent implements OnInit {

  width: number;
  height: number;
  url: string;
  filesJSON: Array<JSON>;
  public arrayKeys;
  currentUser: User;
  nameModels: string;
  nameModelsOK = false;
  press = false;
  public informationJSONAlgorithm;
  csvUrlJSONInformationAlgorithm: string[];

  columns: string;
  id: string;
  kmeans: string;
  glm: string;
  cnn: string;
  sklearnClustering: string;
  birchClustering: string;
  wardClustering: string;
  gan: string;


  search;
  searchName;

  typeAlgorithm: boolean;


  directoryTmp: Array<string>;
  directory: string;
  nameModelsArray = [];
  public isLoggedIn = false;
  public userProfile: KeycloakProfile | null = null;


  constructor(private userService: UserService,
              private toastr: ToastrService,
              private translate: TranslateService,
              private transLang: TranslationService,
              private readonly keycloak: KeycloakService) {
    // Upload user
    // tslint:disable-next-line:max-line-length
    this.currentUser = { id: '', username: '' , firstName: '' , lastName: ''}; // JSON.parse(localStorage.getItem('currentUser')); 
    this.columns = 'columns';
    this.id  = 'id';
    this.kmeans = 'kmeans';
    this.glm = 'glm';
    this.cnn = 'cnn';
    this.sklearnClustering = 'Sklearn_Clustering';
    this.birchClustering = 'Sklearn_Clustering_Birch';
    this.wardClustering = 'Sklearn_Clustering_Ward';
    this.gan = 'gan';

  }

  @Output() myEvent = new EventEmitter();


  // tslint:disable-next-line:typedef
  async ngOnInit() {

    this.isLoggedIn = await this.keycloak.isLoggedIn();

    if (this.isLoggedIn) {
      this.userProfile = await this.keycloak.loadUserProfile();
      // this.currentUser = await this.keycloak.loadUserProfile();
      // this.currentUser.password = 'x';
      this.currentUser.id = this.userProfile.id;
      this.currentUser.username = this.userProfile.username;
      this.currentUser.firstName = this.userProfile.firstName;
      this.currentUser.lastName = this.userProfile.lastName;
    }

    this.myModels();

  }

  myModels(): void {


    this.directoryTmp = [];
    this.directory = '';

    // Load all existing models in the worspace in the web UI
    this.filesJSON = [];
    this.userService.readFilesJSONDirectory(this.currentUser)
      .subscribe(
        data => {
          // set success message and pass true paramater to persist the message after redirecting to the login page
          const nameFilesJSON = data.split(',');
          for (let i = 1; i <= nameFilesJSON.length - 1; i++) {
            this.directoryTmp = nameFilesJSON[i].split('.');
            this.directory = this.directoryTmp[0] + '.' + this.directoryTmp[1];
            console.log( this.directory );
            this.nameModelsArray.push(this.directory);

            this.userService.downloadJSONFiles(this.currentUser, nameFilesJSON[i], this.directory)
              .subscribe(
                file => {
                  this.filesJSON.push(JSON.parse(file));
                  // console.log(JSON.parse(file));
                  // set success message and pass true paramater to persist the message after redirecting to the login page
                  // this.showToaster(this.translate.instant('Read.text') + this.directory + this.translate.instant('Successful.text'));
                },
                error => {
                  this.showToasterError(this.translate.instant('Error.text') + this.directory + this.translate.instant('NoReadSuccessful.text'));
                });
          }
          this.inputOptionModelsName();
          this.showToaster(this.translate.instant('DownloadJSON.text'));

        },
        error => {
          this.showToasterError(this.translate.instant('ErrorDonloadJSON.text'));
        });





  }

  // Resised aunt web UI
  onResized(event: ResizedEvent): void {
    this.width = event.newWidth;
    this.height = event.newHeight;
  }

  // Get date
  getDateTimeStamp(nu: number): string {

    if (this.transLang.activeLang === 'es'){
      return new Date(nu).toLocaleDateString('es-es');
    }else{
      return new Date(nu).toLocaleDateString('en-us');
  }

  }
  // Infortation OK
  showToaster(message: string): any {
    this.toastr.success(message);
  }
  // Information no Ok
  showToasterError(message: string): any {
    this.toastr.error(message);
  }

  // Update model, for send to next component, and change color the model select in web UI
  onUpdateModel($event, active, typealgorithm): void {
    const clickedIndex = $event;
    // console.log(clickedIndex, active);
    this.nameModels = clickedIndex;
    this.nameModelsOK = true;
    this.myEvent.emit(this.nameModels);
    this.informationJSONAlgorithm = typealgorithm;
    // tslint:disable-next-line:forin
    for (const item in this.filesJSON) {
      // console.log(item);

      if (item === active) {
        document.getElementById(item).className = 'card-body  press';
      }
      else {
        document.getElementById(item).className = 'card-body ';
      }
    }

    // this.getDataBackEnd();

  }

  getEmitter(): any {
        return this.nameModels;
    }


  getDataBackEnd(): any {


      // this.userService.downloadJSONFiles(this.currentUser, url[1], url[0]).subscribe(data => {
      //   this.informationJSONAlgorithm =  JSON.parse(data).typealgorithm;
      //   console.log(this.informationJSONAlgorithm);
        switch (this.informationJSONAlgorithm) {
          case 'columns':
            this.typeAlgorithm = true;
            break;
          case 'id':
            this.typeAlgorithm = false;
            break;

          default:
            break;
        }
    // });

  }

  seachingDisplay(): void{

    const viewElementsType = this.search.split(',');
    const view = ['glm', 'id', 'kmeans', 'columns', 'cnn'];

    view.forEach(element => {
      const type = document.getElementsByClassName(element);
      console.log(type);
      // tslint:disable-next-line:prefer-for-of
      for (let i = 0; i < type.length; i++) {
          const parent = (type[i] as HTMLElement).parentElement.parentElement.parentElement.parentElement;
          // parent.style.visibility = 'visible';
          // parent.style.overflow = 'visible';
          parent.className = 'card align-items-center border border-0 efecto3';
          parent.style.display = 'block';
          parent.style.display = '';



      }
    });
    console.log(viewElementsType.length);
    if (viewElementsType.length > 1){
      viewElementsType.forEach(element => {
        const type = document.getElementsByClassName(element);
        console.log(type);
        // tslint:disable-next-line:prefer-for-of
        for (let i = 0; i < type.length; i++) {
            const parent = (type[i] as HTMLElement).parentElement.parentElement.parentElement.parentElement;
            // parent.style.visibility = 'hidden';
            // parent.style.overflow = 'hidden';
            parent.className = 'card align-items-center border border-0 efecto3';
            parent.style.display = 'none';



        }
      });
    }



  }

  inputOptionModelsName(): void{

    const inputSearch = document.getElementById('inputSearchNamesModels') as HTMLElement;
    const inputSearchDataList = (document.createElement('datalist') as HTMLDataListElement);
    inputSearchDataList.id = 'name';
    inputSearchDataList.style.display = 'block';

    this.nameModelsArray.forEach(element => {

      const option = (document.createElement('option') as HTMLOptionElement);
      option.value = element;
      option.label = element;
      inputSearchDataList.appendChild(option);
      inputSearch.appendChild(inputSearchDataList);


    });
  }

}

