import { Component, OnInit, Input, ViewChild } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserService } from '../../../services/index_services';
import {BackendscriptsService} from '../../../services/backendscripts.service';
import { User } from '../../../models/index_models';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { LanguageApp } from '../../../services/LanguageApp';
import { TranslationService } from '../../../services/translation.service';
import { TimeLoadingService } from '../../../services/timeloading.service';
import { KeycloakService } from 'keycloak-angular';
import { KeycloakProfile } from 'keycloak-js';



import {
  ChartComponent,
  ApexChart,
  ApexPlotOptions,
  ApexTitleSubtitle,
  ApexAxisChartSeries,
  ApexXAxis,
  ApexTooltip,
  ApexDataLabels,
  ApexStroke,
  ApexMarkers,
  ApexYAxis,
  ApexGrid,
  ApexLegend
} from 'ng-apexcharts';

export type ChartOptions = {
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
  selector: 'app-neuronalnetworkcolums-anomalies',
  templateUrl: './neuronalnetworkcolums-anomalies.component.html',
  styleUrls: ['./neuronalnetworkcolums-anomalies.component.css']
})
export class NeuronalnetworkcolumsAnomaliesComponent implements OnInit {
  @ViewChild('chartTesting') chartTesting: ChartComponent;
  public chartOptionsTesting: Partial<ChartOptions>;
  @ViewChild('chartTrain') chartTrain: ChartComponent;
  public chartOptionsTrain: Partial<ChartOptions>;

  @ViewChild('chartReconst') chartReconst: ChartComponent;
  public chartOptionsReconst: Partial<ChartOptions>;

  // data gone previus component (neuronalnetworkcolumnas-corre)
  @Input() modelName: string;
  @Input() uploadedFilesTrain: Array<File>;
  @Input() uploadedFilesTest: Array<File>;
  @Input() selIds: string;
  @Input() anomalyButton: boolean;
  @Input() dateModelCreateModels: string;

  host: any;

  informationWorkflow: string;

  columsID: string;

  nextNeu = false;
  gifData = false;
  gifCorr = false;
  corrView = false;
  blobbox = false;
  predictionButton = false;

  csvUrlAnomalies: string[];
  csvUrlTestAnomalies: string[];
  csvUrlTrainAnomalies: string[];
  csvUrlRecostructedError: string[];
  csvUrlPrediction: string[];


  csvDataAnomalies: Array<string[]> = [];
  csvDataTestAnomalies: Array<string[]> = [];
  csvDataTrainAnomalies: Array<string[]> = [];
  csvDataRecostructedError: Array<string[]> = [];
  csvDataPredictions: Array<string[]> = [];
  searchMSE: string;


  public chartArrayAnomalies;
  public chartArrayTestAnomalies;
  public chartArrayTrainAnomalies;
  public chartArrayRecostructedError;
  public chartArrayPredictions;
  public xaxis; // : {categories: string[], title: {text: string}}[];
  rank;
  valueMaxCharLinearY;
  valueMinCharLinearY;

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

  public test: Array<number[]>;


  numAnomalies = 0;
  numTestAnomalies = 0;
  numTrainAnomalies = 0;
  numReconstruted = 0;

  dtOptionsAnomalies: DataTables.Settings = { pagingType: 'full_numbers', pageLength: 10, dom: 'Rlfrtip' };
  currentUser: User;

  public isLoggedIn = false;
  public userProfile: KeycloakProfile | null = null;

  // loadNumberProgress: {progress: number, state: string};
  // interval;



