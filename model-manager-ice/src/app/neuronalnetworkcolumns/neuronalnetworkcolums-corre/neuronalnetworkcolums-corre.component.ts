import { Component, OnInit, Input, ViewChild, OnChanges } from '@angular/core';
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


// import * as d3 from 'd3';
// import ApexCharts from 'apexcharts'
// import { MatTabChangeEvent } from '@angular/material/';
// import { strict } from 'assert';


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
  selector: 'app-neuronalnetworkcolums-corre',
  templateUrl: './neuronalnetworkcolums-corre.component.html',
  styleUrls: ['./neuronalnetworkcolums-corre.component.css']
})


export class NeuronalnetworkcolumsCorreComponent implements OnInit{

   // data gone previus component (neuronalnetwork- updaload file)
  @Input() modelName: string;
  @Input() uploadedFilesTrain: Array<File>;
  @Input() uploadedFilesTest: Array<File>;
  @Input() selectsIds: any;
  @Input() buttonCorreWorking: boolean;
  @Input() dateModelCreateModels: string;

  informationWorkflow: string;
  gifCorr = false;
  correlationTrain: Array<string[]> = [];
  correlationTest: Array<string[]> = [];
  columnsId = ' ';
  sendColumnsID = '';

  file: File;
  numPerarson = 0;
  numKendall = 0;
  numSpearman = 0;
  numHeadmapID = 0;

  csvUrlPearsonCorrelation: string[];
  csvUrlKendallCorrelation: string[];
  csvUrlSpearmanCorrelation: string[];
  csvUrlChoseIDColumnsCorrelation: string[];
  public urls;

  nextNeu = false;
  gifData = false;
  anomaly = false;

  csvDataPearson: Array<string[]> = [];
  csvDataKendall: Array<string[]> = [];
  csvDataSpearman: Array<string[]> = [];
  csvDataHeadMapID: Array<string[]> = [];

  csvDataPearsonHeader;
  csvDataKendallHeader;
  csvDataSpearmanHeader;
  csvDataPearsonBody;
  csvDataKendallBody;
  csvDataSpearmanBody;

  public chartArrayPearson;
  public chartArrayKendall;
  public chartArraySpearman;
  public chartArrayHeadMapID;


  public kendallCorr;
  public spearmanCorr;

  corrView = false;


  pearson = true;
  kendall = false;
  spearman = false;

  host: any;

    // Load setting graphics

  @ViewChild('chartPearsonCorr') chartPearsonCorr: ChartComponent;
  @ViewChild('chartKendallCorr') chartKendallCorr: ChartComponent;
  @ViewChild('chartSpearmanCorr') chartSpearmanCorr: ChartComponent;
  @ViewChild('chartHeadmapIDCorr') chartHeadmapIDCorr: ChartComponent;

  public chartOptionsPearsonCorr: Partial<ChartOptions>;
  public chartOptionsKendallCorr: Partial<ChartOptions>;
  public chartOptionsSpearmanCorr: Partial<ChartOptions>;
  public chartOptionsHeadmapIDCorr: Partial<ChartOptions>;
  currentUser: User;

  public isLoggedIn = false;
  public userProfile: KeycloakProfile | null = null;



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

    this.csvUrlPearsonCorrelation = [this.modelName + '.' + this.dateModelCreateModels,
                                     this.modelName + '.' + this.dateModelCreateModels + '_pearson_correlation.csv'];

    this.csvUrlKendallCorrelation = [this.modelName + '.' + this.dateModelCreateModels,
                                    this.modelName + '.' + this.dateModelCreateModels + '_kendall_correlation.csv'];

    this.csvUrlSpearmanCorrelation = [this.modelName + '.' + this.dateModelCreateModels,
                                      this.modelName + '.' + this.dateModelCreateModels + '_spearman_correlation.csv'];

