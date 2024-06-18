import { Component, OnInit, Input, ViewChild } from '@angular/core';
// import { UploadFileComponent } from '../../uploadfile/uploadfile.component';
import { HttpClient } from '@angular/common/http';
// import { Observable } from 'rxjs';
import { ToastrService } from 'ngx-toastr';
import { UserService } from '../../../services/index_services';
import {BackendscriptsService} from '../../../services/backendscripts.service';
import { User } from '../../../models/index_models';
import { TranslateService } from '@ngx-translate/core';
import { LanguageApp } from '../../../services/LanguageApp';
import { TranslationService } from '../../../services/translation.service';
import { TimeLoadingService } from '../../../services/timeloading.service';
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

export type ChartOptionsBoxplot = {
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
  selector: 'app-neuronalnetworkingcolumnsid-anomalies',
  templateUrl: './neuronalnetworkingcolumnsid-anomalies.component.html',
  styleUrls: ['./neuronalnetworkingcolumnsid-anomalies.component.css']
})
export class NeuronalnetworkingcolumnsidAnomaliesComponent implements OnInit {

  @Input() modelName: string;
  @Input() uploadedFilesTrain: Array<File>;
  @Input() uploadedFilesTest: Array<File>;
  @Input() selIds: string;
  @Input() selectedIdsRows: string;
  @Input() buttonCorreWorking: boolean;
  @Input() dateModelCreateModels: string;

  // @ViewChild('chartTrain') chartTrain: ChartComponent;
  @ViewChild('chartHeadmapIDCorr') chartHeadmapIDCorr: ChartComponent;

  // public chartOptionsTrain: Partial<ChartOptions>;
  public chartOptionsHeadmapIDCorr: Partial<ChartOptions>;

  @ViewChild('chartTesting') chartTesting: ChartComponent;
  public chartOptionsTesting: Partial<ChartOptionsBoxplot>;
  @ViewChild('chartTrain') chartTrain: ChartComponent;
  public chartOptionsTrain: Partial<ChartOptionsBoxplot>;

  public chartArrayTestAnomaliesGraphicsRefresh;

  public chartArrayTestAnomaliesGraphics;
  public chartArrayTrainAnomaliesGraphics;

  dtOptions: DataTables.Settings = {pagingType: 'full_numbers', pageLength: 10};




  blobbox = false;

  gifCorr = false;
  corrView = false;
  sendColumnsID = '';
  columnsId = ' ';
  informationWorkflow: string;

  currentUser: User;


  csvDataAnomaliesHeader;
  csvDataAnomaliesBody;
  csvDataAnomalies: Array<string[]> = [];

  csvUrlTrainingCol: string[];
  csvUrlTestingCol: string[];
  csvUrlTrainAnomalies: string[];
  csvUrlCoorelation: string[];
  csvUrlCoorelation2: string[];
  perfectMatrixheatmap1: string[];


  public chartArrayTrain;
  public chartArrayTest;
  public chartArrayCorr;
  public chartArrayCorr2;
  public charArrayperfectMatrixheatmap1;


  csvDataTrainHeader;
  csvDataTrainBody;

  csvDataTestHeader;
  csvDataTestBody;

  csvDataCoorHeader;
  csvDataCoorBody;

  csvDataCoorHeader2;
  csvDataCoorBody2;

  numTrain = 0;
  numTest = 0;
  numCoor = 0;
  numCoor2 = 0;
  numHeadmapID = 0;


