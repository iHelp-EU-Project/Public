import { Component, OnInit, ViewChildren, ViewChild, AfterViewInit, OnDestroy, ElementRef} from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Subject } from 'rxjs';
// import { ToastrService } from 'ngx-toastr';
declare var require: any;
import { UserService, AlertService } from '../../services/index_services';
import {BackendscriptsService} from '../../services/backendscripts.service';
import { User } from '../../models/index_models';
import { ToastrService } from 'ngx-toastr';
import { DatePipe } from '@angular/common';
import { TranslateService } from '@ngx-translate/core';
import { LanguageApp } from '../../services/LanguageApp';
import { TranslationService } from '../../services/translation.service';
import { TimeLoadingService } from '../../services/timeloading.service';
import { KeycloakService } from 'keycloak-angular';
import { KeycloakProfile } from 'keycloak-js';


@Component({
  selector: 'app-uploadfilesid',
  templateUrl: './uploadfilesid.component.html',
  styleUrls: ['./uploadfilesid.component.css']
})
export class UploadfilesidComponent implements OnInit{

 trainFile: File = null;
  testFile: File = null;
  uploadedFilesTrain: Array<File> = null;
  uploadedFilesTest: Array<File> = null;
  modelName: string = null;
  informationWorkflow: string;
  correlationTrain: Array<string[]> = [];
  correlationTest: Array<string[]> = [];
  correlationTrainHeaders;
  correlationTrainBody;
  correlationTestHeaders;
  correlationTestBody;
  nextNeu = false;
  gifData = false;
  @ViewChildren('columnsID') item;
  selectedIds = [];
  checktable = false;
  inputFileTrain = false;
  inputFileTest = false;
  dtOptions: DataTables.Settings = {pagingType: 'full_numbers', pageLength: 10 };
  currentUser: User;
  pipeDate: string = null;

  host: any;
  public isLoggedIn = false;
  public userProfile: KeycloakProfile | null = null;

  @ViewChild('name') element: ElementRef;

  constructor(private http: HttpClient,
              private userService: UserService,
              private backEndPython: BackendscriptsService,
              private alertService: AlertService,
              private toastr: ToastrService,
              private datePipe: DatePipe,
              private translate: TranslateService,
              private transLang: TranslationService,
              public loading: TimeLoadingService,
              private readonly keycloak: KeycloakService) {

    // this.currentUser = JSON.parse(localStorage.getItem('currentUser'));
    this.currentUser = { id: '', username: '', firstName: '', lastName: '' };
    this.host = self.location.host.split(':')[0];

  }

  // tslint:disable-next-line:typedef
  async ngOnInit() {

      this.isLoggedIn = await this.keycloak.isLoggedIn();

      if (this.isLoggedIn) {
          this.userProfile = await this.keycloak.loadUserProfile();
          this.currentUser.id = this.userProfile.id;
          this.currentUser.username = this.userProfile.username;
          this.currentUser.firstName = this.userProfile.firstName;
          this.currentUser.lastName = this.userProfile.lastName;

      }


  }

  fileChangeTrain(element): void {
    this.uploadedFilesTrain = element.target.files;
    this.inputFileTrain = true;
  }

  fileChangeTest(element): void {
    this.uploadedFilesTest = element.target.files;
    this.inputFileTest = true;
  }

  // showToaster(): void
  // {
  //     this.toastr.success('Error, Select at least one column of the table');
  // }


  upload(): void {

    let formData = new FormData();
    // tslint:disable-next-line:prefer-for-of
    for (let i = 0; i < this.uploadedFilesTrain.length; i++) {
      formData.append('file', this.uploadedFilesTrain[i], this.uploadedFilesTrain[i].name);
    }

    this.userService.updateFiletoUploads(formData);
    formData = new FormData();

    // tslint:disable-next-line:prefer-for-of
    for (let i = 0; i < this.uploadedFilesTest.length; i++) {
      formData.append('file', this.uploadedFilesTest[i], this.uploadedFilesTest[i].name);

    }
    // this.http.post('/api/upload', formData)
    //   .subscribe((response) => {
    //   });

    this.userService.updateFiletoUploads(formData);


    this.runningScriptsML();
  }

