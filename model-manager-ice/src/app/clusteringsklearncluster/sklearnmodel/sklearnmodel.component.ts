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
  selector: 'app-sklearnmodel',
  templateUrl: './sklearnmodel.component.html',
  styleUrls: ['./sklearnmodel.component.css']
})
export class SklearnmodelComponent implements OnInit, AfterViewInit {

  @Input() modelName: string;
  @Input() uploadedFilesTrain: Array<File>;
  @Input() selectedIdsRowsSecondworkflow: string;
  @Input() selIds: string;
  @Input() selectedIdsRows: string;
  @Input() selectedIdsRowsSecondworkflowEnd: string;
  @Input() choseRegresionColumns: string;
  @Input() buttonCorreWorking: boolean;
  @Input() dateModelCreateModels: string;
  @Input() csvDataTrainBody;
  @Input() withoutQuotesAnomalyesTrainHeaders;
  @Input() correlationTrainHeaders;
  @Input() dtOptions: any;
  @Input() correlationTrainName: string;
  @Input() correlationLabelName: string;
  @Input() uploadFileDataset: boolean;


  @ViewChild(DataTableDirective, {static: false}) datatableElement: DataTableDirective;


  viewtable = false;

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

  currentUser: User;
  corrView = false;
  anomaly = false;
  nextNeu = false;

  public chartArrayTrain;
  csvDataTrain: Array<string[]> = [];

  choseForBuildModelKmeans = [];
  choseDeleteColumns = [];

  columnsPredict;
  choseDeleteColumnsString;

  fileNameLabel;

  blobbox = false;

  numTrain: number;

  host: any;


  reactiveForm: FormGroup;


  randomState;
  deleteColumns;
  init;
  nInit;
  maxIteration;
  clusterK;
  classColumn;
  valuesNan;
  advancedModel;

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
        this.selected.valueChanges.subscribe(changes => {
          this.Opciones(changes);
        });

