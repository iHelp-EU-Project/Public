import { Component, OnInit, ViewChildren, ViewChild, AfterViewInit, OnDestroy, ElementRef} from '@angular/core';
import { HttpClient } from '@angular/common/http';
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
import { KeycloakService } from 'keycloak-angular';
import { KeycloakProfile } from 'keycloak-js';

@Component({
  selector: 'app-clusteringbirch',
  templateUrl: './clusteringbirch.component.html',
  styleUrls: ['./clusteringbirch.component.css']
})
export class ClusteringbirchComponent implements OnInit  {

   trainFile: File = null;
  labelFile: File = null;
  uploadedFilesTrain: Array<File> = null;
  uploadedFilesLabel: Array<File> = null;
  modelName: string = null;
  informationWorkflow: string;
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
  // @ViewChild(NeuronalnetworkcolumsCorreComponent) networkcolumsCorreComponent;
  // corrView = true;
  inputFileTrain = false;
 inputFileLabel = false;
   // dtOptions: DataTables.Settings = {pagingType: 'full_numbers', pageLength: 10 };
  // dtOptionsTest: DataTables.Settings = {pagingType: 'full_numbers', pageLength: 10, };
  @ViewChild(DataTableDirective, {static: false}) datatableElement: DataTableDirective;
  dtOptions: any = {};
  currentUser: User;
  pipeDate: string = null;
  host: any;
  columnsDataset: { title: string, data: string } [];
  public isLoggedIn = false;
  public userProfile: KeycloakProfile | null = null;
  uploadFileDataset = false;

  // loadNumberProgress: {progress: number, state: string};
  // interval;

  @ViewChild('name') element: ElementRef;

  constructor(private http: HttpClient,
              private userService: UserService,
              private alertService: AlertService,
              private toastr: ToastrService,
              private datePipe: DatePipe,
              private translate: TranslateService,
              private transLang: TranslationService,
              public loading: TimeLoadingService,
              private readonly keycloak: KeycloakService) {

    // Our authentification
    // this.currentUser = JSON.parse(localStorage.getItem('currentUser'));
    // keycloak authentification
    this.currentUser = { id: '', username: '' , firstName: '' , lastName: ''};    this.host = self.location.host.split(':')[0];
    this.columnsDataset = [{title: ' ', data: ' '}];


  }


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

  }

   // fileChangeTrain(element): void {
  //   this.uploadedFilesTrain = element.target.files;
  //   this.inputFileTrain = true;
  // }

  cleanedFileName

  fileChangeTrain(element): void {
    const files = element.target.files;
    const updatedFiles: File[] = [];

    // Verificar y corregir caracteres extraños en el nombre del archivo
    for (let i = 0; i < files.length; i++) {
        const originalFileName = files[i].name;
        this.cleanedFileName = this.cleanFileName(originalFileName);

        // Si el nombre del archivo ha cambiado, crear un nuevo archivo con el nombre corregido
        if (originalFileName !== this.cleanedFileName) {
            // Puedes mostrar un mensaje o hacer cualquier otra acción aquí
            console.log(`Nombre del archivo corregido: ${this.cleanedFileName}`);
            const updatedFile = new File([files[i]], this.cleanedFileName);
            updatedFiles.push(updatedFile);
        } else {
            updatedFiles.push(files[i]);
        }
    }

    // Asignar la nueva lista actualizada a this.uploadedFilesTrain
    this.uploadedFilesTrain = updatedFiles;
    this.inputFileTrain = true;
}

cleanFileName(fileName: string): string {
    // Utilizar una expresión regular para eliminar espacios en blanco y paréntesis
    const cleanedName = fileName.replace(/[^\w.-]/g, '_').replace(/\s+/g, '_').replace(/[_()]+/g, '_');
    return cleanedName;
}


  fileChangeLabel(element): void {
    this.uploadedFilesLabel = element.target.files;
    this.inputFileLabel = true;
  }

  // showToaster(): void
  // {
  //     this.toastr.success('Error, Select at least one column of the table');
  // }


  upload(): void {

    this.buttonsGroup = false;
    const formData = new FormData();
    // tslint:disable-next-line:prefer-for-of
    for (let i = 0; i < this.uploadedFilesTrain.length; i++) {
      formData.append('file', this.uploadedFilesTrain[i], this.uploadedFilesTrain[i].name);
    }
    // tslint:disable-next-line:prefer-for-of
    // for (let i = 0; i < this.uploadedFilesLabel.length; i++) {
    //   formData.append('uploads[]', this.uploadedFilesLabel[i], this.uploadedFilesLabel[i].name);

    // }
    // this.http.post('http://' + this.host + ':3001/users/files/upload', formData)
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

      this.headersTemps = Object.keys(this.informationWorkflow[0]);

      this.headersTemps  = this.headersTemps.toString().split(',');

      // console.log(SampleJson);
      console.log(this.informationWorkflow);
      const headers = this.headersTemps.join();

      this.headersTemps.forEach(element => {
      // this.columnsDataset = ;
      this.columnsDataset.push( {title: element.replace('_', ' '), data: element  });

    });
      this.columnsDataset.shift();
      this.dtOptions = {
        pagingType: 'full_numbers', pageLength: 20,
        data: this.informationWorkflow,
        columns: this.columnsDataset,
        responsive: true,
        orderCellsTop: true,
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
                  this.uploadFileDataset = true;

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