    this.csvUrlChoseIDColumnsCorrelation = [this.modelName + '.' + this.dateModelCreateModels,
                                           'perfectMatrixheatmap1.csv'];






  }

  showToaster(): void
  {
      this.toastr.success(this.translate.instant('Correlation.text') );
  }


  perarsonCorr(): void{


    this.chartOptionsPearsonCorr = {
      chart: {
        height: 550,
        width: 1000,
        type: 'heatmap',
        animations: {enabled: false},
      },
      stroke: {
        width: 0
      },
      dataLabels: {
        enabled: true,
      },
      colors: ['#008FFB'],
      title: {
        text: this.translate.instant('Correlation.text')  + ' Matrix Pearson'
      }
    };

    this.chartOptionsKendallCorr = {
      chart: {
        height: 550,
        width: 1000,
        type: 'heatmap',
        animations: {enabled: false},
      },
      stroke: {
        width: 0
      },
      dataLabels: {
        enabled: true,
      },
      colors: ['#008FFB'],
      title: {
        text:  this.translate.instant('Correlation.text') + ' Matrix Kendall'
      }
     };

    this.chartOptionsSpearmanCorr = {
      chart: {
        height: 550,
        width: 1000,
        type: 'heatmap',
        animations: {enabled: false},
      },
      stroke: {
        width: 0
      },

      dataLabels: {
        enabled: true,
      },
      colors: ['#008FFB'],
      title: {
        text: this.translate.instant('Correlation.text') + ' Matrix Spearman'
      }
    };

    this.chartOptionsHeadmapIDCorr = {
      chart: {
        height: 550,
        width: 1000,
        type: 'heatmap',
        animations: {enabled: false},
      },
      stroke: {
        width: 0
      },
       plotOptions: {
        heatmap: {
          radius: 30,
          enableShades: true,
          colorScale: {
            ranges: [
              {
                from: -1.0,
                to: 0.1999999,
                color: '#ffc2d5'
              },
              {
                from: 0.2,
                to: 0.4999999,
                color: '#ffa1c0'
              },
              {
                from: 0.5,
                to: 0.6999999,
                color: '#ff7fac'
              },
               {
                from: 0.7,
                to: 0.9999999,
                color: '#ff5799'
              },
                {
                from: 1.0,
                to: 1.0,
                color: '#ff0d86'
              }
            ]
          }
        }
      },
      dataLabels: {
        enabled: true,
        style: {
          colors: ['#fff']
        }
      },
      xaxis: {
        type: 'category'
      },
      title: {
        text: this.translate.instant('Correlation.text')  + ' Matrix Columns'
      }
    };




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




  // Get raw data of rest api for serialize data for view in graphics and tables
  getData(typeCorrelation: string, url: string[]): any {


      this.userService.downloadJSONFiles(this.currentUser, url[1], url[0] ).subscribe(data => {
        const list = data.split('\n');
        if (typeCorrelation === 'pearson') {
          this.chartArrayPearson = [];
          this.csvDataPearsonHeader = [];
          this.csvDataPearsonBody = [];
        }
        if (typeCorrelation === 'kendall') {
          this.chartArrayKendall = [];
          this.csvDataKendallHeader = [];
          this.csvDataKendallBody = [];
        }
        if (typeCorrelation === 'sperman') {
          this.chartArraySpearman = [];
          this.csvDataSpearmanHeader = [];
          this.csvDataSpearmanBody = [];
        }
        if (typeCorrelation === 'headmapID') {
          this.chartArrayHeadMapID = [];
        }

        list.forEach(e => {
          // for (let i = 1; i < list.length ; i++) {}
            switch (typeCorrelation) {
              case 'pearson':
                this.csvDataPearson.push(e.split(',')); // tables
                if (this.numPerarson !== 0) {
                  this.chartArrayPearson.push({name: this.transformRowsDataName(e.split(',')),
                                        data: this.transformRowsData(e.split(','), this.csvDataPearson) }); // charts correlation matrix
                  console.log(this.transformRowsData(e.split(','), this.csvDataPearson), 'Pearson');
                } else {
                  this.numPerarson = 1;
                  console.log('header table out: ', e.split(','));
                }
                break;

              case 'kendall':
                this.csvDataKendall.push(e.split(','));
                if (this.numKendall !== 0) {
                this.chartArrayKendall.push({name: this.transformRowsDataName(e.split(',')),
                                              data: this.transformRowsData(e.split(','), this.csvDataKendall) });
                console.log(this.transformRowsData(e.split(','), this.csvDataKendall), 'kendall');
                } else {
                  this.numKendall = 1;
                  console.log('header table out: ', e.split(','));
                }
                break;

              case 'sperman':
                this.csvDataSpearman.push(e.split(','));
                if (this.numSpearman !== 0) {
                this.chartArraySpearman.push({name: this.transformRowsDataName(e.split(',')),
                                              data: this.transformRowsData(e.split(','), this.csvDataSpearman) });
                console.log(this.transformRowsData(e.split(','), this.csvDataSpearman), 'sperman');
                 } else {
                  this.numSpearman = 1;
                  console.log('header table out: ', e.split(','));
                }
                break;

                case 'headmapID':
                this.csvDataHeadMapID.push(e.split(','));
                if (this.numHeadmapID !== 0) {
                this.chartArrayHeadMapID.push({name: this.transformRowsDataName(e.split(',')),
                                              data: this.transformRowsData(e.split(','), this.csvDataHeadMapID ) });
                console.log(this.transformRowsData(e.split(','), this.csvDataHeadMapID), 'headmapID');
                 } else {
                  this.numHeadmapID = 1;
                  console.log('headmapID table out: ', e.split(','));
                }
                break;

              default:
                break;
            }
          // console.log(e);
        });

          // seeialize data for tables
        if (typeCorrelation === 'pearson') {
          this.csvDataPearsonBody = this.csvDataPearson.slice();
          this.csvDataPearsonHeader = this.csvDataPearsonBody.shift();
          this.csvDataPearsonBody.pop();
        }
        if (typeCorrelation === 'kendall') {
          this.csvDataKendallBody = this.csvDataKendall.slice();
          this.csvDataKendallHeader = this.csvDataKendallBody.shift();
          this.csvDataKendallBody.pop();
        }
        if (typeCorrelation === 'sperman') {
          this.csvDataSpearmanBody = this.csvDataSpearman.slice();
          this.csvDataSpearmanHeader = this.csvDataSpearmanBody.shift();
          this.csvDataSpearmanBody.pop();
        }

      });
    // }
      // console.log(this.urls, 'Harcodeado');
      console.log(this.chartArrayPearson, 'Final pearson');
      console.log(this.chartArrayKendall, 'Final kendall');
      console.log(this.chartArraySpearman, 'Final sperman');
      this.numPerarson = 0;
      this.numKendall = 0;
      this.numSpearman = 0;
      this.numHeadmapID = 0;



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



  // Call to scrit for make models
  runningScriptsMLCorr(): void {
    this.backEndPython.loginAutorization(this.currentUser, this.isLoggedIn.toString());
    setTimeout(() => {

      this.loading.timeLoading();
      this.gifCorr = true;
      this.sendColumnsID = this.saveColumnsIDstring(this.selectsIds);
      try {
        // tslint:disable-next-line: max-line-length
        this.http.get(`http://${this.host}:5002/scripts/scriptsmlCorr/${this.modelName}/${this.uploadedFilesTrain[0].name}/${this.uploadedFilesTest[0].name}/${this.sendColumnsID}/${this.currentUser.username}`, { responseType: 'text' }).subscribe(data => {
          this.informationWorkflow = data as string;
          // this.processLineByLineTrain();
          this.getData('pearson', this.csvUrlPearsonCorrelation);
          this.getData('kendall', this.csvUrlKendallCorrelation);
          this.getData('sperman', this.csvUrlSpearmanCorrelation);
          this.getData('headmapID', this.csvUrlChoseIDColumnsCorrelation);
          this.corrView = true;
          this.gifCorr = false;
          this.perarsonCorr();
          this.anomaly = true;
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
        this.kendallCorr = [];
        this.kendallCorr = this.chartArrayKendall.slice();

        break;

      case 2:
        this.spearmanCorr = [];
        this.spearmanCorr = this.chartArraySpearman.slice();

        break;

      default:
        break;
    }

  }


}
