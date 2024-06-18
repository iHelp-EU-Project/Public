import { ActivatedRoute } from '@angular/router';
import { Component, OnInit, Input, ViewChild } from '@angular/core';
// import { UploadFileComponent } from '../../uploadfile/uploadfile.component';
import { HttpClient } from '@angular/common/http';
// import { Observable } from 'rxjs';
import { ToastrService } from 'ngx-toastr';
import { UserService } from '../services/index_services';
import { User } from '../models/index_models';
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
  selector: 'app-readmodelid',
  templateUrl: './readmodelid.component.html',
  styleUrls: ['./readmodelid.component.css']
})

export class ReadmodelidComponent implements OnInit {

  @Input() modelName: string;
  @Input() information: string;

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

  dtOptions: DataTables.Settings = {pagingType: 'full_numbers', pageLength: 10};

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

    this.chartOptionsTesting = {
    chart: {
      height: 550,
      type: 'boxPlot',
        animations: {
          enabled: false,
          speed: 5
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
          enabled: false,
          speed: 5
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

    this.csvUrlTrainingCol = [this.modelName , this.modelName  + '_training_data_Val_Col.csv'];
    this.csvUrlTestingCol = [this.modelName , this.modelName  + '_testing_data_Val_Col.csv'];
    this.csvUrlCoorelation2 = [this.modelName , this.modelName + '2.csv'];
    this.perfectMatrixheatmap1 = [this.modelName, 'perfectMatrixheatmap1.csv'];

    this.getData('train', this.csvUrlTrainingCol);


    this.gifCorr = false;
    this.viewGraphics();


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


  getData(typeCorrelation: string, url: string[]): any {


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

        if (typeCorrelation === 'coor2') {
          this.chartArrayCorr2 = [];
          this.csvDataCoorHeader2 = [];
          this.csvDataCoorBody2 = [];
        }
        if (typeCorrelation === 'perfectmatrix') {
          this.charArrayperfectMatrixheatmap1 = [];
        }

        list.forEach(e => {
            switch (typeCorrelation) {
              case 'train':
                this.csvDataTrain.push(e.split(',')); // tables
                if (this.numTrain !== 0) {
                  this.chartArrayTrain.push({name: this.transformRowsDataName(e.split(',')),
                                        data: this.transformRowsData(e.split(','), this.csvDataTrain) }); // charts correlation matrix
                } else {
                  this.numTrain = 1;
                }
                break;

              case 'test':
                this.csvDataTest.push(e.split(','));
                if (this.numTest !== 0) {
                this.chartArrayTest.push({name: this.transformRowsDataName(e.split(',')),
                                              data: this.transformRowsData(e.split(','), this.csvDataTest) });
                } else {
                  this.numTest = 1;
                }
                break;



              case 'coor2':
                this.csvDataCoor2.push(e.split(','));
                if (this.numCoor2 !== 0) {
                this.chartArrayCorr2.push({name: this.transformRowsDataName(e.split(',')),
                                              data: this.transformRowsData(e.split(','), this.csvDataCoor2) });
                 } else {
                  this.numCoor2 = 1;
                }
                break;

                case 'perfectmatrix':
                this.csvDataHeadMapID.push(e.split(','));
                if (this.numHeadmapID !== 0) {
                this.charArrayperfectMatrixheatmap1.push({name: this.transformRowsDataName(e.split(',')),
                                              data: this.transformRowsData(e.split(','), this.csvDataHeadMapID ) });
                 } else {
                  this.numHeadmapID = 1;
                }
                break;

              default:
                break;
            }
          // console.log(e);
        });

          // seeialize data for tables
        if (typeCorrelation === 'train') {
          this.csvDataTrainBody = this.csvDataTrain.slice();
          this.csvDataTrainHeader = this.csvDataTrainBody.shift();
          this.csvDataTrainBody.pop();
          this.csvDataTrainBody.pop();
          this.getData('test', this.csvUrlTestingCol);

        }
        if (typeCorrelation === 'test') {
          this.csvDataTestBody = this.csvDataTest.slice();
          this.csvDataTestHeader = this.csvDataTestBody.shift();
          this.csvDataTestBody.pop();
          this.csvDataTestBody.pop();
          this.getData('coor2', this.csvUrlCoorelation2);
        }

        if (typeCorrelation === 'coor2') {
          this.csvDataCoorBody2 = this.csvDataCoor2.slice();
          this.csvDataCoorHeader2 = this.csvDataCoorBody2.shift();
          this.csvDataCoorBody2.pop();
          this.getData('perfectmatrix', this.perfectMatrixheatmap1);
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
        type: this.translate.instant('Category.text')
      },
      title: {
        text: this.translate.instant('CreateContainer.text')
      }
    };




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
      this.dtOptions = { pagingType: 'full_numbers', pageLength: 10, language: LanguageApp.spanishdatatables };
    }else{
      this.dtOptions = { pagingType: 'full_numbers', pageLength: 10};
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
