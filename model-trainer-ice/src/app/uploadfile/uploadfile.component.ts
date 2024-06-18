import { Component, OnInit, ViewChildren, ViewChild, AfterViewInit, OnDestroy} from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Subject } from 'rxjs';
// import { ToastrService } from 'ngx-toastr';
declare var require: any;
import { UserService, AlertService } from '../services/index_services';
import { User } from '../models/index_models';
import { ToastrService } from 'ngx-toastr';
import { DatePipe } from '@angular/common';
import { KeycloakService } from 'keycloak-angular';
import { KeycloakProfile } from 'keycloak-js';



@Component({
  selector: 'app-uploadfile',
  templateUrl: './uploadfile.component.html',
  styleUrls: ['./style_uploadfile.css']
})
export class UploadFileComponent implements AfterViewInit, OnInit {

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
  public isLoggedIn = false;
  public userProfile: KeycloakProfile | null = null;

  constructor(private http: HttpClient,
              private userService: UserService,
              private alertService: AlertService,
              private toastr: ToastrService,
              private datePipe: DatePipe,
              private readonly keycloak: KeycloakService) {

        // this.currentUser = JSON.parse(localStorage.getItem('currentUser'));
        this.currentUser = { id: '', username: '', firstName: '', lastName: '' };
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

  ngAfterViewInit(): void {
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

    const formData = new FormData();
    // tslint:disable-next-line:prefer-for-of
    for (let i = 0; i < this.uploadedFilesTrain.length; i++) {
      formData.append('uploads[]', this.uploadedFilesTrain[i], this.uploadedFilesTrain[i].name);
    }
    // tslint:disable-next-line:prefer-for-of
    for (let i = 0; i < this.uploadedFilesTest.length; i++) {
      formData.append('uploads[]', this.uploadedFilesTest[i], this.uploadedFilesTest[i].name);

    }
    this.http.post('/api/upload', formData)
      .subscribe((response) => {
      });

    this.runningScriptsML();
  }

  runningScriptsML(): void {
    this.gifData = true;
    // tslint:disable-next-line: max-line-length
    this.http.get(`http://127.0.0.1:5002/scriptsml/${this.modelName}/${this.uploadedFilesTrain[0].name}/${this.uploadedFilesTest[0].name}/${this.currentUser.username}`).subscribe(data => {
      this.informationWorkflow = data as string;
      this.processLineByLineTrain();
      this.nextNeu = true;
      this.gifData = false;
    });

    this.createWorkSpace();

  }


   createWorkSpace(): any {

         this.pipeDate = this.datePipe.transform(Date.now(), 'yyyyMMdd');

         this.userService.createdirectorymodel(this.currentUser.username.toString(), this.modelName + '.' + this.pipeDate )
            .subscribe(
                data => {
                    // set success message and pass true paramater to persist the message after redirecting to the login page
                    this.alertService.success('Create container in workSpace of model successful', true);
                    this.showToaster('WorkSpace created');
                },
                error => {
                    this.alertService.error(error);
                    this.showToasterError('Error, container in workSpace of model no created, please try again');
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
  }
}