  runningScriptsML(): void {
    this.backEndPython.loginAutorization(this.currentUser, this.isLoggedIn.toString());
    setTimeout(() => {

      this.loading.timeLoading();
      this.tableLanguage();
      this.gifData = true;
      try {
        // tslint:disable-next-line: max-line-length
        this.http.get(`/scripts/scriptsmlid/${this.modelName}/${this.uploadedFilesTrain[0].name}/${this.uploadedFilesTest[0].name}/${this.currentUser.username}`, { responseType: 'text' }).subscribe(data => {
          this.informationWorkflow = data as string;
          this.processLineByLineTrain();
          this.nextNeu = true;
          this.gifData = false;
        });
      } catch (error) {
        this.gifData = false;
        this.nextNeu = true;
      }

      this.createWorkSpace();
    }, 1500);

  }




   createWorkSpace(): any {

         this.pipeDate = this.datePipe.transform(Date.now(), 'yyyyMMdd');

         this.userService.createdirectorymodel(this.currentUser.username.toString(), this.modelName + '.' + this.pipeDate )
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

    showToaster(message: string): any{
      this.toastr.success(message);
    }

     showToasterError(message: string): any{
      this.toastr.error(message);
  }



  processLineByLineTrain(): void {
    let numTrain = 0;
    const filesModelTrain: string[] = [this.uploadedFilesTrain[0].name];
    const expression = /\s* \s*/;
    const arrlines = this.informationWorkflow.split(/\r?\n/);
    for (let i = 0, strLen = arrlines.length; i < strLen; i++) {
      if (arrlines[i].includes('Parse progress:')) {
        if (numTrain === 0) {
          this.correlationTrain.push(filesModelTrain);
          numTrain++;
        }
        for (let j = i + 1; j < strLen; j++) {
          if (arrlines[j].includes('rows')) {
            i++;
            break;
          } else {
            if (j === (i + 1)) {
              this.correlationTrain.push(arrlines[j].trimStart().split(expression));
            }
            else {
              this.correlationTrain.push(arrlines[j].split(expression));
            }
          }
        }
        this.processLineByLineTest(arrlines, i, expression);
        break;
      }
    }
    this.correlationTrainHeaders = [];
    this.correlationTrainBody = [];

    this.correlationTrainBody = this.correlationTrain.slice();
    this.correlationTrainBody.shift();
    this.correlationTrainHeaders = this.correlationTrainBody.shift();
    this.correlationTrainBody.shift();
    this.correlationTrainBody.pop();
  }


  processLineByLineTest(st: string[], n: number, s: RegExp): void{
    const arrlines = st;
    let numTest = 0;
    const filesModelTest: string[] = [this.uploadedFilesTest[0].name];

    for (let i = n, strLen = arrlines.length; i < strLen; i++) {
      if (arrlines[i].includes('Parse progress:')) {
        if (numTest === 0) {
          this.correlationTest.push(filesModelTest);
          numTest++;
        }
        for (let j = i + 1; j < st.length; j++) {
          if (arrlines[j].includes('rows')) {
            break;
          } else {
            if (j === (i + 1)) {
              this.correlationTest.push(arrlines[j].trimStart().split(s));
            }
            else {
              this.correlationTest.push(arrlines[j].split(s));
            }
          }
        }
      }
    }
    this.correlationTestHeaders = [];
    this.correlationTestBody = [];

    this.correlationTestBody = this.correlationTest.slice();
    this.correlationTestBody.shift();
    this.correlationTestHeaders = this.correlationTestBody.shift();
    this.correlationTestBody.shift();
    this.correlationTestBody.pop();

  }

  OnCheckboxSelect(id, event): void {

      this.selectedIds = [];
      if (event.target.checked === true) {
        // tslint:disable-next-line: object-literal-shorthand
        this.selectedIds.push({ id: id, checked: event.target.checked });
      }
      if (event.target.checked === false) {
        this.selectedIds = this.selectedIds.filter((item) => item.id !== id);
      }

      if (this.selectedIds.length < 1){
        this.checktable = false;
      } else{
        this.checktable = true;
      }

      this.correlationTrainHeaders.forEach(it => {
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

   tableLanguage(): void{
      if (this.transLang.activeLang === 'es'){
      this.dtOptions = { pagingType: 'full_numbers', pageLength: 10, language: LanguageApp.spanishdatatables };
    }else{
      this.dtOptions = { pagingType: 'full_numbers', pageLength: 10};
    }
  }

}
