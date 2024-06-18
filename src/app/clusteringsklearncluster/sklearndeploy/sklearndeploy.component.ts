import { Component, OnInit, Input, ViewChild } from '@angular/core';
// import { UploadFileComponent } from '../../uploadfile/uploadfile.component';
import { HttpClient } from '@angular/common/http';
// import { Observable } from 'rxjs';
import { ToastrService } from 'ngx-toastr';
import { UserService } from '../../services/index_services';
import { User } from '../../models/index_models';
import { ActivatedRoute } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { TranslateService } from '@ngx-translate/core';
import { LanguageApp } from '../../services/LanguageApp';
import { TranslationService } from '../../services/translation.service';
import { DomSanitizer, SafeResourceUrl, SafeUrl, SafeHtml, SafeStyle, SafeScript } from '@angular/platform-browser';
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
import { parse } from 'path';
import { strict } from 'assert';
import { param } from 'jquery';

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
  colors: string[];
  legend: ApexLegend;


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
  colors: string[];

};

@Component({
  selector: 'app-sklearndeploy',
  templateUrl: './sklearndeploy.component.html',
  styleUrls: ['./sklearndeploy.component.css']
})
export class SklearndeployComponent implements OnInit {

  @ViewChild('chartClusterBoxplot') chartCluster: ChartComponent;
  public chartOptionsCluster: Partial<ChartOptions>;
  @ViewChild('chartMatrix') charMatrix: ChartComponent;
  public chartOptionsMatrix: Partial<ChartOptions>;

  @ViewChild('chartPoints') chartPoints: ChartComponent;
  public chartOptionsPoints: Partial<ChartOptions>;

  @ViewChild('PredictionCluster') PredictionCluster: ChartComponent;
  public chartPredictionCluster: Partial<ChartOptions>;

  @ViewChild('TrueLabel') TrueLabel1: ChartComponent;
  public chartTrueLabel: Partial<ChartOptions>;

  @ViewChild('chartPredictPhase') chartBar: ChartComponent;
  public chartOptionsBar: Partial<ChartOptionsBar>;

  @Input() modelName: string;
  @Input() dateModelCreateModels: string;
  @Input() classColumn: string;
  currentUser: User;

  receivedParameters: string;
  itemsClassColumn: string;

  mY = [];
  mX = [];

  csvDataRegresion;

  public chartArrayTrainPre;
  csvDataTrainHeaderPre;
  csvDataTrainBodyPre = [];
  csvDataTrainBodyJustTwentyRows;
  csvDataTrainBodyJustTwentyRowsPre;
  csvDataTrain: Array<string[]> = [];
  public withoutQuotesAnomalyesTrainHeaders;
  public withoutQuotesAnomalyesTrainHeadersPre;

  @ViewChild('chartRegression2') chart: ChartComponent;
  public chartOptions: Partial<ChartOptions>;

  public width = '100%';
  public height = '600px';

  @ViewChild('chartRegression') chartTrain: ChartComponent;
  public chartOptionsRegression: Partial<ChartOptionsBoxplot>;
  public chartArrayRegressionGraphics;
  public chartArrayRegressionGraphics2;
  mymodels = false;

  dtOptions: DataTables.Settings = { pagingType: 'full_numbers', pageLength: 10 };

  observation = [{ path : '' }];
  barForce = [{ path : '' }];


  csvUrlClusterBoxplot: string[];
  csvUrlKMeansDataMatrix: string[];
  csvUrlDataPrediction: string[];
  csvUrlDataRegresion: string[];
  csvUrlDataRegresionPredictor: string[];
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

  public chartArrayMatrixGraphicsPre;


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
  csvDataRegressionBodyBodyMappedColumns = [];
  numregression = 0;
  csvDataregressionBodyJustTwentyRows;
  withoutQuotesAnomalyesregressionHeaders;
  public chartArrayregressionGraphics;


  chartArrayPhase = [];
  csvDataPhaseHeader = [];
  csvDataPhaseBody = [];
  csvDataPhaseBodyBodyMappedColumns = [];

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
  host: any;
  colors = ['#FF0000', '#00FF00', '#3366FF', '#9900FF', '#CCFF00', '#FF33FF', '#FFFF00', '#66FFFF', '#FF6666', '#000000', '#FF0040', '#B43104', ' #0B6121', '#01DFA5', '#2E2EFE', '#DF01A5', '#088A4B', '#B40431', '#1C1C1C', '#B45F04'];

  // Array final for basic graphics scatter
  arraySerieTL: { name: string; data: Array<number[]>}[] = [];
  arraySeriesPC: { name: string; data: Array<number[]>}[] = [];

  displayStyle = 'none';
  userSummaryDothtml;
  url;

  public isLoggedIn = false;
  public userProfile: KeycloakProfile | null = null;



