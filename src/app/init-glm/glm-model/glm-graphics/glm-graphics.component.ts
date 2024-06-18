import { Component, OnInit, Input, ViewChild } from '@angular/core';
// import { UploadFileComponent } from '../../uploadfile/uploadfile.component';
import { HttpClient } from '@angular/common/http';
// import { Observable } from 'rxjs';
import { ToastrService } from 'ngx-toastr';
import { UserService } from '../../../services/index_services';
import { User } from '../../../models/index_models';
import { ActivatedRoute } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { TranslateService } from '@ngx-translate/core';
import { LanguageApp } from '../../../services/LanguageApp';
import { TranslationService } from '../../../services/translation.service';
import { KeycloakService } from 'keycloak-angular';
import { KeycloakProfile } from 'keycloak-js';

import {
  ChartComponent,
  ApexAxisChartSeries,
  ApexChart,
  ApexXAxis,
  ApexDataLabels,
  ApexTitleSubtitle,
  ApexStroke,
  ApexGrid,
  ApexFill,
  ApexMarkers,
  ApexYAxis,
  ApexTheme,
  ApexTooltip,
  ApexPlotOptions,
  ApexLegend

} from 'ng-apexcharts';

export type ChartOptions = {
  series: ApexAxisChartSeries;
  chart: ApexChart;
  xaxis: ApexXAxis;
  dataLabels: ApexDataLabels;
  grid: ApexGrid;
  fill: ApexFill;
  markers: ApexMarkers;
  yaxis: ApexYAxis;
  stroke: ApexStroke;
  title: ApexTitleSubtitle;
  theme: ApexTheme;
  tooltip: ApexTooltip;
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

export type ChartOptionsBar = {
  series: ApexAxisChartSeries;
  chart: ApexChart;
  dataLabels: ApexDataLabels;
  plotOptions: ApexPlotOptions;
  xaxis: ApexXAxis;
};


@Component({
  selector: 'app-glm-graphics',
  templateUrl: './glm-graphics.component.html',
  styleUrls: ['./glm-graphics.component.css']
})
export class GlmGraphicsComponent implements OnInit {

  @ViewChild('chartClusterBoxplot') chartCluster: ChartComponent;
  public chartOptionsCluster: Partial<ChartOptions>;
  @ViewChild('chartMatrix') charMatrix: ChartComponent;
  public chartOptionsMatrix: Partial<ChartOptions>;

  @ViewChild('chartPoints') chartPoints: ChartComponent;
  public chartOptionsPoints: Partial<ChartOptions>;

  @ViewChild('chartPoints2') chartPoints2: ChartComponent;
  public chartOptionsPoints2: Partial<ChartOptions>;

  @ViewChild('chartPrediction') chartPrediction: ChartComponent;
  public chartOptionsPrediction: Partial<ChartOptionsBoxplot>;

  @ViewChild('chartPredictPhase') chartBar: ChartComponent;
  public chartOptionsBar: Partial<ChartOptionsBar>;


  @Input() modelName: string;
  @Input() dateModelCreateModels: string;
  currentUser: User;

  receivedParameters: string;

  csvDataRegresion;

  public chartArrayTrain;
  csvDataTrainHeader;
  csvDataTrainBody;
  csvDataTrainBodyJustTwentyRows;
  csvDataTrain: Array<string[]> = [];
  public withoutQuotesAnomalyesTrainHeaders;

  @ViewChild('chartRegression2') chart: ChartComponent;
  public chartOptions: Partial<ChartOptions>;



  @ViewChild('chartRegression') chartTrain: ChartComponent;
  public chartOptionsRegression: Partial<ChartOptionsBoxplot>;

  public chartArrayRegressionGraphics;
  public chartArrayRegressionGraphics2;
  mymodels = false;

  dtOptions: DataTables.Settings = { pagingType: 'full_numbers', pageLength: 10 };



  csvUrlClusterBoxplot: string[];
  csvUrlKMeansDataMatrix: string[];
  csvUrlDataPrediction: string[];
  csvUrlDataRegresion: string[];
  csvUrlPhaseBoxplot: string[];
  csvUrlPredictorBoxplot: string[];

  chartArrayClusterBoxplot = [];
  csvDataClusterBoxplotHeader = [];
  csvDataClusterBoxplotBody = [];
  numClusterBoxplot = 0;
  csvDataClusterBoxplotBodyJustTwentyRows;
  withoutQuotesAnomalyesClusterBoxplotHeaders;
  public chartArrayClusterBoxplotGraphics;


  rank: { type: string, categories: number[]; };
  valueMaxCharLinearY;
  valueMinCharLinearY;
  public xaxis;


  chartArrayMatrix = [];
  csvDataMatrixHeader = [];
  csvDataMatrixBody = [];
  csvDataMatrixBodyMappedColumns = [];


  numMatrix = 0;
  csvDataMatrixBodyJustTwentyRows;
  withoutQuotesAnomalyesMatrixHeaders;
  public chartArrayMatrixGraphics;

  rankMatrix: { type: string, categories: number[]; };
  valueMaxCharLinearYMatrix;
  valueMinCharLinearYMatrix;
  public xaxisMatrix;



  chartArrayPrediction = [];
  csvDataPredictionHeader = [];
  csvDataPredictionBody = [];
  numPrediction = 0;
  csvDataPredictionBodyJustTwentyRows;
  withoutQuotesAnomalyesPredictionHeaders;
  public chartArrayPredictionGraphics;

  chartArrayregression = [];
  csvDataregressionHeader = [];
  csvDataregressionBody = [];
  numregression = 0;
  csvDataregressionBodyJustTwentyRows;
  withoutQuotesAnomalyesregressionHeaders;
  public chartArrayregressionGraphics;


  chartArrayPhase = [];
  csvDataPhaseHeader = [];
  csvDataPhaseBody = [];
  numPhase = 0;
  csvDataPhaseBodyJustTwentyRows;
  withoutQuotesAnomalyesPhaseHeaders;
  public chartArrayPhaseGraphics;
  public chartArrayPhaseGraphics2;



  chartArrayPredictor = [];
  csvDataPredictorHeader = [];
  csvDataPredictorBody = [];
  numPredictor = 0;
  csvDataPredictorBodyJustTwentyRows;
  withoutQuotesAnomalyesPredictorHeaders;
  public chartArrayPredictorGraphics;

  deleteColumnsLeyend = [];

  clusters = [];
  xaxisBar;
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

    this.chartOptionsBar = {
      // series: [
      //   {
      //     data: [2, 10, 20, 12]
      //   }
      // ],
      chart: {
        type: 'bar',
        height: 350
      },
      plotOptions: {
        bar: {
          horizontal: true
        }
      },
      dataLabels: {
        enabled: false
      },
      xaxis: {
        categories: ['Cluster 0', 'Cluster 1', 'Cluster 2', 'Cluster 3']
      }
    };


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

    this.tableLanguage();


    this.route.params.subscribe(params =>
      // tslint:disable-next-line:no-string-literal
      this.receivedParameters = params['namemodels']
    );

    if (typeof this.receivedParameters !== 'undefined') {
      this.modelName = this.receivedParameters.split('.')[0];
      this.dateModelCreateModels = this.receivedParameters.split('.')[1];
      this.mymodels = true;
    }

    // this.csvUrlClusterBoxplot = [this.modelName + '.' + this.dateModelCreateModels, 'Cluster_boxplot.csv'];
    this.csvUrlKMeansDataMatrix = [this.modelName + '.' + this.dateModelCreateModels, this.modelName + '.' + this.dateModelCreateModels + '_data_matrix.csv'];
    this.csvUrlDataPrediction = [this.modelName + '.' + this.dateModelCreateModels, this.modelName + '.' + this.dateModelCreateModels + '_data_prediction.csv'];
    this.csvUrlDataRegresion = [this.modelName + '.' + this.dateModelCreateModels, this.modelName + '.' + this.dateModelCreateModels + '_data_regresion.csv'];
    this.csvUrlPhaseBoxplot = [this.modelName + '.' + this.dateModelCreateModels, this.modelName + '.' + this.dateModelCreateModels + '_Phase_boxplot.csv'];
    this.csvUrlPredictorBoxplot = [this.modelName + '.' + this.dateModelCreateModels, this.modelName + '.' + this.dateModelCreateModels + '_Predictor_boxplot.csv'];


    // Readin of back end all the files.
    // this.getData('cluster', this.csvUrlClusterBoxplot);
    this.mappedNumberItemsToStringColumns();

    this.getData('prediction', this.csvUrlDataPrediction);





    this.chartOptionsMatrix = {
      chart: {
        height: 600,
        width: 1200,
        type: 'line',
        animations: {
          enabled: false,
          speed: 5
        },
      },
      stroke: {
        width: 7,
        curve: 'smooth'
      },
      title: {
        text: this.translate.instant('DataMatrix.text'),
        align: 'left',
        style: {
          fontSize: '16px',
          color: '#666'
        }
      },
      fill: {
        type: 'gradient',
        gradient: {
          shade: 'dark',
          gradientToColors: ['#FDD835'],
          shadeIntensity: 1,
          type: 'horizontal',
          opacityFrom: 1,
          opacityTo: 1,
          stops: [0, 100, 100, 100]
        }
      },
      markers: {
        size: 4,
        colors: ['#FFA41B'],
        strokeColors: '#fff',
        strokeWidth: 2,
        hover: {
          size: 7
        }
      },
      yaxis: {
        min: this.valueMinCharLinearYMatrix,
        max: this.valueMaxCharLinearYMatrix,
        title: {
          text: this.translate.instant('Engagement.text')
        }
      }
    };


    this.chartOptionsPoints = {

      chart: {
        height: 600,
        width: 1200,
        type: 'scatter',
        zoom: {
          enabled: true,
          type: 'xy'
        },
        animations: {
          enabled: false,
          speed: 5
        },
      },
      xaxis: {
        tickAmount: 10,
        labels: {
          formatter: (val) => {
            return parseFloat(val).toFixed(1);
          }
        }
      },
      yaxis: {
        tickAmount: 7
      }
    };
    this.chartOptions = {
      chart: {
        type: 'candlestick',
        height: 600,
        width: 1200,
      },
      title: {
        text: this.translate.instant('RegressionChart.text'),
        align: 'left'
      },
      xaxis: {
        type: 'numeric'
      },
      yaxis: {
        tooltip: {
          enabled: true
        }
      }
    };
  }



  getData(typeCorrelation: string, url: string[]): any {


    this.userService.downloadJSONFiles(this.currentUser, url[1], url[0]).subscribe(data => {
      const list = data.split('\n');
      if (typeCorrelation === 'cluster') {
        this.chartArrayClusterBoxplot = [];
        this.csvDataClusterBoxplotHeader = [];
        this.csvDataClusterBoxplotBody = [];
      }

      if (typeCorrelation === 'matrix') {
        this.chartArrayMatrix = [];
        this.csvDataMatrixHeader = [];
        this.csvDataMatrixBody = [];
      }

      if (typeCorrelation === 'prediction') {
        this.chartArrayPrediction = [];
        this.csvDataPredictionHeader = [];
        this.csvDataPredictionBody = [];
      }

      if (typeCorrelation === 'regression') {
        this.chartArrayregression = [];
        this.csvDataregressionHeader = [];
        this.csvDataregressionBody = [];
      }

      if (typeCorrelation === 'phase') {
        this.chartArrayPhase = [];
        this.csvDataPhaseHeader = [];
        this.csvDataPhaseBody = [];
      }

      if (typeCorrelation === 'predictor') {
        this.chartArrayPredictor = [];
        this.csvDataPredictorHeader = [];
        this.csvDataPredictorBody = [];
      }



      list.forEach(e => {
        // for (let i = 1; i < list.length ; i++) {}
        switch (typeCorrelation) {
          case 'cluster':
            this.chartArrayClusterBoxplot.push(e.split(',')); // tables

            break;

          case 'matrix':
            this.chartArrayMatrix.push(e.split(',')); // tables

            break;

          case 'prediction':
            this.chartArrayPrediction.push(e.split(',')); // tables

            break;

          case 'regression':
            this.chartArrayregression.push(e.split(',')); // tables

            break;

          case 'phase':
            this.chartArrayPhase.push(e.split(',')); // tables

            break;

          case 'predictor':
            this.chartArrayPredictor.push(e.split(',')); // tables

            break;

          case 'boxplot':
            this.csvDataTrain.push(e.split(',')); // tables

            break;


          default:
            break;
        }
      });

      // seeialize data for tables
      if (typeCorrelation === 'cluster') {
        this.csvDataClusterBoxplotBody = this.chartArrayClusterBoxplot.slice();
        this.csvDataClusterBoxplotHeader = this.csvDataClusterBoxplotBody.shift();
        this.csvDataClusterBoxplotBody.pop();

        this.csvDataClusterBoxplotBodyJustTwentyRows = [];
        for (let i = 0; i <= 20; i++) {
          this.csvDataClusterBoxplotBodyJustTwentyRows.push(this.csvDataClusterBoxplotBody[i]);
        }

        this.withoutQuotesAnomalyesClusterBoxplotHeaders = [];
        for (let i = 0; i <= this.csvDataClusterBoxplotHeader.length - 1; i++) {
          this.withoutQuotesAnomalyesClusterBoxplotHeaders.push(this.csvDataClusterBoxplotHeader[i].replace(/['"]+/g, '')
                                                                .replace(/\W/g, ''));
        }



      }

      if (typeCorrelation === 'matrix') {
        this.csvDataMatrixBody = [];
        this.csvDataMatrixBody = this.chartArrayMatrix.slice();

        this.csvDataMatrixHeader = [];
        this.csvDataMatrixHeader = this.csvDataMatrixBody.shift();
        this.csvDataMatrixBody.pop();

        this.csvDataMatrixBodyMappedColumns = [];
        this.csvDataMatrixBodyMappedColumns = this.writeMapped(this.csvDataMatrixHeader,
                                                               this.deleteColumnsLeyend,
                                                               this.csvDataMatrixBody.slice());



        this.csvDataMatrixBodyJustTwentyRows = [];
        for (let i = 0; i <= 20; i++) {
          this.csvDataMatrixBodyJustTwentyRows.push(this.csvDataMatrixBody[i]);
        }

        this.withoutQuotesAnomalyesMatrixHeaders = [];
        for (let i = 0; i <= this.csvDataMatrixHeader.length - 1; i++) {
          this.withoutQuotesAnomalyesMatrixHeaders.push(this.csvDataMatrixHeader[i].replace(/['"]+/g, '').replace(/\W/g, ''));
        }



        this.getData('regression', this.csvUrlDataRegresion);

      }

      if (typeCorrelation === 'prediction') {
        this.csvDataPredictionBody = this.chartArrayPrediction.slice();
        this.csvDataPredictionHeader = this.csvDataPredictionBody.shift();
        this.csvDataPredictionBody.pop();

        this.csvDataPredictionBodyJustTwentyRows = [];
        for (let i = 0; i <= 20; i++) {
          this.csvDataPredictionBodyJustTwentyRows.push(this.csvDataPredictionBody[i]);
        }

        this.withoutQuotesAnomalyesPredictionHeaders = [];
        for (let i = 0; i <= this.csvDataPredictionHeader.length - 1; i++) {
          this.withoutQuotesAnomalyesPredictionHeaders.push(this.csvDataPredictionHeader[i].replace(/['"]+/g, '').replace(/\W/g, ''));
        }


        this.getData('matrix', this.csvUrlKMeansDataMatrix);

      }

      if (typeCorrelation === 'regression') {
        this.csvDataregressionBody = this.chartArrayregression.slice();
        this.csvDataregressionHeader = this.csvDataregressionBody.shift();
        this.csvDataregressionBody.pop();

        this.csvDataregressionBodyJustTwentyRows = [];
        for (let i = 0; i <= 20; i++) {
          this.csvDataregressionBodyJustTwentyRows.push(this.csvDataregressionBody[i]);
        }

        this.withoutQuotesAnomalyesregressionHeaders = [];
        for (let i = 0; i <= this.csvDataregressionHeader.length - 1; i++) {
          this.withoutQuotesAnomalyesregressionHeaders.push(this.csvDataregressionHeader[i].replace(/['"]+/g, '').replace(/\W/g, ''));
        }

        this.transformRowsDataAnomalies(this.chartArrayregression, 'boxplot');

        this.getData('phase', this.csvUrlPhaseBoxplot);

      }

      if (typeCorrelation === 'phase') {
        this.csvDataPhaseBody = this.chartArrayPhase.slice();
        this.csvDataPhaseHeader = this.csvDataPhaseBody.shift();
        this.csvDataPhaseBody.pop();

        this.csvDataPhaseBodyJustTwentyRows = [];
        for (let i = 0; i <= 20; i++) {
          this.csvDataPhaseBodyJustTwentyRows.push(this.csvDataPhaseBody[i]);
        }

        this.withoutQuotesAnomalyesPhaseHeaders = [];
        for (let i = 0; i <= this.csvDataPhaseHeader.length - 1; i++) {
          this.withoutQuotesAnomalyesPhaseHeaders.push(this.csvDataPhaseHeader[i].replace(/['"]+/g, '').replace(/\W/g, ''));
        }

        this.getData('predictor', this.csvUrlPredictorBoxplot);


      }



      if (typeCorrelation === 'predictor') {
        this.csvDataPredictorBody = this.chartArrayPredictor.slice();
        this.csvDataPredictorHeader = this.csvDataPredictorBody.shift();
        this.csvDataPredictorBody.pop();

        this.csvDataPredictorBodyJustTwentyRows = [];
        for (let i = 0; i <= 20; i++) {
          this.csvDataPredictorBodyJustTwentyRows.push(this.csvDataPredictorBody[i]);
        }

        this.withoutQuotesAnomalyesPredictorHeaders = [];
        for (let i = 0; i <= this.csvDataPredictorHeader.length - 1; i++) {
          this.withoutQuotesAnomalyesPredictorHeaders.push(this.csvDataPredictorHeader[i].replace(/['"]+/g, '').replace(/\W/g, ''));
        }

        this.viewAll();

      }





    });

    this.numClusterBoxplot = 0;


  }

  transformRowsDataAnomalies(array: Array<string[]>, s: string): void {



    if (s === 'prediction') {
      this.chartArrayPrediction = [];
    }

    if (s === 'boxplot') {
      // this.chartArrayRegressionGraphics = [];
      this.chartArrayRegressionGraphics2 = [];
    }

    const final: { x: string; y: number[]; }[] = [];
    let tmp = [];

    console.log(array.length, 'rows ' + s);
    console.log(array[1].length, 'columns ' + s);


    for (let i = 0; i < array[i].length; i++) {
      tmp = [];
      for (let j = 0; j < array.length - 2; j++) {
        tmp.push(array[j][i]);

      }
      const tmpY: number[] = [];
      for (let h = 1; h <= tmp.length - 2; h++) {
        // tslint:disable-next-line:radix
        tmpY.push(parseFloat(tmp[h]));
      }
      final.push({
        x: tmp[0].replace(/\W/g, ''),
        y: tmpY
      });
    }

    if (s === 'prediction') {
      this.chartArrayPrediction.push({
        type: 'boxPlot',
        data: final
      });
    }

    if (s === 'boxplot') {
      // this.chartArrayRegressionGraphics.push({
      //   type: 'boxPlot',
      //   data: final
      // });
      this.chartArrayRegressionGraphics2.push({
        name: 'candle',
        data: final
      });

    }


  }



  // Serialises data so that they can be consumed by tables and graphs
  transformRowsDataMatrix(array: Array<string[]>): { name: string; data: number[]; }[] {


    let tmp: string[] = [];
    let tmpVal: number[] = [];
    let valNumber: number[] = [];
    const tmpMaxMin: number[] = [];
    let count = 0;

    // this.rank = [];

    const final: { name: string; data: number[]; }[] = [];

    this.xaxisMatrix = [];

    for (let j = 0; j < array[j].length; j++) {
      tmp = [];
      tmpVal = [];
      tmp.push(array[0][j]);
      // tslint:disable-next-line:no-var-keyword
      var count2 = 0;
      valNumber = [];
      for (let i = 1; i < array.length - 1; i++) {

        // tslint:disable-next-line:radix
        tmpVal.push(parseInt(array[i][j]));
        // tslint:disable-next-line:radix
        tmpMaxMin.push(parseInt(array[i][j]));
        count2 += 0.25;
        valNumber.push(count2);

      }


      final.push({ name: tmp[0], data: tmpVal });


      count++;

      if (count === array[0].length) {
        this.rankMatrix = {
          type: 'Value',
          categories: valNumber
        };
      }
    }

    this.valueMaxCharLinearYMatrix = Math.max(...tmpMaxMin);
    this.valueMinCharLinearYMatrix = Math.min(...tmpMaxMin);







    return final;
  }
  // Serialises data so that they can be consumed by tables and graphs
  transformRowsDataPredictorCluster(array: Array<string[]>): { name: string; data: Array<number[]>; }[] {



    let tmp: string[] = [];
    // let tmpVal: Array<number[]> = [];
    let tmpVal2: Array<number[]> = [];

    const tmpMaxMin: number[] = [];
    const count = 0;



    const finalOtro: { name: string; data: Array<number[]>; }[] = [];
    const finalOtro2: { name: string; data: Array<number[]>; }[] = [];


    const regex = new RegExp('\"', 'g');

    for (let j = 0; j < array[j].length - 1; j++) {
      tmp = [];
      // tmpVal = [];
      tmpVal2 = [];
      tmp.push(array[0][j].replace(regex, ''));
      for (let i = 1; i < array.length - 1; i++) {


        const numberCluster = Number(array[i][array[0].length - 1].replace(regex, ''));

        // tslint:disable-next-line:radix
        tmpVal2.push([numberCluster, parseInt(array[i][j])]);

      }

      finalOtro2.push({ name: tmp[0], data: tmpVal2 });


    }



    return finalOtro2;
  }

  viewAll(): void {

    this.transformRowsDataAnomalies(this.chartArrayPrediction, 'prediction');

    this.chartArrayMatrixGraphics = this.transformRowsDataMatrix(this.chartArrayMatrix);
    this.chartArrayPhaseGraphics = this.transformRowsDataPredictorCluster(this.chartArrayPhase);
    this.chartOptionsPrediction = {
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
        text: this.translate.instant('PredictionGLM.text'),
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

  // Information OK
  showToaster(message: string): any {
    this.toastr.success(message);
  }
  // Information no OK
  showToasterError(message: string): any {
    this.toastr.error(message);
  }

  tableLanguage(): void {
    if (this.transLang.activeLang === 'es') {
      this.dtOptions = { pagingType: 'full_numbers', pageLength: 10, language: LanguageApp.spanishdatatables };
    } else {
      this.dtOptions = { pagingType: 'full_numbers', pageLength: 10 };
    }
  }

  saveFinish(): void{
    this.showToaster(this.translate.instant('SaveModel.text'));
  }

 deleteModels(): void{


    this.userService.deleteModels(this.currentUser, this.modelName + '.' + this.dateModelCreateModels)
      .subscribe(
        file => {
          console.log(file);
          this.showToaster(this.modelName + '.' + this.dateModelCreateModels + '  ' + this.translate.instant('ModelDeleted.text'));

        },
        error => {
          this.showToasterError(this.translate.instant('Error.text') + this.modelName + '.' + this.dateModelCreateModels
           + this.translate.instant('ModelDeletedError.text'));
        });



  }

  /**
   * Download and save the json file with map, numbers to strings
   */
  mappedNumberItemsToStringColumns(): void {

    this.userService.getLeyendJSONColumnsObject(this.currentUser, this.modelName + '.' + this.dateModelCreateModels)
              .subscribe(
                json => {
                  console.log(json);
                  this.deleteColumnsLeyend = json;
                },
                error => {
                });


  }


 /**
  * Assigning the numbers to real word names, so that the user is shown the original items in the columns.
  */
  writeMapped(header: string[], leyend: any, mapped: string[][]): any {


      const finalArray = [];

      mapped.slice().forEach(element => {
            finalArray.push(element.slice());

      });


      leyend.forEach(element => {
              // tslint:disable-next-line:prefer-for-of
              for (let index = 0; index < header.length; index++) {
                if (element.columns === header[index].replace(/['"]+/g, '')){
                  // tslint:disable-next-line: prefer-for-of
                  for (let i = 0; i < mapped.length; i++) {
                    // tslint:disable-next-line:prefer-for-of
                    for (let j = 0; j < element.list.length; j++) {
                      if (parseInt(mapped[i][index], 10) === j){
                          // tslint:disable-next-line:no-string-literal
                          finalArray[i][index] = element.list[j];
                          // console.log(element.columns + ' --> ' +  j + ' : ', mapped[i][index]);

                      }

                    }

                  }
                }
              }
        });


      return finalArray;
  }


}
