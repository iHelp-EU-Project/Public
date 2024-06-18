import { Component, OnInit, Input, ViewChild } from '@angular/core';
// import { UploadFileComponent } from '../../uploadfile/uploadfile.component';
import { HttpClient } from '@angular/common/http';
// import { Observable } from 'rxjs';
import { ToastrService } from 'ngx-toastr';
import { UserService } from '../../services/index_services';
import {BackendscriptsService} from '../../services/backendscripts.service';
import { User } from '../../models/index_models';
import { TranslateService } from '@ngx-translate/core';
import { LanguageApp } from '../../services/LanguageApp';
import { TranslationService } from '../../services/translation.service';
import { TimeLoadingService } from '../../services/timeloading.service';
import { KeycloakService } from 'keycloak-angular';
import { KeycloakProfile } from 'keycloak-js';

import {
  ChartComponent,
  ApexAxisChartSeries,
  ApexTitleSubtitle,
  ApexDataLabels,
  ApexChart,
  ApexPlotOptions,
  ApexXAxis,
  ApexStroke
} from 'ng-apexcharts';

export type ChartOptions = {
  series: ApexAxisChartSeries;
  chart: ApexChart;
  dataLabels: ApexDataLabels;
  fill: any;
  colors: any;
  stroke: ApexStroke;
  title: ApexTitleSubtitle;
  xaxis: ApexXAxis;
  plotOptions: ApexPlotOptions;
};

@Component({
  selector: 'app-neuronalnetworkingcolumnsid-corre',
  templateUrl: './neuronalnetworkingcolumnsid-corre.component.html',
  styleUrls: ['./neuronalnetworkingcolumnsid-corre.component.css']
})
export class NeuronalnetworkingcolumnsidCorreComponent implements OnInit {

   // data gone previus component (neuronalnetwork- updaload file)
  @Input() modelName: string;
  @Input() uploadedFilesTrain: Array<File>;
  @Input() uploadedFilesTest: Array<File>;
  @Input() selectsIds: any;
  @Input() buttonCorreWorking: boolean;
  @Input() dateModelCreateModels: string;

  dtOptions: DataTables.Settings = {pagingType: 'full_numbers', pageLength: 10};


  informationWorkflow: string;
  gifCorr = false;
  correlationTrain: Array<string[]> = [];
  correlationTest: Array<string[]> = [];
  columnsId = ' ';
  sendColumnsID = '';

  selectedIdsRows = [];

  checktable = false;


  currentUser: User;
  corrView = false;
  anomaly = false;
  nextNeu = false;

  csvDataTrainHeader;
  csvDataTrainBody;
  csvDataTrain: Array<string[]> = [];

  csvDataTestHeader;
  csvDataTestBody;
  csvDataTest: Array<string[]> = [];

  public isLoggedIn = false;
  public userProfile: KeycloakProfile | null = null;

  host: any;

  constructor(private http: HttpClient,
              private toastr: ToastrService,
              private userService: UserService,
              private backEndPython: BackendscriptsService,
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

  showToaster(): void
  {
      this.toastr.success(this.translate.instant('Selectleast.text') );
  }



  transformRowsData(array: string[], arrayColumns: Array<string[]>): { x: string; y: number; }[]{


    const tmp: {x: string; y: number; } = null;
    const final: {x: string; y: number; }[] =  [];

    for (let i = 1; i <= array.length - 1; i++ ){
      final.push({x : arrayColumns[0][i],
                  y :  parseFloat(array[i])});
      }


    return final;
 }

  transformRowsDataName(array: string[]): string{
      return array[0];
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


  runningScriptsMLCorrID(): void {
    this.backEndPython.loginAutorization(this.currentUser, this.isLoggedIn.toString());
    setTimeout(() => {

      this.loading.timeLoading();
      this.tableLanguage();
      this.gifCorr = true;
      this.corrView = true;
      this.sendColumnsID = this.saveColumnsIDstring(this.selectsIds);
      try {
        // tslint:disable-next-line: max-line-length
        this.http.get(`/scripts/scriptsmlCorrid/${this.modelName}/${this.uploadedFilesTrain[0].name}/${this.uploadedFilesTest[0].name}/${this.sendColumnsID}/${this.currentUser.username}`, { responseType: 'text' }).subscribe(data => {
          this.informationWorkflow = data as string;
          this.processLineByLineTrain();
          this.gifCorr = false;
          this.nextNeu = true;


        });

      } catch (error) {
        this.gifCorr = false;
        this.nextNeu = true;
      }
    }, 1500);


  }

  processLineByLineTrain(): void {
    let numTrain = 0;
    const filesModelTrain: string[] = [this.uploadedFilesTrain[0].name];
    const expression = /\s* \s*/;
    const arrlines = this.informationWorkflow.split(/\r?\n/);
    for (let i = 0, strLen = arrlines.length; i < strLen; i++) {
      if (arrlines[i].includes('END-TESTING')) {
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
    this.csvDataTrainHeader = [];
    this.csvDataTrainBody = [];

    this.csvDataTrainBody = this.correlationTrain.slice();
    this.csvDataTrainBody.shift();
    this.csvDataTrainHeader = this.csvDataTrainBody.shift();
    this.csvDataTrainBody.shift();
    this.csvDataTrainBody.pop();
  }

  processLineByLineTest(st: string[], n: number, s: RegExp): void{
    const arrlines = st;
    let numTest = 0;
    const filesModelTest: string[] = [this.uploadedFilesTest[0].name];

    for (let i = n, strLen = arrlines.length; i < strLen; i++) {
      if (arrlines[i].includes('END-TRAIN')) {
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
    this.csvDataTestHeader = [];
    this.csvDataTestBody = [];

    this.csvDataTestBody = this.correlationTest.slice();
    this.csvDataTestBody.shift();
    this.csvDataTestHeader = this.csvDataTestBody.shift();
    this.csvDataTestBody.shift();
    this.csvDataTestBody.pop();

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

  OnCheckboxSelect(id, event): void {

      this.selectedIdsRows = [];

      if (event.target.checked === true) {
        // tslint:disable-next-line: object-literal-shorthand
        this.selectedIdsRows.push({ id: id, checked: event.target.checked });
      }
      if (event.target.checked === false) {
        this.selectedIdsRows = this.selectedIdsRows.filter((item) => item.id !== id);
      }

      if (this.selectedIdsRows.length < 1){
        this.checktable = false;
      } else{
        this.checktable = true;
      }

      this.csvDataTrainBody.forEach(it => {
            console.log(it[0]);
            if (it[0] !== id){
              try {
                // tslint:disable-next-line:no-string-literal
                document.getElementById(it[0])['checked'] = false;
              } catch (error) {
                console.log('Not yet generated in the WEB UI, ignore');
              }
            }else{
              // it.id = true;
              // tslint:disable-next-line:no-string-literal
              document.getElementById(it[0])['checked'] = true;
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