  constructor(private http: HttpClient,
              private toastr: ToastrService,
              private userService: UserService,
              private route: ActivatedRoute,
              public sanitizeHTML: DomSanitizer,
              private translate: TranslateService,
              private transLang: TranslationService,
              private readonly keycloak: KeycloakService) { // , private mymodel: MymodelsComponent) {
    // upload user
    // this.currentUser = JSON.parse(localStorage.getItem('currentUser'));
    this.currentUser = { id: '', username: '', firstName: '', lastName: '' };    this.host = self.location.host.split(':')[0];


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
    async ngOnInit() {    // let color;
    // while (this.colors.length < 100) {
    //     do {
    //         color = Math.floor((Math.random() * 1000000) + 1);
    //     } while (this.colors.indexOf(color) >= 0);
    //     this.colors.push('#' + ('000000' + color.toString(16)).slice(-6));
    // }
    // console.log(this.colors);

    this.isLoggedIn = await this.keycloak.isLoggedIn();

    if (this.isLoggedIn) {
        this.userProfile = await this.keycloak.loadUserProfile();
        this.currentUser.id = this.userProfile.id;
        this.currentUser.username = this.userProfile.username;
        this.currentUser.firstName = this.userProfile.firstName;
        this.currentUser.lastName = this.userProfile.lastName;

    }

    this.tableLanguage();

    // Detect if rest is the mymodels or the new model
    this.route.params.subscribe(params =>
      // tslint:disable-next-line:no-string-literal
      this.receivedParameters = params['namemodels']

    );
    // if the data get of the mymodels build the date
    if (typeof this.receivedParameters !== 'undefined') {
      this.modelName = this.receivedParameters.split('.')[0];
      this.dateModelCreateModels = this.receivedParameters.split('.')[1].substring(0, this.receivedParameters.split('.')[1].length - 0);
      this.getJSONItemClass();
      this.mymodels = true;
    }

    this.getFilesGeneratedPicturesOfScriptsAlgorithm();

    // buid the url where is the csv with information of the model
    this.csvUrlDataRegresion = [this.modelName + '.' + this.dateModelCreateModels, this.modelName + '.' + this.dateModelCreateModels + '_data_regresion.csv'];
    // tslint:disable-next-line:max-line-length
    this.csvUrlDataRegresionPredictor = [this.modelName + '.' + this.dateModelCreateModels, this.modelName + '.' + this.dateModelCreateModels + '_data_regresion_prediction.csv'];



    // Readin of back end all the files.
    // this.getData('cluster', this.csvUrlClusterBoxplot);
    this.mappedNumberItemsToStringColumns();
    // this.getData('matrix', this.csvUrlKMeansDataMatrix);
    // this.getData('prediction', this.csvUrlDataPrediction);
    this.getData('regression', this.csvUrlDataRegresion);
    this.getData('regressionPre', this.csvUrlDataRegresionPredictor);

    // this.getData('phase', this.csvUrlPhaseBoxplot);
    // this.getData('predictor', this.csvUrlPredictorBoxplot);

     // link to html file in back-end
    // tslint:disable-next-line: max-line-length
    this.url = 'http://' + this.host + ':3001/users/files/downloadPicturesShapPlots/summary_plot_dot.html?username=' + this.currentUser.username + '&modelsname=' + this.modelName + '.' + this.dateModelCreateModels;

    // download html file of SHAP plots
    fetch(this.url)
      .then(response => response.text())
      .then(text => {
        this.userSummaryDothtml = text;
        // console.log(text);
        // console.log(this.userSummaryDothtml );
      });

    // sanitize html for security
    this.userSummaryDothtml = this.transformSanitize(this.userSummaryDothtml, 'html');






    this.chartOptionsCluster = {

      chart: {
        height: 900,
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
      // xaxis: {
      //   type: 'numeric',
      //   categories: this.rank
      // },
      title: {
        text: this.translate.instant('Clustering.text'),
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
        min: this.valueMinCharLinearY,
        max: this.valueMaxCharLinearY,
        title: {
          text: this.translate.instant('Engagement.text')
        }
      }
    };

    this.chartOptionsMatrix = {
      // series: [
      //   {
      //     name: 'Total Within SS',
      //     data: [19515, 17520, 15784]
      //   },
      //   {
      //     name: 'AIC',
      //     data: [19523, 17532, 15800]
      //   },
      //   {
      //     name: 'BIC',
      //     data: [19555, 17580, 15863 ]
      //   }
      // ],
      chart: {
        height: 900,
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
      // xaxis: {
      //   type: 'numeric',
      //   categories: this.rank
      // },
      title: {
        text: this.translate.instant('DataMatrix.text'),
        align: 'center',
        style: {
          fontSize: '14px',
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
        },
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
        animations: {
          enabled: false,
          speed: 5
        },
      },
      title: {
        text: this.translate.instant('RegressionChart.text'),
        align: 'center',
        style: {
          fontSize: '14px', // Ajusta el tamaño de la fuente aquí
        }
      },
      xaxis: {
        type: 'numeric',
        labels: {
          style: {
            fontSize: '14px', // Ajusta el tamaño de la fuente aquí
          }
        }
      },
      yaxis: {
        tooltip: {
          enabled: true
        },
        labels: {
          style: {
            fontSize: '14px', // Ajusta el tamaño de la fuente aquí
          }
        }
      }
    };


  }

  /**
   * Function for diferent types of sanitanize for security of external resorces
   */
  public transformSanitize(value: any, type: string): SafeHtml | SafeStyle | SafeScript | SafeUrl | SafeResourceUrl {
   switch (type) {
     case 'html':
       return this.sanitizeHTML.bypassSecurityTrustHtml(
         `${value}`.replace(/<p[^>]*>/g, '').replace(/<strong[^>]*>/g, '')
       );
     case 'text':
       const span = document.createElement('span');
       span.innerHTML = value;
       return span.textContent || span.innerText;
     case 'style':
       return this.sanitizeHTML.bypassSecurityTrustStyle(value);
     case 'script':
       return this.sanitizeHTML.bypassSecurityTrustScript(value);
     case 'url':
       return this.sanitizeHTML.bypassSecurityTrustUrl(value);
     case 'resourceUrl':
       return this.sanitizeHTML.bypassSecurityTrustResourceUrl(value);
     default:
       throw new Error(`Invalid safe type specified: ${type}`);
   }
 }

  public generateData(baseval, count, yrange): any {
    let i = 0;
    const series = [];
    while (i < count) {
      // var x =Math.floor(Math.random() * (750 - 1 + 1)) + 1;;
      const y =
        Math.floor(Math.random() * (yrange.max - yrange.min + 1)) + yrange.min;
      const z = Math.floor(Math.random() * (75 - 15 + 1)) + 15;

      series.push([baseval, y, z]);
      baseval += 86400000;
      i++;
    }
    return series;
  }


  /**
   * Get names images generated by the python script saved in json file for view in web UI or frond-end
   */
  getFilesGeneratedPicturesOfScriptsAlgorithm(): void{


    this.userService.getFilesGeneratedPicturesOfScriptsAlgorithm(this.currentUser, this.modelName + '.' + this.dateModelCreateModels)
      .subscribe(
        file => {
          console.log(file);

          file.Observation.forEach(element => {
            // tslint:disable-next-line: max-line-length
            const url = 'http://' + this.host + ':3001/users/files/downloadPicturesShapPlots/' + element + '?username=' + this.currentUser.username + '&modelsname=' + this.modelName + '.' + this.dateModelCreateModels;

            const temp = {path : url};
            this.observation.push(temp);
          });

          file.BarForce.forEach(element => {
            // tslint:disable-next-line: max-line-length
            const url = 'http://' + this.host + ':3001/users/files/downloadPicturesShapPlots/' + element + '?username=' + this.currentUser.username + '&modelsname=' + this.modelName + '.' + this.dateModelCreateModels;

            const temp = {path : url};
            this.barForce.push(temp);
          });

          this.observation.shift();
          this.barForce.shift();


        },
        error => {
           console.log('error');

        });



  }

  /**
   * Get names files chosen in trained in frond end  saved in json file for view in web UI or frond-end the plots
   */
  getJSONItemClass(): void{

    let value = '';

    this.userService.compressItemClases(this.currentUser, this.modelName + '.' + this.dateModelCreateModels)
      .subscribe(
        file => {
          // console.log(file[6].list);

          file.classesStringToNumberDictionary.forEach(element => {

            if (element.name === file.targetClass) {

              element.possibleValues.forEach(e => {
                value += e + ',';
              });

            }

          });
          // console.log(value.substring(0, value.length - 1));
          this.classColumn = value.substring(0, value.length - 1);

        },
        error => {
          console.log('error');

        });



    // return 'no pancreatic disease,benign hepatobilliary disease,pancreatic ductal adenocarcinoma';
  }

  /**
   * Call the file in back-end and serialize csv to data we can see in plots apex
   * @typeCorrelation
   * @url
   */
  getData(typeCorrelation: string, url: string[]): any {

    // for (const url of this.urls){

    // this.getInfo(url).subscribe(data => {

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

      if (typeCorrelation === 'regressionPre') {
        this.chartArrayTrainPre = [];
        this.csvDataTrainHeaderPre = [];
        this.csvDataTrainBodyPre = [];
      }

      list.forEach(e => {
        // for (let i = 1; i < list.length ; i++) {}
        switch (typeCorrelation) {
          case 'cluster':
            this.chartArrayClusterBoxplot.push(e.split(',')); // tables
            // if (this.numClusterBoxplot !== 0) {
            //   this.chartArrayTrain.push({name: this.transformRowsDataName(e.split(',')),
            //                         data: this.transformRowsData(e.split(','), this.csvDataTrain) }); // charts correlation matrix
            //   console.log(this.transformRowsData(e.split(','), this.csvDataTrain), 'regression');
            // } else {
            //   this.numTrain = 1;
            //   console.log('header table out: ', e.split(','));
            // }
            break;

          case 'matrix':
            this.chartArrayMatrix.push(e.split(',')); // tables
            // if (this.numClusterBoxplot !== 0) {
            //   this.chartArrayTrain.push({name: this.transformRowsDataName(e.split(',')),
            //                         data: this.transformRowsData(e.split(','), this.csvDataTrain) }); // charts correlation matrix
            //   console.log(this.transformRowsData(e.split(','), this.csvDataTrain), 'regression');
            // } else {
            //   this.numTrain = 1;
            //   console.log('header table out: ', e.split(','));
            // }
            break;

          case 'prediction':
            this.chartArrayPrediction.push(e.split(',')); // tables
            // if (this.numClusterBoxplot !== 0) {
            //   this.chartArrayTrain.push({name: this.transformRowsDataName(e.split(',')),
            //                         data: this.transformRowsData(e.split(','), this.csvDataTrain) }); // charts correlation matrix
            //   console.log(this.transformRowsData(e.split(','), this.csvDataTrain), 'regression');
            // } else {
            //   this.numTrain = 1;
            //   console.log('header table out: ', e.split(','));
            // }
            break;

          case 'regression':
            this.chartArrayregression.push(e.split(',')); // tables
            // if (this.numClusterBoxplot !== 0) {
            //   this.chartArrayTrain.push({name: this.transformRowsDataName(e.split(',')),
            //                         data: this.transformRowsData(e.split(','), this.csvDataTrain) }); // charts correlation matrix
            //   console.log(this.transformRowsData(e.split(','), this.csvDataTrain), 'regression');
            // } else {
            //   this.numTrain = 1;
            //   console.log('header table out: ', e.split(','));
            // }
            break;

          case 'phase':
            this.chartArrayPhase.push(e.split(',')); // tables
            // if (this.numClusterBoxplot !== 0) {
            //   this.chartArrayTrain.push({name: this.transformRowsDataName(e.split(',')),
            //                         data: this.transformRowsData(e.split(','), this.csvDataTrain) }); // charts correlation matrix
            //   console.log(this.transformRowsData(e.split(','), this.csvDataTrain), 'regression');
            // } else {
            //   this.numTrain = 1;
            //   console.log('header table out: ', e.split(','));
            // }
            break;

          case 'predictor':
            this.chartArrayPredictor.push(e.split(',')); // tables
            // if (this.numClusterBoxplot !== 0) {
            //   this.chartArrayTrain.push({name: this.transformRowsDataName(e.split(',')),
            //                         data: this.transformRowsData(e.split(','), this.csvDataTrain) }); // charts correlation matrix
            //   console.log(this.transformRowsData(e.split(','), this.csvDataTrain), 'regression');
            // } else {
            //   this.numTrain = 1;
            //   console.log('header table out: ', e.split(','));
            // }
            break;

          case 'regressionPre':
            this.chartArrayTrainPre.push(e.split(',')); // tables
            // if (this.numTrain !== 0) {
            //   this.chartArrayTrain.push({name: this.transformRowsDataName(e.split(',')),
            //                         data: this.transformRowsData(e.split(','), this.csvDataTrain) }); // charts correlation matrix
            //   console.log(this.transformRowsData(e.split(','), this.csvDataTrain), 'regression');
            // } else {
            //   this.numTrain = 1;
            //   console.log('header table out: ', e.split(','));
            // }
            break;


          default:
            break;
        }
        // console.log(e);
        // get JSON with columns mapped number to string for see.

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


      }

      if (typeCorrelation === 'regression') {
        this.csvDataregressionBody = [];
        this.csvDataregressionBody = this.chartArrayregression.slice();
        this.csvDataregressionHeader = [];
        this.csvDataregressionHeader = this.csvDataregressionBody.shift();
        this.csvDataregressionBody.pop();

        this.csvDataRegressionBodyBodyMappedColumns = [];
        this.csvDataRegressionBodyBodyMappedColumns = this.writeMapped(this.csvDataregressionHeader,
                                                               this.deleteColumnsLeyend,
                                                               this.csvDataregressionBody.slice());

        this.csvDataregressionBodyJustTwentyRows = [];
        for (let i = 0; i <= 20; i++) {
          // this.csvDataregressionBodyJustTwentyRows.push(this.csvDataregressionBody[i]);
          this.csvDataregressionBodyJustTwentyRows.push(this.csvDataRegressionBodyBodyMappedColumns[i]);
        }

        this.withoutQuotesAnomalyesregressionHeaders = [];
        for (let i = 0; i <= this.csvDataregressionHeader.length - 1; i++) {
          this.withoutQuotesAnomalyesregressionHeaders.push(this.csvDataregressionHeader[i].replace(/['"]+/g, '').replace(/\W/g, ''));
        }

        this.transformRowsDataAnomalies(this.chartArrayregression, 'boxplot');

      }

      if (typeCorrelation === 'phase') {

        this.csvDataPhaseBody = [];
        this.csvDataPhaseBody = this.chartArrayPhase.slice();
        this.csvDataPhaseHeader = [];
        this.csvDataPhaseHeader = this.csvDataPhaseBody.shift();
        this.csvDataPhaseBody.pop();

        this.csvDataPhaseBodyBodyMappedColumns = [];
        this.csvDataPhaseBodyBodyMappedColumns = this.writeMapped(this.csvDataPhaseHeader,
                                                               this.deleteColumnsLeyend,
                                                               this.csvDataPhaseBody.slice());

        this.csvDataPhaseBodyJustTwentyRows = [];
        for (let i = 0; i <= 20; i++) {
          this.csvDataPhaseBodyJustTwentyRows.push(this.csvDataPhaseBodyBodyMappedColumns[i]);
        }


        this.withoutQuotesAnomalyesPhaseHeaders = [];
        for (let i = 0; i <= this.csvDataPhaseHeader.length - 1; i++) {
          this.withoutQuotesAnomalyesPhaseHeaders.push(this.csvDataPhaseHeader[i].replace(/['"]+/g, '').replace(/\W/g, ''));
        }


      }

      if (typeCorrelation === 'regressionPre') {
        this.csvDataTrainBodyPre = this.chartArrayTrainPre.slice();
        this.csvDataTrainHeaderPre = this.csvDataTrainBodyPre.shift();
        this.csvDataTrainBodyPre.pop();
        this.csvDataTrainBodyPre.pop();

        this.csvDataTrainBodyJustTwentyRowsPre = [];
        for (let i = 0; i <= 20; i ++){
          this.csvDataTrainBodyJustTwentyRowsPre.push( this.csvDataTrainBodyPre[i]);
        }

        this.withoutQuotesAnomalyesTrainHeadersPre = [];
        for (let i = 0; i <= this.csvDataTrainHeaderPre.length - 1; i++) {
          this.withoutQuotesAnomalyesTrainHeadersPre.push(this.csvDataTrainHeaderPre[i].replace(/['"]+/g, '').replace(/\W/g, ''));
        }

        this.viewAll();

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

        this.countClustersForChartBar(this.csvDataPredictionBodyJustTwentyRows);

        // console.log(this.csvDataPredictionBodyJustTwentyRows , '###########Prediction');

        this.viewAll();

      }





    });
    // }
    // console.log(this.urls, 'Harcodeado');
    // console.log(this.chartArrayClusterBoxplot, 'Final Cluster');
    // console.log(this.chartArrayMatrix, 'Final Matrix');

    // console.log(this.chartArrayPrediction, 'Final Prediction');
    // console.log(this.chartArrayregression, 'Final Regression');

    // console.log(this.chartArrayPhase, 'Final Phase');
    // console.log(this.chartArrayPredictor, 'Final Predictor');

    this.numClusterBoxplot = 0;


  }

  transformRowsDataAnomalies(array: Array<string[]>, s: string): void {



    if (s === 'cluster') {
      this.chartArrayClusterBoxplot = [];
    }

    if (s === 'boxplot') {
      // this.chartArrayRegressionGraphics = [];
      this.chartArrayRegressionGraphics2 = [];
    }

    const final: { x: string; y: number[]; }[] = [];
    let tmp = [];

    for (let i = 0; i < array[i].length - 1; i++) {
      tmp = [];
      for (let j = 0; j < array.length - 2; j++) {
        tmp.push(array[j][i]);

      }
      const tmpY: number[] = [];
      for (let h = 1; h <= tmp.length - 2; h++) {
        // tslint:disable-next-line:radix
        tmpY.push(parseInt(tmp[h]));
      }
      final.push({
        x: tmp[0].replace(/\W/g, ''),
        y: tmpY
      });
    }

    if (s === 'cluster') {
      this.chartArrayClusterBoxplot.push({
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
  transformRowsData(array: Array<string[]>): { name: string; data: number[]; }[] {


    const tmp: number[] = [];
    const ss: number[] = [];
    const aic: number[] = [];
    const bic: number[] = [];
    const categ: number[] = [];
    // this.rank = [];

    const final: { name: string; data: number[]; }[] = [];

    this.xaxis = [];


    for (let j = 1; j < array.length - 1; j++) {
      // tslint:disable-next-line: radix
      categ.push(parseInt(array[j][0]));
      ss.push(parseFloat(array[j][1]));
      aic.push(parseFloat(array[j][2]));
      bic.push(parseFloat(array[j][3]));
      for (let i = 1; i < array[j].length; i++) {
        tmp.push(parseFloat(array[j][i]));
      }
    }

    this.valueMaxCharLinearY = Math.max(...tmp);
    this.valueMinCharLinearY = Math.min(...tmp);

    for (let i = 1; i < array.length - 2; i++) {

    }



    final.push({
      name: this.translate.instant('TotalWithinSS.text'),
      data: ss
    });
    final.push({
      name: 'AIC',
      data: aic
    });
    final.push({
      name: 'BIC',
      data: bic
    });

    this.rank = {
      type: 'numeric',
      categories: categ
    };

    // console.log(tmp, 'Temp');
    // console.log(this.rank, 'Rank');
    // console.log(ss, 'Within');
    // console.log(aic, 'AIC');
    // console.log(bic, 'BIC');
    // console.log(final, 'Final');

    return final;
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
    // console.log(array.length, "Todo");
    // console.log(array[0].length, "columnas");
    // console.log(array[0][0].length, "filas");

    for (let j = 0; j < array[j].length; j++) {
      tmp = [];
      tmpVal = [];
      tmp.push(array[0][j]);
      let count2 = 0;
      valNumber = [];
      for (let i = 1; i < array.length - 1; i++) {

        // tslint:disable-next-line: radix
        tmpVal.push(parseInt(array[i][j]));
        // tslint:disable-next-line: radix
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


  /**
   * a forEach cycle-based approach is used to flatten the array. Iterate over each row and then iterate over each value in the row. Each value is converted 
   * to a number using Number() and numbers are added to the array if it is a valid number (not NaN).
   * Then, the average value is calculated using the numbers obtained. To filter out elements outside the mean, 
   * the original array is iterated over and each row is converted into an array of numbers. Then, the average of each row is calculated and compared with the overall average value.
   * The result returned by the function is an object with the properties average and outOfAverage, where outOfAverage is a two-dimensional array of strings.
   * @param array 
   * @returns media and outOfMedia
   */
  getAverageValueAndOutOfAverageElements(array:  Array<string[]>): { media: number, outOfTheMedia:  Array<string[]> } {
    const numbers: number[] = [];
    array.forEach((row) => {
      row.forEach((value) => {
        const number = Number(value);
        if (!isNaN(number)) {
          numbers.push(number);
        }
      });
    });
  
    const sum = numbers.reduce((total, value) => total + value, 0);
    const media = sum / numbers.length;
  
    const outOfTheMedia = array.filter((row) => {
      const rowNumber = row.map((value) => Number(value));
      const averageRow = rowNumber.reduce((total, value) => total + value, 0) / rowNumber.length;
      return !isNaN(averageRow) && averageRow !== media;
    });
  
    return { media, outOfTheMedia };
  }


  /**
   * Eliminate the values closest to each other from an array of strings so that the display is faster in the frond and 
   * the browser does not get stuck because if more than 200 values are added, the graphic blocks the navigation.
   * @param array 
   * @param differ 
   * @param max 
   * @param min 
   * @param med 
   * @returns 
   */
  removeCloseValues(array: Array<string[]>, differ: number, max: number, min: number, med: number): Array<string[]> {
    // Copy the original array to avoid modifying it directly
    var newArray = array.slice()// this.filtrarNumerosPorUmbral(array.slice(),med - 0.00004 ,med + 0.6);

    // Iterate over array elements
    for (var i = 0; i < newArray.length - 1; i++) {
     
        // Calculate the difference between the current element and the next element.
        var difftemp0 = parseFloat(newArray[i][0]) - (parseFloat(newArray[i][1]))// < med + (-1.0) && med + 1.0 < parseFloat(newArray[i][1]) ? med : parseFloat(newArray[i][1]));
        var difftemp1 = parseFloat(newArray[i + 1][0]) - (parseFloat(newArray[i + 1][1]))// < med + -(1.0) && med + 1.0 < parseFloat(newArray[i + 1][1]) ? med : parseFloat(newArray[i + 1][1]));

        var diffEnd = difftemp0 - difftemp1
        //If the differences are less than or equal to 1, remove the values in the following row
        if (diffEnd <= differ && (diffEnd < max && diffEnd > min)) {
       
          delete newArray[i];
        }

    }
  

    //Filter out undefined values and reorganise indexes
    var resultado = this.reinitIndex(newArray);
    
    
   
    return resultado;
  }

  
  /**
   * Filter out undefined values and reorganise indexes
   * @param array 
   * @returns new array
   */
  reinitIndex(array: any[]): any[] {
    const newArray = array.filter((value) => value !== undefined);
    return newArray.map((value, index) => value);
  } 

  /**
   * Serialize datasets predictions and ture label, for view in animated plots.
   */
  transformClassPrediction(array: Array<string[]>): void{ // { name: string; data: Array<number[]>; }[]{

    const  {media , outOfTheMedia }= this.getAverageValueAndOutOfAverageElements(array);
    console.log("media:", media);
    console.log("Out of the  media:", outOfTheMedia);

    if (array.length > 2500){
      //array = array.slice(500,2000)
      array = this.removeCloseValues(array,0.0008,media+1.0,media-1.0, media);
    }

    const rawClassColum = this.classColumn.split(',');
    const classC = [];
    rawClassColum.forEach(element => {
      element = element.replace(/-/g, '_');
      classC.push(element.split('_').join(' '));
    });
    const clases = new Array(classC.length);

    for (let index = 0; index < clases.length; index++) {
      clases[index] = new Array(2);
      clases[index][0] = [];
      clases[index][1] = [];

    }


    let tempTL;
    let tempPC;
    let count = 0;

    for (let j = 0; j < array[j].length - 1; j++) {

            for (let i = 0; i < array.length - 1; i += 1) {

                switch (j) {
                  case 0:
                    count = 0;
                    // console.log('Prediction Label: ' +  array[i][2] , 'True Label: ' + array[i][3] );
                    tempTL = array[i][3]; //.slice(0, -1);  // replace(/\\/g, '');
                    tempPC = array[i][2]; // .slice(1, -1);  // replace(/\\/g, '');
                    // console.log('Prediction Label: ' +  tempPC , 'True Label: ' + tempTL);

                    classC.forEach(element => {

                        if (tempPC.split('_').join(' ') === element){
                          clases[count][0].push([Number(array[i][0].substring(0, array[i][0].length - 12)),
                                                        Number(array[i][1].substring(0, array[i][1].length - 12))]);
                        }
                        if (tempTL.split('_').join(' ') === element ){
                          clases[count][1].push([Number(array[i][0].substring(0, array[i][0].length - 12)),
                                                        Number(array[i][1].substring(0, array[i][1].length - 12))]);
                        }

                        count++;

                     });

                    break;

                }



            }

    }



    // save in final arrays for view
    for (let index = 0; index < classC.length; index++) {
      this.arraySeriesPC.push({name: classC[index], data: clases[index][0] });
      this.arraySerieTL.push({name: classC[index], data: clases[index][1] });
      console.log(clases[index][0]);
      console.log(clases[index][1]);


    }

    // tslint:disable-next-line:prefer-for-of
    for (let index = 0; index < this.arraySeriesPC.length; index++) {
      // tslint:disable-next-line: prefer-for-of
      for (let index2 = 0; index2 < this.arraySeriesPC[index].data.length; index2++) {
        this.mX.push(this.arraySeriesPC[index].data[index2][0]);
        this.mY.push(this.arraySeriesPC[index].data[index2][1]);
      }
    }

    // Deploy plots Scatter
    this.chargePlotScatter();

  }


  // Serialises data so that they can be consumed by tables and graphs
  transformRowsDataPredictorCluster(array: Array<string[]>): { name: string; data: Array<number[]>; }[] {



    let tmp: string[] = [];
    let tmpVal2: Array<number[]> = [];

    const tmpMaxMin: number[] = [];

    // this.rank = [];



    const finalOtro: { name: string; data: Array<number[]>; }[] = [];
    const finalOtro2: { name: string; data: Array<number[]>; }[] = [];




    // this.xaxisMatrix = [];
    // console.log(array.length, "Todo");
    // console.log(array[0].length, "columnas");
    // console.log(array[0][0].length, "filas");

    const regex = new RegExp('\"', 'g');
    // console.log( +array[2][array[0].length-1].replace(regex,""), "numero Columna predccion");

    for (let j = 0; j < array[j].length - 1; j++) {
      tmp = [];
      tmpVal2 = [];
      tmp.push(array[0][j].replace(regex, ''));
      // for(let i = 1 ; i < array.length ; i++){
      for (let i = 1; i < array.length - 1; i += 15) {
        // console.log(array[i][array[0].length-1], '0')
        // console.log(array[i][array[1].length-1], '1')

        const numberCluster = Number(array[i][array[0].length - 1].replace(regex, ''));

        // tmpVal.push([parseInt(array[i][j]),numberCluster]);
        // tslint:disable-next-line: radix
        tmpVal2.push([numberCluster, parseInt(array[i][j])]);

      }

      // finalOtro.push({name: tmp[0] , data: tmpVal  });
      finalOtro2.push({ name: tmp[0], data: tmpVal2 });


    }


    // console.log(finalOtro, 'Final');
    // console.log(finalOtro2, 'FinalOtro');


    // this.chartArrayPhaseGraphics2 = finalOtro;

    return finalOtro2;
  }

  viewAll(): void {

    // this.chartArrayClusterBoxplotGraphics = this.transformRowsData(this.chartArrayClusterBoxplot);
    // this.chartArrayMatrixGraphics = this.transformRowsDataMatrix(this.chartArrayMatrix);
    // this.chartArrayPhaseGraphics = this.transformRowsDataPredictorCluster(this.chartArrayPhase);

    // this.chartArrayMatrixGraphicsPre = this.transformRowsDataMatrix(this.csvDataTrainBodyPre);
    // this.chartArrayMatrixGraphicsPre =
    this.transformClassPrediction(this.csvDataTrainBodyPre);

    window.dispatchEvent(new Event('resize'));

  }

  tableLanguage(): void {
    if (this.transLang.activeLang === 'es') {
      this.dtOptions = { pagingType: 'full_numbers', pageLength: 10, language: LanguageApp.spanishdatatables };
    } else {
      this.dtOptions = { pagingType: 'full_numbers', pageLength: 10 };
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


    this.userService.compressDirectoryModelforSendToModelManager(this.currentUser, this.modelName + '.' + this.dateModelCreateModels)
      .subscribe(
        file => {
          console.log(file);
          // this.showToaster(this.modelName + '.' + this.dateModelCreateModels + '  ' + this.translate.instant('ModelDeleted.text'));
          this.deleteModels();

        },
        error => {
          // this.showToasterError(this.translate.instant('Error.text') + this.modelName + '.' + this.dateModelCreateModels
          //  + this.translate.instant('ModelDeletedError.text'));
          this.deleteModels();

        });



    this.showToaster(this.translate.instant('SaveModel.text'));

  }

  /**
   * delete of workspace the model.
   */
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
  *
  */
  writeMapped(header: string[], leyend: any, mapped: string[][]): any {


      const finalArray = [];

      mapped.slice().forEach(element => {
            finalArray.push(element.slice());

      });


      leyend.classesStringToNumberDictionary.forEach(element => {
              // tslint:disable-next-line:prefer-for-of
              for (let index = 0; index < header.length; index++) {
                if (element.name === header[index].replace(/['"]+/g, '')){
                  // tslint:disable-next-line: prefer-for-of
                  for (let i = 0; i < mapped.length; i++) {
                    // console.log(element.name + ': ', this.csvDataMatrixBody[i][index]);
                    // tslint:disable-next-line:prefer-for-of
                    for (let j = 0; j < element.possibleValues.length; j++) {
                      if (parseInt(mapped[i][index], 10) === j){
                          // tslint:disable-next-line:no-string-literal
                          finalArray[i][index] = element.possibleValues[j];
                          // console.log(element.name + ' --> ' +  j + ' : ', mapped[i][index]);

                      }

                    }

                  }
                }
              }
        });


      return finalArray;
  }

  /**
   * Count clusters in the propiertie, for view in the char Bar
   */
  countClustersForChartBar(informatiionCluster: any): void {

    const finalBar: {data: number[]; }[] = [];
    let xaxis: { xaxis: {categories: string[]} };

    const arrayValue = [];
    informatiionCluster.forEach(element => {
      arrayValue.push(parseInt(element[0].replace(/['"]+/g, ''), 10));
      console.log(parseInt(element[0].replace(/['"]+/g, ''), 10));
    });


    const indices = new Array(Math.max(...arrayValue) + 1); // colocar en vez de 8 el max del array "x"
    indices.fill(0);
    const temp = [];

    const xaxi = [];
    for (let i = 0; i < indices.length; i++) {
      temp.push('Cluster ' + i.toString());
      // tslint:disable-next-line:prefer-for-of
      for (let j = 0; j < arrayValue.length; j++) {
        if (i === arrayValue[j]) {
          indices[i] = indices[i] + 1;
        }
      }
    }

    console.log(temp);
    xaxis = { xaxis: {
        categories: temp
      }};

    finalBar.push({data: indices});
    console.log(this.chartOptionsBar.xaxis);
    console.log(xaxis);
    this.xaxisBar = xaxis;
    this.clusters = finalBar;
    console.log(indices);


  }

  openPopup(): void {
    this.displayStyle = 'block';
    // window.open(this.url, '_self');
  }
  closePopup(): void  {
    this.displayStyle = 'none';
  }

  // Charge plot with predictions
  chargePlotScatter(): void {

    this.chartPredictionCluster = {
      colors: this.colors,
      chart: {
        height: 900,
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
          },
          style: {
            fontSize: '14px', // Ajusta el tamaño de la fuente aquí
          }
        },
        // min: Math.min(...this.mX),
        // max: Math.max(...this.mX),
      },
      yaxis: {
        tickAmount: 7,
        min: Math.min(...this.mY),
        max: Math.max(...this.mY),
        labels: {
          style: {
            fontSize: '14px', // Ajusta el tamaño de la fuente aquí
          }
        }
      },
      title: {
        text: this.translate.instant('PredictionLabel.Text'),
        align: 'center',
        style: {
          fontSize: '14px', // Ajusta el tamaño de la fuente aquí
        }
      },
      legend: {
        position: "bottom",
        horizontalAlign: "center",
        fontSize: "14px"
      },
    };

    this.chartTrueLabel = {
      chart: {
        height: 900,
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
      colors: this.colors,
      xaxis: {
        tickAmount: 10,
        labels: {
          formatter: (val) => {
            return parseFloat(val).toFixed(1);
          },
          style: {
            fontSize: '14px', // Ajusta el tamaño de la fuente aquí
          }
        },
        // min: Math.min(...this.mX),
        // max: Math.max(...this.mX),
      },
      yaxis: {
        tickAmount: 7,
        min: Math.min(...this.mY),
        max: Math.max(...this.mY),
        labels: {
          style: {
            fontSize: '14px', // Ajusta el tamaño de la fuente aquí
          }
        },
      },
      title: {
        text: this.translate.instant('TrueLabel.Text'),
        align: 'center',
        style: {
          fontSize: '14px', // Ajusta el tamaño de la fuente aquí
        }
      },
      legend: {
        position: "bottom",
        horizontalAlign: "center",
        fontSize: "14px"
      },
    };


  }

}