  csvDataTrain: Array<string[]> = [];
  csvDataTest: Array<string[]> = [];
  csvDataCoor: Array<string[]> = [];
  csvDataCoor2: Array<string[]> = [];
  csvDataHeadMapID: Array<string[]> = [];

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
            private readonly keycloak: KeycloakService) {

    // this.currentUser = JSON.parse(localStorage.getItem('currentUser'));
    this.currentUser = { id: '', username: '', firstName: '', lastName: '' };
    this.host = self.location.host.split(':')[0];


    this.chartOptionsTesting = {
    chart: {
      height: 550,
      type: 'boxPlot',
        animations: {enabled: false},
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
        animations: {enabled: false},
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




    this.csvUrlTrainingCol = [this.modelName + '.' + this.dateModelCreateModels, this.modelName + '.' + this.dateModelCreateModels + '_training_data_Val_Col.csv'];
    this.csvUrlTestingCol = [this.modelName + '.' + this.dateModelCreateModels, this.modelName + '.' + this.dateModelCreateModels + '_testing_data_Val_Col.csv'];
    this.csvUrlCoorelation2 = [this.modelName + '.' + this.dateModelCreateModels, this.modelName + '.' + this.dateModelCreateModels + '2.csv'];
    this.perfectMatrixheatmap1 = [this.modelName + '.' + this.dateModelCreateModels, 'perfectMatrixheatmap1.csv'];



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

  runningScriptsMLCorrAnomaliesID(): void {
    this.backEndPython.loginAutorization(this.currentUser, this.isLoggedIn.toString());
    setTimeout(() => {

      this.loading.timeLoading();
      this.tableLanguage();
      this.gifCorr = true;
      this.corrView = true;
      this.sendColumnsID = this.saveColumnsIDstring(this.selectedIdsRows);
      try {
        // tslint:disable-next-line: max-line-length
        this.http.get(`/scripts/scriptsmlanomalyid/${this.modelName}/${this.uploadedFilesTrain[0].name}/${this.uploadedFilesTest[0].name}/${this.sendColumnsID}/${this.selIds}/${this.currentUser.username}`, { responseType: 'text' }).subscribe(data => {
          this.informationWorkflow = data as string;
          this.getData('train', this.csvUrlTrainingCol);
          this.getData('test', this.csvUrlTestingCol);
          // this.getData('coor', this.csvUrlCoorelation);
          this.getData('coor2', this.csvUrlCoorelation2);
          this.getData('perfectmatrix', this.perfectMatrixheatmap1);

          this.gifCorr = false;
          this.viewGraphics();


        });

      } catch (error) {
        this.gifCorr = false;
        this.corrView = true;
      }
    }, 1500);


  }

  getData(typeCorrelation: string, url: string[]): any {

    // for (const url of this.urls){

      // this.getInfo(url).subscribe(data => {

      this.userService.downloadJSONFiles(this.currentUser, url[1], url[0] ).subscribe(data => {
        const list = data.split('\n');
        if (typeCorrelation === 'train') {
          this.chartArrayTrain = [];
          this.csvDataTrainHeader = [];
          this.csvDataTrainBody = [];
        }
        if (typeCorrelation === 'test') {
          this.chartArrayTest = [];
          this.csvDataTestHeader = [];
          this.csvDataTestBody = [];
        }
        // if (typeCorrelation === 'coor') {
        //   this.chartArrayCorr = [];
        //   this.csvDataCoorHeader = [];
        //   this.csvDataCoorBody = [];
        // }
        if (typeCorrelation === 'coor2') {
          this.chartArrayCorr2 = [];
          this.csvDataCoorHeader2 = [];
          this.csvDataCoorBody2 = [];
        }
        if (typeCorrelation === 'perfectmatrix') {
          this.charArrayperfectMatrixheatmap1 = [];
        }

        list.forEach(e => {
          // for (let i = 1; i < list.length ; i++) {}
            switch (typeCorrelation) {
              case 'train':
                this.csvDataTrain.push(e.split(',')); // tables
                if (this.numTrain !== 0) {
                  this.chartArrayTrain.push({name: this.transformRowsDataName(e.split(',')),
                                        data: this.transformRowsData(e.split(','), this.csvDataTrain) }); // charts correlation matrix
                  console.log(this.transformRowsData(e.split(','), this.csvDataTrain), 'train');
                } else {
                  this.numTrain = 1;
                  console.log('header table out: ', e.split(','));
                }
                break;

              case 'test':
                this.csvDataTest.push(e.split(','));
                if (this.numTest !== 0) {
                this.chartArrayTest.push({name: this.transformRowsDataName(e.split(',')),
                                              data: this.transformRowsData(e.split(','), this.csvDataTest) });
                console.log(this.transformRowsData(e.split(','), this.csvDataTest), 'test');
                } else {
                  this.numTest = 1;
                  console.log('header table out: ', e.split(','));
                }
                break;


              case 'coor2':
                this.csvDataCoor2.push(e.split(','));
                if (this.numCoor2 !== 0) {
                this.chartArrayCorr2.push({name: this.transformRowsDataName(e.split(',')),
                                              data: this.transformRowsData(e.split(','), this.csvDataCoor2) });
                console.log(this.transformRowsData(e.split(','), this.csvDataCoor2), 'coor2');
                 } else {
                  this.numCoor2 = 1;
                  console.log('header table out: ', e.split(','));
                }
                break;

                case 'perfectmatrix':
                this.csvDataHeadMapID.push(e.split(','));
                if (this.numHeadmapID !== 0) {
                this.charArrayperfectMatrixheatmap1.push({name: this.transformRowsDataName(e.split(',')),
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
        });

          // seeialize data for tables
        if (typeCorrelation === 'train') {
          this.csvDataTrainBody = this.csvDataTrain.slice();
          this.csvDataTrainHeader = this.csvDataTrainBody.shift();
          this.csvDataTrainBody.pop();
          this.csvDataTrainBody.pop();
        }
        if (typeCorrelation === 'test') {
          this.csvDataTestBody = this.csvDataTest.slice();
          this.csvDataTestHeader = this.csvDataTestBody.shift();
          this.csvDataTestBody.pop();
          this.csvDataTestBody.pop();
        }

        if (typeCorrelation === 'coor2') {
          this.csvDataCoorBody2 = this.csvDataCoor2.slice();
          this.csvDataCoorHeader2 = this.csvDataCoorBody2.shift();
          this.csvDataCoorBody2.pop();
          this.viewAll();
        }

      });

      this.numTrain = 0;
      this.numTest = 0;
      this.numCoor = 0;
      this.numCoor2 = 0;
      this.numHeadmapID = 0;


  }

   transformRowsDataName(array: string[]): string{
      return array[0];
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

  viewAll(): void {
    this.transformRowsDataAnomalies(this.csvDataTrain, 'train');
    this.transformRowsDataAnomalies(this.csvDataTest, 'test');
    this.blobbox = true;

  }

   onTabChanged($event): void {
    const clickedIndex = $event.index;

    console.log(clickedIndex);
    switch (clickedIndex) {
      case 1:
        this.chartArrayTestAnomaliesGraphicsRefresh = [];
        this.chartArrayTestAnomaliesGraphicsRefresh = this.chartArrayTestAnomaliesGraphics.slice();

        break;

      default:
        break;
    }

  }


  transformRowsDataAnomalies(array: Array<string[]>, s: string): void {


    if (s === 'test') {
      this.chartArrayTestAnomaliesGraphics = [];
    }
    if (s === 'train') {
      this.chartArrayTrainAnomaliesGraphics = [];
    }

    const final: { x: string; y: number[]; }[] = [];
    const tmp = [];

    // tslint:disable-next-line:prefer-for-of
    for (let i = 0; i < array.length; i++) {
       // tslint:disable-next-line:prefer-for-of
       for (let j = 0; j < array[i].length; j++) {
        tmp.push(array[i][j]);
       }
    }
    const tmpY: number[] = [];
    for (let h = 1; h <= tmp.length - 2; h++) {
      tmpY.push(parseFloat(tmp[h]));
    }
    final.push({
      x: tmp[0],
      y: tmpY
    });


    if (s === 'test') {
      this.chartArrayTestAnomaliesGraphics.push({
        type: 'boxPlot',
        data: final
      });
    }
    if (s === 'train') {
      this.chartArrayTrainAnomaliesGraphics.push({
        type: 'boxPlot',
        data: final
      });
    }




  }

  viewGraphics(): void{


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
        type: this.translate.instant('Category.text')
      },
      title: {
        text: this.translate.instant('CorrelationMatrixSelectedID.text')
      }
    };




  }

   tableLanguage(): void{
      if (this.transLang.activeLang === 'es'){

      this.dtOptions = { pagingType: 'full_numbers', pageLength: 10, language: LanguageApp.spanishdatatables };
    }else{
      this.dtOptions = { pagingType: 'full_numbers', pageLength: 10};
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


}
