import { Component, OnInit, ViewChildren, ViewChild, AfterViewInit, OnDestroy, ElementRef, DoCheck} from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Subject } from 'rxjs';
// import { ToastrService } from 'ngx-toastr';
declare var require: any;
import { UserService, AlertService } from '../../services/index_services';
import { User } from '../../models/index_models';
import { ToastrService } from 'ngx-toastr';
import { DatePipe } from '@angular/common';
import { TranslateService } from '@ngx-translate/core';
import { LanguageApp } from '../../services/LanguageApp';
import { TranslationService } from '../../services/translation.service';
import { TimeLoadingService } from '../../services/timeloading.service';
import { DataTableDirective } from 'angular-datatables';
// import { AllCommunityModules } from '@ag-grid-community/all-modules';
// import '@ag-grid-community/all-modules/dist/styles/ag-grid.css';
// import '@ag-grid-community/all-modules/dist/styles/ag-theme-alpine.css';
import { Observable } from 'rxjs';
import SampleJson from '../../../assets/resources/csvjson.json';
import { KeycloakService } from 'keycloak-angular';
import { KeycloakProfile } from 'keycloak-js';

@Component({
  selector: 'app-uploadfiles-glm',
  templateUrl: './uploadfiles-glm.component.html',
  styleUrls: ['./uploadfiles-glm.component.css']
})
export class UploadfilesGLMComponent implements OnInit   {

  trainFile: File = null;
  // testFile: File = null;
  uploadedFilesTrain: Array<File> = null;
  // uploadedFilesTest: Array<File> = null;
  modelName: string = null;
  informationWorkflow: any;
  correlationTrain: Array<string[]> = [];
  correlationTest: Array<string[]> = [];
  correlationTrainHeaders;
  correlationTrainBody;
  correlationTestHeaders;
  correlationTestBody;
  headersTemps: Array<string> = [];
  nextNeu = false;
  buttonsGroup = true;
  gifData = false;
  @ViewChildren('columnsI') item;
  selectedIds = [];
  checktable = false;
  inputFileTrain = false;
  inputFileTest = false;
  @ViewChild(DataTableDirective, {static: false}) datatableElement: DataTableDirective;
  dtOptions: any = {};
  currentUser: User;
  pipeDate: string = null;
  host: any;
  columnsDataset: { title: string, data: string } [];
  viewFormGLM = false;




  @ViewChild('name') element: ElementRef;

  public isLoggedIn = false;
  public userProfile: KeycloakProfile | null = null;



  constructor(private http: HttpClient,
              private userService: UserService,
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
    this.columnsDataset = [{title: ' ', data: ' '}];


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
            this.createWorkSpace();

        }



    }


  fileChangeTrain(element): void {
    this.uploadedFilesTrain = element.target.files;
    this.inputFileTrain = true;
  }



  upload(): void {

    this.buttonsGroup = false;
    const formData = new FormData();
    // tslint:disable-next-line:prefer-for-of
    for (let i = 0; i < this.uploadedFilesTrain.length; i++) {
      formData.append('file', this.uploadedFilesTrain[i], this.uploadedFilesTrain[i].name);
    }

    // this.http.post('/api/upload', formData)
    //   .subscribe((response) => {
    //   });
    this.userService.updateFiletoUploads(formData);

    setTimeout(() => {
      this.runningScriptsML();
     }, 1500);

    }


  runningScriptsML(): void {
    this.tableLanguage();
    this.loading.timeLoading();
    this.gifData = true;
    try {

        this.userService.datasetCVSTOJSON(this.currentUser, this.uploadedFilesTrain[0].name, 'uploads')
              .subscribe(
                // tslint:disable-next-line:no-shadowed-variable
                data => {
                  this.informationWorkflow = JSON.parse(data);
                  // this.processLineByLineTrain();
                  this.extractValuesJSONCSVFile();
                  this.nextNeu = true;
                  this.gifData = false;

                  },
              error => {
                });

    }catch (error){
      this.gifData = false;
      this.nextNeu = true;
    }
    this.createWorkSpace();

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


    extractValuesJSONCSVFile(): void {

      const http = this.http;
      const nameFile = this.uploadedFilesTrain[0].name;
      const view = false;

      this.headersTemps = Object.keys(this.informationWorkflow[0]);

      this.headersTemps  = this.headersTemps.toString().split(',');

      console.log(this.informationWorkflow);
      const headers = this.headersTemps.join();

      this.headersTemps.forEach(element => {
      this.columnsDataset.push( {title: element.replace('_', ' '), data: element  });

    });
      this.columnsDataset.shift();
      this.dtOptions = {
        pagingType: 'full_numbers', pageLength: 20,
        data: this.informationWorkflow,
        columns: this.columnsDataset,
        responsive: true,
        orderCellsTop: true,
        deferRender:    true,
        scroller:       true,
        dom: '<"top"fpl><"float"i>rt<"bottom"B><"clear">',
        buttons: [
          {
            extend: 'csv',
            filename: this.uploadedFilesTrain[0].name.split('.')[0],
            text: 'Generate final train dataset',
            action: (e, dt, button, config) => {
                const csvContent = [];
                
                // Add the headers to the CSV
                csvContent.push(',' + headers);
          
                // Add the rows to the CSV
                for (let i = 0; i < dt.rows().data().length; i++) {
                  const rowData = dt.rows().data()[i];
                  const rowArray = Object.values(rowData);
                  rowArray.unshift(''); // Add an empty column at the beginning
                  csvContent.push(rowArray.join(','));
                }
          
                // Convert the array to a CSV string
                const csvString = csvContent.join('\n');
          
                // Create a blob with the CSV content
                const blob = new Blob(['\ufeff' + csvString], { type: 'text/csv;charset=utf-8;' });
          
          
                const formData = new FormData();
                formData.append('file', blob, nameFile);
          
                // Send formData to the server
                this.userService.updateFiletoUploads(formData).subscribe(response => {
                  
                console.log(response);
              });
            }
          }
        ]
      };

    }





   tableLanguage(): void{
      if (this.transLang.activeLang === 'es'){

      this.dtOptions = { pagingType: 'full_numbers', pageLength: 10, language: LanguageApp.spanishdatatables };
    }else{
      this.dtOptions = { pagingType: 'full_numbers', pageLength: 10};
    }
  }

}