  constructor(private http: HttpClient,
              private userService: UserService,
              private backEndPython: BackendscriptsService,
              private toastr: ToastrService,
              private translate: TranslateService,
              private transLang: TranslationService,
              public loading: TimeLoadingService,
              private readonly keycloak: KeycloakService) {

    // this.currentUser = JSON.parse(localStorage.getItem('currentUser'));
    this.currentUser = { id: '', username: '', firstName: '', lastName: '' };
    this.host = self.location.host.split(':')[0];



    this.chartOptionsTesting = {
      chart: {
        height: 550,
        type: 'boxPlot',
        animations: {enabled: false}
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
        animations: {enabled: false}
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

    // Urls of all the files saven in worspace for views in web UI

    this.csvUrlAnomalies = [this.modelName + '.' + this.dateModelCreateModels,
    this.modelName + '.' + this.dateModelCreateModels + '_anomalies.csv'];
    this.csvUrlTestAnomalies = [this.modelName + '.' + this.dateModelCreateModels,
    this.modelName + '.' + this.dateModelCreateModels + '_test_anomalies.csv'];
    this.csvUrlTrainAnomalies = [this.modelName + '.' + this.dateModelCreateModels,
    this.modelName + '.' + this.dateModelCreateModels + '_train_anomalies.csv'];
    this.csvUrlRecostructedError = [this.modelName + '.' + this.dateModelCreateModels,
    this.modelName + '.' + this.dateModelCreateModels + '_reconstrucError.csv'];
    this.csvUrlPrediction = [this.modelName + '.' + this.dateModelCreateModels,
    this.modelName + '.' + this.dateModelCreateModels + '_predictions.csv'];


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
        animations: {enabled: false}
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
          text:  this.translate.instant('ReconstructionMSE.text')
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
  // This function calls the python back-end server, to generate the models.
  runningScriptsMLAnomaly(): void {
    this.backEndPython.loginAutorization(this.currentUser, this.isLoggedIn.toString());
    setTimeout(() => {

      this.tableLanguage();
      this.loading.timeLoading();
      this.gifCorr = true;
      try {
        // tslint:disable-next-line: max-line-length
        this.http.get(`/scripts/scriptsmlanomaly/${this.modelName}/${this.uploadedFilesTrain[0].name}/${this.uploadedFilesTest[0].name}/${this.selIds}/${this.currentUser.username}`, { responseType: 'text' }).subscribe(data => {
          this.informationWorkflow = data as string;
          this.getData('anomalies', this.csvUrlAnomalies);
          this.corrView = true;
          this.gifCorr = false;
          this.predictionButton = true;
          this.anomalyButton = false;
          this.showToaster(this.translate.instant('Model.text') +
            this.modelName + this.translate.instant('Model2.text') +
            this.currentUser.username);


        });

      } catch (error) {
        this.gifCorr = false;
        this.nextNeu = true;
      }
    }, 1500);


  }
  // fuction reading other files, start afther per asycr
  viewAll(): void {
    this.transformRowsDataAnomalies(this.csvDataTrainAnomalies, 'train');
    this.processLineSeachActualMSE();
    this.chartArrayRecostructedError = this.transformRowsData(this.csvDataRecostructedError);
    this.initChartsLinear();
    this.transformRowsDataAnomalies(this.csvDataTestAnomalies, 'test');
    this.blobbox = true;
    this.predictionButton = false;
  }
  // Save column id.
  saveColumnsIDstring(id): string {


    for (const j of id) {
      this.columsID += ',' + j.id;
      console.log(this.columsID);
    }
    const temp = this.columsID.substring(2);
    console.log(temp);
    return temp;

  }


  // Function to collect and sort the data from the files in the backend and prepare the data to be displayed in the html component.
  getData(typeCorrelation: string, url: string[]): any {


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
      if (typeCorrelation === 'recostructederror') {
        this.chartArrayRecostructedError = [];
      }
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

          case 'recostructederror':
            this.csvDataRecostructedError.push(e.split(','));
            break;

          case 'predictions':
            this.csvDataPredictions.push(e.split(','));
            break;

          default:
            break;
        }
      });

      if (typeCorrelation === 'anomalies') {
        this.getData('anomaliestrain', this.csvUrlTrainAnomalies);

      }

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
        this.getData('recostructederror', this.csvUrlRecostructedError);

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
        this.getData('anomaliestest', this.csvUrlTestAnomalies);

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
        this.getData('predictions', this.csvUrlPrediction);

      }

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

    console.log(array.length, 'rows ' + s);
    console.log(array[1].length, 'columns ' + s);


    for (let i = 0; i < array[1].length; i++) {
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
    }
    if (s === 'train') {
      this.chartArrayTrainAnomalies.push({
        type: 'boxPlot',
        data: final
      });
    }




  }


  // return of the first row for table and chart headers
  transformRowsDataName(array: string[]): string {
    return array[0];
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
      mse.push(parseFloat(this.searchMSE.split(':')[1]));
    }

    this.valueMaxCharLinearY = Math.max(...tmp);
    this.valueMinCharLinearY = Math.min(...tmp);

    for (let i = 1; i < array.length - 2; i++) {
      this.rank.push(array[i][1]);
    }



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



  getInfo(url: string): Observable<string> {

    return this.http.get(url, { responseType: 'text', params: { user: this.currentUser.username } });

  }

  // Search the MSE in wokflow exit python server
  processLineSeachActualMSE(): void {

    const arrlines = this.informationWorkflow.split(/\r?\n/);
    for (let i = 0, strLen = arrlines.length; i < strLen; i++) {
      if (arrlines[i].includes('MSE:')) {
        console.log(`Line from file: ${arrlines[i]}`);
        this.searchMSE = arrlines[i];
        break;
      }
    }
  }
  // Change between tabs, tables
  onTabChanged($event): void {
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
  // Information OK
  showToaster(message: string): any {
    this.toastr.success(message);
  }
  // Information no OK
  showToasterError(message: string): any {
    this.toastr.error(message);
  }
  saveFinish(): void{
    this.showToaster(this.translate.instant('SaveModel.text'));
  }

  tableLanguage(): void{
      if (this.transLang.activeLang === 'es'){
      this.dtOptionsAnomalies = { pagingType: 'full_numbers', pageLength: 10, dom: 'Rlfrtip', language: LanguageApp.spanishdatatables };
    }else{
      this.dtOptionsAnomalies = {pagingType: 'full_numbers', pageLength: 10, dom: 'Rlfrtip' };
    }
  }



}
