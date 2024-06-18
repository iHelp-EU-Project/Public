import {
  Component,
  OnInit,
  ViewChild,
  Input,
  OnChanges,
  ViewContainerRef,
  DoCheck,
  AfterContentInit,
  AfterContentChecked,
  AfterViewChecked,
  AfterViewInit,
  OnDestroy
} from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ToastrService } from 'ngx-toastr';
import { UserService } from '../services/index_services';
import { User } from '../models/index_models';
import { ActivatedRoute } from '@angular/router';
import { MymodelsComponent } from '../mymodels/mymodels.component';
import { TranslateModule } from '@ngx-translate/core';
import { TranslateService } from '@ngx-translate/core';
import { LanguageApp } from '../services/LanguageApp';
import { TranslationService } from '../services/translation.service';
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
  ApexStroke,
  ApexTooltip,
  ApexMarkers,
  ApexYAxis,
  ApexGrid,
  ApexLegend
} from 'ng-apexcharts';

export type ChartOptionsCorre = {
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



export type ChartOptionsAnomalies = {
  series: ApexAxisChartSeries;
  chart: ApexChart;
  title: ApexTitleSubtitle;
  xaxis: ApexXAxis,
  tooltip: ApexTooltip,
  plotOptions: ApexPlotOptions;
  colors: string[]
  stroke: ApexStroke;
  dataLabels: ApexDataLabels;
  markers: ApexMarkers;
  yaxis: ApexYAxis;
  grid: ApexGrid;
  legend: ApexLegend;
};


@Component({
  selector: 'app-readmodel',
  templateUrl: './readmodel.component.html',
  styleUrls: ['./readmodel.component.css']
})
export class ReadmodelComponent
 implements
          OnInit,
          AfterContentInit,
          // AfterContentChecked,
          AfterViewInit,
          AfterViewChecked
          // OnDestroy
          { // , OnChanges {

  @Input() modelName: string;
  @Input() information: string;
  currentUser: User;

  valueMaxCharLinearY;
  valueMinCharLinearY;
  rank;

  csvUrlPearsonCorrelation: string[];
  csvUrlKendallCorrelation: string[];
  csvUrlSpearmanCorrelation: string[];
  csvUrlChoseIDColumnsCorrelation: string[];

  csvUrlAnomalies: string[];
  csvUrlTestAnomalies: string[];
  csvUrlTrainAnomalies: string[];
  csvUrlRecostructedError: string[];
  csvUrlPrediction: string[];
  csvUrlJSONmodel: string[];


  modelJSON: string;

  csvDataPearson: Array<string[]> = [];
  csvDataKendall: Array<string[]> = [];
  csvDataSpearman: Array<string[]> = [];
  csvDataHeadMapID: Array<string[]> = [];

  file: File;
  numPerarson = 0;
  numKendall = 0;
  numSpearman = 0;
  numHeadmapID = 0;

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

  informationWorkflow: string;
  searchMSE: string;

  csvDataAnomalies: Array<string[]> = [];
  csvDataTestAnomalies: Array<string[]> = [];
  csvDataTrainAnomalies: Array<string[]> = [];
  csvDataRecostructedError: Array<string[]> = [];
  csvDataPredictions: Array<string[]> = [];

  public chartArrayAnomalies;
  public chartArrayTestAnomalies;
  public chartArrayTrainAnomalies;
  public chartArrayRecostructedError;
  public chartArrayPredictions;
  public xaxis; // : {categories: string[], title: {text: string}}[];


  public chartArrayTestAnomaliesHeaders;
  public chartArrayTrainAnomaliesHeaders;
  public chartArrayRecostructedErrorHeaders;
  public chartArrayPredictionsHeaders;

  public chartArrayTestAnomaliesBody;
  public chartArrayTrainAnomaliesBody;
  public chartArrayRecostructedErrorBody;
  public chartArrayPredictionsBody;

  public withoutQuotesAnomalyesTrainHeaders;
  public withoutQuotesAnomalyesTestHeaders;
  public withoutQuotesAnomalyesMSEHeaders;
  public withoutQuotesAnomalyesPredictHeaders;

  public chartArrayTestAnomaliesRefresh;

  blobbox = false;


  dtOptionsAnomalies: DataTables.Settings = { pagingType: 'full_numbers', pageLength: 10, dom: 'Rlfrtip' };

  // Settings Charts
  @ViewChild('chartPearsonCorr') chartPearsonCorr: ChartComponent;
  @ViewChild('chartKendallCorr') chartKendallCorr: ChartComponent;
  @ViewChild('chartSpearmanCorr') chartSpearmanCorr: ChartComponent;
  @ViewChild('chartHeadmapIDCorr') chartHeadmapIDCorr: ChartComponent;

  public chartOptionsPearsonCorr: Partial<ChartOptionsCorre>;
  public chartOptionsKendallCorr: Partial<ChartOptionsCorre>;
  public chartOptionsSpearmanCorr: Partial<ChartOptionsCorre>;
  public chartOptionsHeadmapIDCorr: Partial<ChartOptionsCorre>;

  @ViewChild('chartTesting') chartTesting: ChartComponent;
  public chartOptionsTesting: Partial<ChartOptionsAnomalies>;
  @ViewChild('chartTrain') chartTrain: ChartComponent;
  public chartOptionsTrain: Partial<ChartOptionsAnomalies>;

  @ViewChild('chartReconst') chartReconst: ChartComponent;
  public chartOptionsReconst: Partial<ChartOptionsAnomalies>;
  public isLoggedIn = false;
  public userProfile: KeycloakProfile | null = null;

  constructor(private http: HttpClient,
              private toastr: ToastrService,
              private userService: UserService,
              private route: ActivatedRoute,
              private translate: TranslateService,
              private transLang: TranslationService,
              private readonly keycloak: KeycloakService) {

    // this.currentUser = JSON.parse(localStorage.getItem('currentUser'));
    this.currentUser = { id: '', username: '', firstName: '', lastName: '' };



    // Load setting graphics
    this.chartOptionsTesting = {
      chart: {
        height: 550,
        type: 'boxPlot',
        animations: {
          enabled: false
        },
      },
      colors: ['#008FFB', '#FEB019'],
      title: {
        text: this.translate.instant('Test.text'),
        align: 'left'
      },
      xaxis: {
        type: 'numeric',
        tooltip: {
        }
      },
      tooltip: {
        shared: false,
        intersect: true
      }
    };

    this.chartOptionsTrain = {
      chart: {
        height: 550,
        type: 'boxPlot',
        animations: {
          enabled: false
        },
      },
      colors: ['#008FFB', '#FEB019'],
      title: {
        text: this.translate.instant('Train.text'),
        align: 'left'
      },
      xaxis: {
        type: 'numeric',
        tooltip: {
        }
      },
      tooltip: {
        shared: false,
        intersect: true
      }
    };

    this.initChartsLinear();

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
    this.tableLanguage();

    this.route.params.subscribe( params =>
        // tslint:disable-next-line:no-string-literal
        this.modelName = params['namemodels']
    );

        // Urls of all the files saven in worspace for views in web UI
    this.csvUrlPearsonCorrelation = [this.modelName, this.modelName + '_pearson_correlation.csv'];
    this.csvUrlKendallCorrelation = [this.modelName, this.modelName + '_kendall_correlation.csv'];
    this.csvUrlSpearmanCorrelation = [this.modelName, this.modelName + '_spearman_correlation.csv'];
    this.csvUrlChoseIDColumnsCorrelation = [this.modelName, 'perfectMatrixheatmap1.csv'];
    this.csvUrlAnomalies = [this.modelName, this.modelName + '_anomalies.csv'];
    this.csvUrlTestAnomalies = [this.modelName, this.modelName + '_test_anomalies.csv'];
    this.csvUrlTrainAnomalies = [this.modelName, this.modelName + '_train_anomalies.csv'];
    this.csvUrlRecostructedError = [this.modelName, this.modelName + '_reconstrucError.csv'];
    this.csvUrlPrediction = [this.modelName, this.modelName + '_predictions.csv'];
    this.csvUrlJSONmodel = [this.modelName, this.modelName + '.json'];
    console.log('ngInit_0_start');
    // Readin of back end all the files.
    this.getDataCorr('pearson', this.csvUrlPearsonCorrelation);
    this.getDataCorr('kendall', this.csvUrlKendallCorrelation);
    this.getDataCorr('sperman', this.csvUrlSpearmanCorrelation);
    this.getDataCorr('headmapID', this.csvUrlChoseIDColumnsCorrelation);
    this.getDataCorr('json', this.csvUrlJSONmodel);
    this.getDataCorr('recostructederror', this.csvUrlRecostructedError);

    this.perarsonCorr(); // start charts
    this.getDataAnomalies('anomalies', this.csvUrlAnomalies);
    this.getDataAnomalies('anomaliestest', this.csvUrlTestAnomalies);
    this.getDataAnomalies('anomaliestrain', this.csvUrlTrainAnomalies);
    // this.getDataAnomalies('recostructederror', this.csvUrlRecostructedError);
    this.getDataAnomalies('predictions', this.csvUrlPrediction);
    console.log('ngInit_0_end');

  }



  ngAfterContentInit(): void {

  }

  ngAfterViewInit(): void {

  }
  ngAfterViewChecked(): void {

  }

  // fuction reading other files, start afther per asycr
  viewAll(): void {
    console.log('View_Start');
    this.transformRowsDataAnomalies(this.csvDataTestAnomalies, 'test');
    this.transformRowsDataAnomalies(this.csvDataTrainAnomalies, 'train');
    this.processLineSeachActualMSE();
    this.chartArrayRecostructedError = this.transformRowsData(this.csvDataRecostructedError);
    this.blobbox = true;
    console.log('View_End');

  }
  // Load   graphics
  perarsonCorr(): void {


    this.chartOptionsPearsonCorr = {
      chart: {
        height: 550,
        width: 1000,
        type: 'heatmap',
        animations: {
          enabled: false,
          speed: 5
        },
      },
      stroke: {
        width: 0
      },
      dataLabels: {
        enabled: true,
      },
      colors: ['#008FFB'],
      title: {
        text: 'Correlation Matrix Pearson'
      }
    };

    this.chartOptionsKendallCorr = {
      chart: {
        height: 550,
        width: 1000,
        type: 'heatmap',
        animations: {
          enabled: false,
          speed: 5
        },
      },
      stroke: {
        width: 0
      },
      dataLabels: {
        enabled: true,
      },
      colors: ['#008FFB'],
      title: {
        text: 'Correlation Matrix Kendall'
      }
    };

    this.chartOptionsSpearmanCorr = {
      chart: {
        height: 550,
        width: 1000,
        type: 'heatmap',
        animations: {
          enabled: false,
          speed: 5
        },
      },
      stroke: {
        width: 0
      },

      dataLabels: {
        enabled: true,
      },
      colors: ['#008FFB'],
      title: {
        text: 'Correlation Matrix Spearman'
      }
    };

    this.chartOptionsHeadmapIDCorr = {
      chart: {
        height: 550,
        width: 1000,
        type: 'heatmap',
        animations: {
          enabled: false,
          speed: 5
        },
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
        text: 'Correlation Matrix Columns Selected ID'
      }
    };




  }
  // Load   graphics
  initChartsLinear(): void {

    this.chartOptionsReconst = {
      chart: {
        height: 550,
        type: 'line',
        dropShadow: {
          enabled: true,
          color: '#000',
          top: 18,
          left: 7,
          blur: 10,
          opacity: 0.2
        },
        toolbar: {
          show: false
        },
        animations: {
          enabled: false,
          speed: 5
        },
      },
      colors: ['#77B6EA', '#f50806'],
      dataLabels: {
        enabled: false
      },
      stroke: {
        curve: 'stepline'
      },
      title: {
        text: this.translate.instant('ReconstructionMSE.text'),
        align: 'left'
      },
      grid: {
        borderColor: '#e7e7e7',
        row: {
          colors: ['#f3f3f3', 'transparent'], // takes an array which will be repeated on columns
          opacity: 0.5
        }
      },
      markers: {
        size: 1
      },
      xaxis: {
        categories: this.rank,
        title: {
          text: this.translate.instant('Rank.text')
        }
      },
      yaxis: {
        title: {
          text: this.translate.instant('ReconstructionMSE.text')
        },
        min: this.valueMinCharLinearY,
        max: this.valueMaxCharLinearY
      },
      legend: {
        position: 'top',
        horizontalAlign: 'right',
        floating: true,
        offsetY: -25,
        offsetX: -5
      }
    };
  }
  // Construction or serialisation of the data for the graphs.
  transformRowsDataCorr(array: string[], arrayColumns: Array<string[]>): { x: string; y: number; }[] {


    const tmp: { x: string; y: number; } = null;
    const final: { x: string; y: number; }[] = [];

    for (let i = 1; i <= array.length - 1; i++) {
      final.push({
        x: arrayColumns[0][i],
        y: parseFloat(array[i])
      });
    }


    return final;
  }
  // return of the first row for table and chart headers
  transformRowsDataName(array: string[]): string {
    return array[0];
  }
  // Function to collect and sort the data from the files in the backend and prepare the data to be displayed in the html component.
  // Correlations
  getDataCorr(typeCorrelation: string, url: string[]): any {


    this.userService.downloadJSONFiles(this.currentUser, url[1], url[0]).subscribe(data => {
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
      if (typeCorrelation === 'json') {
        this.modelJSON = data;
      }
      if (typeCorrelation === 'recostructederror') {
        this.chartArrayRecostructedError = [];
      }



      list.forEach(e => {
        switch (typeCorrelation) {
          case 'pearson':
            this.csvDataPearson.push(e.split(',')); // tables
            if (this.numPerarson !== 0) {
              this.chartArrayPearson.push({
                name: this.transformRowsDataName(e.split(',')),
                data: this.transformRowsDataCorr(e.split(','), this.csvDataPearson)
              }); // charts correlation matrix
              // console.log(this.transformRowsDataCorr(e.split(','), this.csvDataPearson), 'Pearson');
            } else {
              this.numPerarson = 1;
              // console.log('header table out: ', e.split(','));
            }
            break;

          case 'kendall':
            this.csvDataKendall.push(e.split(','));
            if (this.numKendall !== 0) {
              this.chartArrayKendall.push({
                name: this.transformRowsDataName(e.split(',')),
                data: this.transformRowsDataCorr(e.split(','), this.csvDataKendall)
              });
              // console.log(this.transformRowsDataCorr(e.split(','), this.csvDataKendall), 'kendall');
            } else {
              this.numKendall = 1;
              // console.log('header table out: ', e.split(','));
            }
            break;

          case 'sperman':
            this.csvDataSpearman.push(e.split(','));
            if (this.numSpearman !== 0) {
              this.chartArraySpearman.push({
                name: this.transformRowsDataName(e.split(',')),
                data: this.transformRowsDataCorr(e.split(','), this.csvDataSpearman)
              });
              // console.log(this.transformRowsDataCorr(e.split(','), this.csvDataSpearman), 'sperman');
            } else {
              this.numSpearman = 1;
              console.log('header table out: ', e.split(','));
            }
            break;

          case 'headmapID':
            this.csvDataHeadMapID.push(e.split(','));
            if (this.numHeadmapID !== 0) {
              this.chartArrayHeadMapID.push({
                name: this.transformRowsDataName(e.split(',')),
                data: this.transformRowsDataCorr(e.split(','), this.csvDataHeadMapID)
              });
              // console.log(this.transformRowsDataCorr(e.split(','), this.csvDataHeadMapID), 'headmapID');
            } else {
              this.numHeadmapID = 1;
              // console.log('headmapID table out: ', e.split(','));
            }
            break;
          case 'recostructederror':
            this.csvDataRecostructedError.push(e.split(','));
            break;


          default:
            break;
        }
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
      if (typeCorrelation === 'recostructederror') {
        this.chartArrayRecostructedErrorHeaders = [];
        this.chartArrayRecostructedErrorBody = [];

        this.chartArrayRecostructedErrorBody = this.csvDataRecostructedError.slice();
        this.chartArrayRecostructedErrorHeaders = this.chartArrayRecostructedErrorBody.shift();
        this.chartArrayRecostructedErrorBody.pop();

        this.withoutQuotesAnomalyesMSEHeaders = [];
        for (let i = 0; i <= this.chartArrayRecostructedErrorHeaders.length - 1; i++) {
          this.withoutQuotesAnomalyesMSEHeaders.push(this.chartArrayRecostructedErrorHeaders[i].replace(/['"]+/g, ''));
        }
      }

    });

    // restar workflow
    this.numPerarson = 0;
    this.numKendall = 0;
    this.numSpearman = 0;
    this.numHeadmapID = 0;



  }

  // Change between tabs anomalies, tables
  onTabChangedAnomalies($event): void {
    const clickedIndex = $event.index;

    console.log(clickedIndex);
    switch (clickedIndex) {
      case 1:
        this.chartArrayTestAnomaliesRefresh = [];
        this.chartArrayTestAnomaliesRefresh = this.chartArrayTestAnomalies.slice();

        break;

      default:
        break;
    }

  }
  // Change between tabs Correlations, tables
  onTabChangedCorr($event): void {
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


  // Function to collect and sort the data from the files in the backend and prepare the data to be displayed in the html component.
  // Anomalies
  getDataAnomalies(typeCorrelation: string, url: string[]): any {

    this.userService.downloadJSONFiles(this.currentUser, url[1], url[0]).subscribe(data => {

      const list = data.split('\n');
      if (typeCorrelation === 'anomalies') {
        this.chartArrayAnomalies = [];
      }
      if (typeCorrelation === 'anomaliestest') {
        this.chartArrayTestAnomalies = [];
      }
      if (typeCorrelation === 'anomaliestrain') {
        this.chartArrayTrainAnomalies = [];

      }
      // if (typeCorrelation === 'recostructederror') {
      //   this.chartArrayRecostructedError = [];
      // }
      if (typeCorrelation === 'predictions') {
        this.chartArrayPredictions = [];
      }


      list.forEach(e => {
        switch (typeCorrelation) {
          case 'anomalies':
            this.csvDataAnomalies.push(e.split(',')); // tables
            break;

          case 'anomaliestest':
            this.csvDataTestAnomalies.push(e.split(','));
            break;

          case 'anomaliestrain':
            this.csvDataTrainAnomalies.push(e.split(','));
            break;

          // case 'recostructederror':
          //   this.csvDataRecostructedError.push(e.split(','));
          //   break;

          case 'predictions':
            this.csvDataPredictions.push(e.split(','));
            break;

          default:
            break;
        }
      });

      if (typeCorrelation === 'anomaliestest') {
        this.chartArrayTestAnomaliesHeaders = [];
        this.chartArrayTestAnomaliesBody = [];

        this.chartArrayTestAnomaliesBody = this.csvDataTestAnomalies.slice();
        this.chartArrayTestAnomaliesHeaders = this.chartArrayTestAnomaliesBody.shift();
        this.chartArrayTestAnomaliesBody.pop();

        this.withoutQuotesAnomalyesTestHeaders = [];
        for (let i = 0; i <= this.chartArrayTestAnomaliesHeaders.length - 1; i++) {
          this.withoutQuotesAnomalyesTestHeaders.push(this.chartArrayTestAnomaliesHeaders[i].replace(/['"]+/g, ''));
        }
      }
      if (typeCorrelation === 'anomaliestrain') {
        this.chartArrayTrainAnomaliesHeaders = [];
        this.chartArrayTrainAnomaliesBody = [];

        this.chartArrayTrainAnomaliesBody = this.csvDataTrainAnomalies.slice();
        this.chartArrayTrainAnomaliesHeaders = this.chartArrayTrainAnomaliesBody.shift();
        this.chartArrayTrainAnomaliesBody.pop();

        this.withoutQuotesAnomalyesTrainHeaders = [];
        for (let i = 0; i <= this.chartArrayTrainAnomaliesHeaders.length - 1; i++) {
          this.withoutQuotesAnomalyesTrainHeaders.push(this.chartArrayTrainAnomaliesHeaders[i].replace(/['"]+/g, ''));
        }

      }
      // if (typeCorrelation === 'recostructederror') {
      //   this.chartArrayRecostructedErrorHeaders = [];
      //   this.chartArrayRecostructedErrorBody = [];

      //   this.chartArrayRecostructedErrorBody = this.csvDataRecostructedError.slice();
      //   this.chartArrayRecostructedErrorHeaders = this.chartArrayRecostructedErrorBody.shift();
      //   this.chartArrayRecostructedErrorBody.pop();

      //   this.withoutQuotesAnomalyesMSEHeaders = [];
      //   for (let i = 0; i <= this.chartArrayRecostructedErrorHeaders.length - 1; i++) {
      //     this.withoutQuotesAnomalyesMSEHeaders.push(this.chartArrayRecostructedErrorHeaders[i].replace(/['"]+/g, ''));
      //   }
      // }

      if (typeCorrelation === 'predictions') {
        this.chartArrayPredictionsHeaders = [];
        this.chartArrayPredictionsBody = [];

        this.chartArrayPredictionsBody = this.csvDataPredictions.slice();
        this.chartArrayPredictionsHeaders = this.chartArrayPredictionsBody.shift();
        this.chartArrayPredictionsBody.pop();

        this.withoutQuotesAnomalyesPredictHeaders = [];
        for (let i = 0; i <= this.chartArrayPredictionsHeaders.length - 1; i++) {
          this.withoutQuotesAnomalyesPredictHeaders.push(this.chartArrayPredictionsHeaders[i].replace(/['"]+/g, ''));
        }

        this.viewAll();

      }


    });

  }

  // Serialises data so that they can be consumed by tables and graphs.
  transformRowsDataAnomalies(array: Array<string[]>, s: string): void {


    if (s === 'test') {
      this.chartArrayTestAnomalies = [];
    }
    if (s === 'train') {
      this.chartArrayTrainAnomalies = [];
    }

    const final: { x: string; y: number[]; }[] = [];
    let tmp = [];

    for (let i = 0; i < array[i].length; i++) {
      tmp = [];
      for (let j = 0; j < array.length - 2; j++) {
        tmp.push(array[j][i]);

      }
      const tmpY: number[] = [];
      for (let h = 1; h <= tmp.length - 2; h++) {
        tmpY.push(parseFloat(tmp[h]));
      }
      final.push({
        x: tmp[0],
        y: tmpY
      });
    }

    if (s === 'test') {
      this.chartArrayTestAnomalies.push({
        type: 'boxPlot',
        data: final
      });
      // console.log(this.chartArrayTestAnomalies, 'Sale');
    }
    if (s === 'train') {
      this.chartArrayTrainAnomalies.push({
        type: 'boxPlot',
        data: final
      });
      // console.log(this.chartArrayTrainAnomalies, 'Sale');
    }




  }
  // Serialises data so that they can be consumed by tables and graphs
  transformRowsData(array: Array<string[]>): { name: string; data: number[]; }[] {


    const tmp: number[] = [];
    const mse: number[] = [];
    this.rank = [];
    const final: { name: string; data: number[]; }[] = [];

    this.xaxis = [];


    for (let j = 1; j < array.length - 2; j++) {
      tmp.push(parseFloat(array[j][0]));
      mse.push(parseFloat(this.searchMSE)); // .split(':')[1]));
    }

    this.valueMaxCharLinearY = Math.max(...tmp);
    this.valueMinCharLinearY = Math.min(...tmp);

    for (let i = 1; i < array.length - 2; i++) {
      this.rank.push(array[i][1]);
    }


    // this.xaxis.push({categories: this.rank, title: { text: 'Rank'}});

    final.push({
      name: this.translate.instant('ReconstructionMSE.text'),
      data: tmp
    });
    final.push({
      name: 'MSE',
      data: mse
    });

    // console.log(this.xaxis, 'XAXIS');
    // console.log(final, 'Recostruction Error');

    return final;
  }
  // Search the MSE in JSON file
  processLineSeachActualMSE(): void {

    JSON.parse(this.modelJSON, (k, v) => {
      if (k === 'MSE') {
        console.log(k, v);
        this.searchMSE = v;
      }
    });

  }


  // Information OK
  showToaster(message: string): any {
    this.toastr.success(message);
  }
  // Information no OK
  showToasterError(message: string): any {
    this.toastr.error(message);
  }
   tableLanguage(): void{
      if (this.transLang.activeLang === 'es'){
      this.dtOptionsAnomalies = { pagingType: 'full_numbers', pageLength: 10, language: LanguageApp.spanishdatatables };
    }else{
      this.dtOptionsAnomalies = { pagingType: 'full_numbers', pageLength: 10};
    }
  }

  deleteModels(): void{


    this.userService.deleteModels(this.currentUser, this.modelName)
      .subscribe(
        file => {
          console.log(file);
          this.showToaster(this.modelName + '  ' + this.translate.instant('ModelDeleted.text'));

        },
        error => {
          this.showToasterError(this.translate.instant('Error.text') + this.modelName + this.translate.instant('ModelDeletedError.text'));
        });



  }
}