        this.reactiveForm = this.builder.group({
          randomState: [null, Validators.required],
          nInit: [null, Validators.required],
          init: [null, Validators.required],
          max_iteration: [null, Validators.required],
          cluster_k: [null, Validators.required],
          classColumn: [null, Validators.required],
          valuesNan: [null, Validators.required],
          advancedModel: [null, Validators.required],


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
    const dataOptions = this.dtOptions;
    let count = -1;
    this.viewtable = true;
    let select: any;
    this.datatableElement.dtInstance.then((dtInstance: DataTables.Api) => {
      // tslint:disable-next-line:typedef
      dtInstance.columns().every(function() {
        count++;
        const column = this;
        // tslint:disable-next-line:max-line-length
        select = $('<div class="align-items-center" id="' + headerColumns[count] + '_selimg"><img  style="height: 15%; width: 15%; margin-left=50%;" src="assets/images/833.gif"/></div><img src="assets/images/filter.png" width="10%" height="10%"><select id="' + headerColumns[count] + '_sel" style="width: 175px;" class="selectpicker"><option  value=""></option></select>')  //
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
                  const value = ele[1].replace(/_/g, '-');
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


                  // });


                });
              });

          });

    });

  }

    /**
     * Serialize id
     *
     */
   saveColumnsIDstring(id): string {

    this.columnsId = '';

    for (const j of id) {
      this.columnsId += ',' + j.id;
      console.log(this.columnsId);
    }
    while (this.columnsId.charAt(0) === ',' || this.columnsId.charAt(0) === ' '){
      this.columnsId  = this.columnsId.substring(1);
    }
    console.log(this.columnsId);
    return this.columnsId;

  }

   /**
    * Get name clases for graphics dynamic refresh
    */
  getNameClases(): string{

    const selectOpClassName = document.getElementById(this.classColumn + '_sel') as HTMLSelectElement;
    console.log('##############: ' + selectOpClassName);
    const listItem =  selectOpClassName.options;
    let out = '';
    const arrai = [];
    // tslint:disable-next-line:prefer-for-of
    for (let i = 1; i < listItem.length; i++) {

          arrai.push(listItem[i].value);
          // out +=  ',' + listItem[i].value ;
    }
    const dataArr = new Set(arrai);

    const result = [...dataArr];

    result.forEach(element => {
        out +=  ',' + element ;
    });


    return out.substring(1).replace(/ /g, '_');


  }


  runningScriptsMLKmeansEnd(): void {
    this.backEndPython.loginAutorization(this.currentUser, this.isLoggedIn.toString());
    setTimeout(() => {

      this.tableLanguage();
      this.loading.timeLoading();
      this.gifCorr = true;
      this.corrView = true;
      this.columnsPredict = this.saveColumnsIDstring(this.choseForBuildModelKmeans);
      this.choseDeleteColumnsString = this.saveColumnsIDstring(this.choseDeleteColumns);
      this.choseDeleteColumnsString = this.choseDeleteColumns.length < 1 ? '_' : this.choseDeleteColumnsString;
      try {
        // tslint:disable-next-line: max-line-length
        this.http.get(`/scripts/ScriptsSklearn/${this.modelName}/${this.currentUser.username}/${this.correlationTrainName}/${this.init}/${this.maxIteration}/${this.clusterK}/${this.randomState}/${this.choseDeleteColumnsString}/${this.classColumn}/${this.columnsPredict}/${this.nInit}/${this.getNameClases()}/${this.valuesNan}/${this.advancedModel}`, { responseType: 'text' }).subscribe(data => {
          this.informationWorkflow = data as string;
          // this.getData('regression', this.csvDataRegresion);
          // this.processLineByLineTrain();
          
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

  OnCheckboxSelectKmeansChooseIDBuildModelsALL(event): void {

    const allCheckBoxes = document.querySelectorAll('input[type="checkbox"]') as NodeListOf<HTMLInputElement>;
    allCheckBoxes.forEach(checkBox => {
      // console.log(checkBox.id, );
      // if (checkBox && checkBox.checked) {
      //   checkBox.checked = true;
      // }
      const checkBoxDel = checkBox.id.split('_');
      console.log(checkBoxDel[checkBoxDel.length - 1]);
      if (event.target.checked === true && checkBoxDel[checkBoxDel.length - 1] !== 'del') {
        checkBox.checked = true;
        // tslint:disable-next-line: object-literal-shorthand
        this.choseForBuildModelKmeans.push({ id: checkBox.id, checked: event.target.checked });
        console.log(this.choseForBuildModelKmeans);
      }
      if (event.target.checked === false && checkBoxDel[checkBoxDel.length - 1] !== 'del' ) {
        checkBox.checked = false;
        this.choseForBuildModelKmeans = this.choseForBuildModelKmeans.filter((item) => item.id !== checkBox.id);
      }
    });


    if (this.choseForBuildModelKmeans.length < 1){
      this.checktable = false;
    } else{
      this.checktable = true;
    }
  }

  OnCheckboxSelectKmeansChooseIDBuildModelsDeleteALL(event): void {

    const allCheckBoxes = document.querySelectorAll('input[type="checkbox"]') as NodeListOf<HTMLInputElement>;
    allCheckBoxes.forEach(checkBox => {
      // console.log(checkBox.id, );
      // if (checkBox && checkBox.checked) {
      //   checkBox.checked = true;
      // }
      const checkBoxDel = checkBox.id.split('_');
      console.log(checkBoxDel[checkBoxDel.length - 1]);
      if (event.target.checked === true && checkBoxDel[checkBoxDel.length - 1] === 'del') {
        checkBox.checked = true;
        // tslint:disable-next-line: object-literal-shorthand
        this.choseForBuildModelKmeans.push({ id: checkBox.id, checked: event.target.checked });
        console.log(this.choseForBuildModelKmeans);
      }
      if (event.target.checked === false && checkBoxDel[checkBoxDel.length - 1] === 'del' ) {
        checkBox.checked = false;
        this.choseDeleteColumns = this.choseDeleteColumns.filter((item) => item.id !== checkBox.id);
      }
    });


    if (this.choseForBuildModelKmeans.length < 1){
      this.checktable = false;
    } else{
      this.checktable = true;
    }
  }

  OnCheckboxSelectDeleteColumns(id, event): void {


      if (event.target.checked === true) {
        // tslint:disable-next-line: object-literal-shorthand
        this.choseDeleteColumns.push({ id: id, checked: event.target.checked });
        this.choseForBuildModelKmeans = this.choseForBuildModelKmeans.filter((item) => item.id !== id);

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
