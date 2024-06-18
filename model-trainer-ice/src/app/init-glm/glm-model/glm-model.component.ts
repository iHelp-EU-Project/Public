import { Component, OnInit, Input, ViewChild, AfterViewInit } from '@angular/core';
// import { UploadFileComponent } from '../../uploadfile/uploadfile.component';
import { HttpClient } from '@angular/common/http';
// import { Observable } from 'rxjs';
import { ToastrService } from 'ngx-toastr';
import { UserService } from '../../services/index_services';
import {BackendscriptsService} from '../../services/backendscripts.service';
import { User } from '../../models/index_models';
import { FormControl, FormGroup, FormBuilder, Validators  } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { TranslateService } from '@ngx-translate/core';
import { LanguageApp } from '../../services/LanguageApp';
import { TranslationService } from '../../services/translation.service';
import { TimeLoadingService } from '../../services/timeloading.service';
import { DataTableDirective } from 'angular-datatables';
import { KeycloakService } from 'keycloak-angular';
import { KeycloakProfile } from 'keycloak-js';


@Component({
  selector: 'app-glm-model',
  templateUrl: './glm-model.component.html',
  styleUrls: ['./glm-model.component.css']
})
export class GlmModelComponent implements OnInit, AfterViewInit {

  @Input() modelName: string;
  @Input() uploadedFilesTrain: Array<File>;
  @Input() uploadedFilesTest: Array<File>;
  @Input() selectsIds: any;
  @Input() buttonCorreWorking: boolean;
  @Input() dateModelCreateModels: string;
  @Input() correlationTrainHeaders;
  @Input() dtOptions: any;
  @Input() correlationTrainName: string;


  declare viewFormGLM: any;





  @ViewChild(DataTableDirective, {static: false}) datatableElement: DataTableDirective;


  selected: FormControl = new FormControl(null);
  opc: any;

  informationWorkflow: string;
  gifCorr = false;
  correlationTrain: Array<string[]> = [];
  headersTemps: Array<string> = [];

  columnsId = ' ';
  sendColumnsIDSecondWorkflow = '';

  sendColumnsIDSecondWorkflowEnd = '';

  checktable = false;

  csvDataRegresion;

  viewtable = false;

  currentUser: User;
  corrView = false;
  anomaly = false;
  nextNeu = false;

  public chartArrayTrain;
  csvDataTrain: Array<string[]> = [];

  choseForBuildModelKmeans = [];
  choseDeleteColumns = [];


  choseForBuildModelKmeansString;
  choseDeleteColumnsString;

  blobbox = false;

  numTrain: number;


  choseRegresionColumnsSplit;
  columnsDataset: { title: string, data: string } [];



  family;
  split;
  seed;
  solver;
  maxIteration;
  lamb;
  standardize;
  missing;
  link;
  tweedie;
  ignore;
  lambdasearch;
  alpha;
  nfolds;
  keeppred;
  columnDepentVar;
  valuesNan;

  reactiveForm: FormGroup;


  csv: any;

  host: any;
  public isLoggedIn = false;
  public userProfile: KeycloakProfile | null = null;

  constructor(private http: HttpClient,
              private toastr: ToastrService,
              private userService: UserService,
              private backEndPython: BackendscriptsService,
              private translate: TranslateService,
              private transLang: TranslationService,
              public loading: TimeLoadingService,
              private builder: FormBuilder,
              private readonly keycloak: KeycloakService) {

    // this.currentUser = JSON.parse(localStorage.getItem('currentUser'));
    this.currentUser = { id: '', username: '', firstName: '', lastName: '' };
    this.host = self.location.host.split(':')[0];
    this.columnsDataset = [{title: ' ', data: ' '}];


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

    // this.extractValuesJSONCSVFile();
    this.selected.valueChanges.subscribe(changes => {
      this.Opciones(changes);
    });

    this.reactiveForm = this.builder.group({
      family: [null, Validators.required],
      split: [null, Validators.required],
      seed: [null, Validators.required],
      solver: [null, Validators.required],
      maxIteration: [null, Validators.required],
      lamb: [null, Validators.required],
      standardize: [null, Validators.required],
      missing: [null, Validators.required],
      link: [null, Validators.required],
      tweedie: [null, Validators.required],
      ignore: [null, Validators.required],
      lambdasearch: [null, Validators.required],
      alpha: [null, Validators.required],
      nfolds: [null, Validators.required],
      keeppred: [null, Validators.required],
      columnDepentVar: [null, Validators.required],
      valuesNan: [null, Validators.required],

    });
  }


  Opciones(opc): any {
   console.log(opc);

  }

  buscar(): any {
    console.log(this.opc);
  }

  ngAfterViewInit(): void {
    const userservice: UserService = this.userService;
    const file: string = this.correlationTrainName;
    const user: User = this.currentUser;
    const headerColumns = this.correlationTrainHeaders;
    // tslint:disable-next-line:prefer-const
    let view = false;
    let select: any;
    let count = -1;
    this.datatableElement.dtInstance.then((dtInstance: DataTables.Api) => {
      // tslint:disable-next-line:typedef
      dtInstance.columns().every(function() {
        count++;
        const column = this;
        // tslint:disable-next-line:max-line-length
        select = $('<img src="assets/images/filter.png" width="10%" height="10%"><div class="align-items-center" id="' + headerColumns[count] + '_selimg"><img  style="height: 15%; width: 15%; margin-left=50%;" src="assets/images/833.gif"/></div><select id="' + headerColumns[count] + '_sel" style="width: 150px;" class="selectpicker"><option  value=""></option></select>')
          .appendTo($(column.header()).empty())
          // tslint:disable-next-line:typedef
          .on('change', function() {
            const val = $.fn.dataTable.util.escapeRegex(
               // tslint:disable-next-line:no-string-literal
               this['value']
            );

            column
              .search(val ? '^' + val + '$' : '', true, false)
              .draw();

          });



      });
      userservice.selectUniqueValuesPost(user, file, headerColumns)
       .subscribe(
          data => {
            console.log(data);
            userservice.readLoadingJSONValuesColumsToSelect(user, this.modelName, '')
              .subscribe(dataSelec => {
                console.log(dataSelec);
                // dataSelec = JSON.stringify(dataSelec);
                // console.log(dataSelec);

                count = 0;
                // column.dataSrc = data;
                // column.data().unique().each(function (d, j) {e
                JSON.parse(dataSelec).forEach(element => {
                  // console.log(element[headerColumns[count]]);
                  // Object.entries(element).forEach(([key, value]) => {
                  // console.log(`${key} ${value}`);
                  const ele = element.split('-');
                  const key = ele[0];
                  const value = ele[1].replace(/_/g, '-');;
                  const selectOp = document.getElementById(key + '_sel') as HTMLSelectElement;
                  // const selectOp = document.getElementById(`${key}` + '_sel') as HTMLSelectElement;

                  const option = document.createElement('option') as HTMLOptionElement;
                  // selectOp.append('<option  value="' + `${value}` + '">' +  `${value}` + '</option>');
                  // option.value = `${value}`;
                  // option.label = `${value}`;
                  option.value = value;
                  option.label = value;
                  selectOp.appendChild(option);
                  // const selectOpDivProgress = document.getElementById(`${key}` + '_selimg') as HTMLDivElement;
                  const selectOpDivProgress = document.getElementById(key + '_selimg') as HTMLDivElement;
                  selectOpDivProgress.hidden = true;
                  count === headerColumns.length ? this.viewtable = true : count++;
                  // count === headerColumns.length ? dataOptions = { processing: false } : dataOptions = { processing: true };


                  // });


                });
              });

          });
    });
  }



   saveColumnsIDstring(id): string {

    for (const j of id) {
      this.columnsId += ',' + j.id;
      console.log(this.columnsId);
    }
    const temp  = this.columnsId.substring(2);
    console.log(temp);
    return temp;

  }

  // tableFromView(): any {

  //   let view: any;
  //   view = this.dtOptions.buttons[0].action;
  //   console.log('Hola  ' + view);
  //   if (view){
  //     console.log('Hola  ' + view);
  //     this.viewFormGLM = true;
  //   }

  // }

  runningScriptsMLKmeansEnd(): void {
    this.backEndPython.loginAutorization(this.currentUser, this.isLoggedIn.toString());
    setTimeout(() => {

      this.tableLanguage();
      this.loading.timeLoading();
      this.gifCorr = true;
      this.corrView = true;
      this.choseForBuildModelKmeansString = this.saveColumnsIDstring(this.choseForBuildModelKmeans);
      this.choseDeleteColumnsString = this.saveColumnsIDstring(this.choseDeleteColumns);
      this.choseDeleteColumnsString = this.choseDeleteColumns.length < 1 ? '_' : this.choseDeleteColumnsString;

      try {
        this.http.get(`http://${this.host}:5002/scripts/GLMModel/${this.modelName}/${this.correlationTrainName}/${this.choseForBuildModelKmeansString}/${this.family}/${this.split}/${this.seed}/${this.solver}/${this.maxIteration}/${this.lamb}/${this.standardize}/${this.missing}/${this.link}/${this.tweedie}/${this.ignore}/${this.lambdasearch}/${this.alpha}/${this.nfolds}/${this.keeppred}/${this.columnDepentVar}/${this.currentUser.username}/${this.choseDeleteColumnsString}/${this.valuesNan}`, { responseType: 'text' }).subscribe(data => {
          this.informationWorkflow = data as string;
          this.gifCorr = false;
          this.nextNeu = true;
        });
      } catch (error) {
        this.gifCorr = false;
        this.nextNeu = true;
      }
    }, 1500);

  }


onTabChanged($event): void {
    const clickedIndex = $event.index;

    console.log(clickedIndex);
    switch (clickedIndex) {
      case 1:

        break;

      case 2:


        break;

      default:
        break;
    }

  }

  OnCheckboxSelectDeleteColumns(id, event): void {


      if (event.target.checked === true) {
        // tslint:disable-next-line: object-literal-shorthand
        this.choseDeleteColumns.push({ id: id, checked: event.target.checked });
      }
      if (event.target.checked === false) {
        this.choseDeleteColumns = this.choseDeleteColumns.filter((item) => item.id !== id);
      }

      // if (this.choseDeleteColumns.length < 2){
      //   this.checktable = false;
      // } else{
      //   this.checktable = true;
      // }
    }


  OnCheckboxSelectKmeansChooseIDBuildModels(id, event): void {


      if (event.target.checked === true) {
        // tslint:disable-next-line: object-literal-shorthand
        this.choseForBuildModelKmeans.push({ id: id, checked: event.target.checked });
      }
      if (event.target.checked === false) {
        this.choseForBuildModelKmeans = this.choseForBuildModelKmeans.filter((item) => item.id !== id);
      }

      if (this.choseForBuildModelKmeans.length < 2){
        this.checktable = false;
      } else{
        this.checktable = true;
      }
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
