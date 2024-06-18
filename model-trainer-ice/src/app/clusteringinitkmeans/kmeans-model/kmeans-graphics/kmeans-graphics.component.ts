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
import { SanitizeHtmlPipe } from '../../../pipes/SanitizeHtmlPipe';
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
  yaxis: ApexYAxis;

};


@Component({
  selector: 'app-kmeans-graphics',
  templateUrl: './kmeans-graphics.component.html',
  styleUrls: ['./kmeans-graphics.component.css']
})
export class KmeansGraphicsComponent implements OnInit {

  @ViewChild('chartClusterBoxplot') chartCluster: ChartComponent;
  public chartOptionsCluster: Partial<ChartOptions>;
  @ViewChild('chartMatrix') charMatrix: ChartComponent;
  public chartOptionsMatrix: Partial<ChartOptions>;

  @ViewChild('chartPoints') chartPoints: ChartComponent;
  public chartOptionsPoints: Partial<ChartOptions>;

  @ViewChild('chartPoints2') chartPoints2: ChartComponent;
  public chartOptionsPoints2: Partial<ChartOptions>;

  @ViewChild('chartPredictPhase') chartBar: ChartComponent;
  public chartOptionsBar: Partial<ChartOptionsBar>;

  @ViewChild('PredictionCluster12') PredictionCluster12: ChartComponent;
  public chartPredictionCluster12: Partial<ChartOptions>;

  @ViewChild('PredictionCluster14') PredictionCluster14: ChartComponent;
  public chartPredictionCluster14: Partial<ChartOptions>;

  @ViewChild('PredictionClusterCentroids') PredictionClusterCentroids: ChartComponent;
  public chartPredictionClusterCentroids: Partial<ChartOptions>;

  @ViewChild('PredictionClusterConstrained') PredictionClusterConstrained: ChartComponent;
  public chartPredictionClusterConstrained: Partial<ChartOptions>;

  @ViewChild('PredictionClusterStandard') PredictionClusterStandard: ChartComponent;
  public chartPredictionClusterStandard: Partial<ChartOptions>;

  @ViewChild('PredictionClusterConstrained12') PredictionClusterConstrained12: ChartComponent;
  public chartPredictionClusterConstrained12: Partial<ChartOptions>;

  @ViewChild('PredictionClusterConstrained14') PredictionClusterConstrained14: ChartComponent;
  public chartPredictionClusterConstrained14: Partial<ChartOptions>;

  @ViewChild('TrueLabel') TrueLabel1: ChartComponent;
  public chartTrueLabel: Partial<ChartOptions>;

  @Input() modelName: string;
  @Input() dateModelCreateModels: string;
  @Input() valuesClassColumn: string;
  @Input() predic: string;

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

  dtOptions: DataTables.Settings = { pagingType: 'full_numbers', pageLength: 10, scrollX: true };

  observation = [{ path: '' }];
  barForce = [{ path: '' }];

  csvUrlClusterBoxplot: string[];
  csvUrlKMeansDataMatrix: string[];
  csvUrlDataPrediction: string[];
  csvUrlDataRegresion: string[];
  csvUrlPhaseBoxplot: string[];
  csvUrlPredictorBoxplot: string[];
  csvUrlAggregated12size: string[];
  csvUrlAggregated14size: string[];
  csvUrlCentroidsKmeans: string[];
  csvUrlConstrained: string[];
  csvUrlStandard: string[];
  csvUrlConstrained12: string[];
  csvUrlConstrained14: string[];



  chartArrayClusterBoxplot = [];
  csvDataClusterBoxplotHeader = [];
  csvDataClusterBoxplotBody = [];
  numClusterBoxplot = 0;
  csvDataClusterBoxplotBodyJustTwentyRows;
  withoutQuotesAnomalyesClusterBoxplotHeaders;
  public chartArrayClusterBoxplotGraphics;


  rank: { type: string, categories: number[]; labels: { style: { fontSize: string}}};
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

  rankMatrix: { type: string, categories: number[]; labels: { style: { fontSize: string}}};
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
  withoutQuotesAnomalyesPredictorHeaders = [];
  public chartArrayPredictorGraphics;


  chartArrayAggregated12sizeTrain = [];
  csvDataAggregated12sizeHeader = [];
  csvDataAggregated12sizeBody = [];
  numAggregated12size = 0;
  csvDataAggregated12sizeBodyJustTwentyRows;
  withoutQuotesAnomalyesAggregated12sizeHeaders = [];
  public chartArrayAggregated12sizeGraphics;


  chartArrayAggregated14sizeTrain = [];
  csvDataAggregated14sizeHeader = [];
  csvDataAggregated14sizeBody = [];
  numAggregated14size = 0;
  csvDataAggregated14sizeBodyJustTwentyRows;
  withoutQuotesAnomalyesAggregated14sizeHeaders = [];
  public chartArrayAggregated14sizeGraphics;

  chartArrayAggregatedCentroidssizeTrain = [];
  csvDataAggregatedCentroidssizeHeader = [];
  csvDataAggregatedCentroidssizeBody = [];
  numAggregatedCentroidssize = 0;
  csvDataAggregatedCentroidssizeBodyJustTwentyRows;
  withoutQuotesAnomalyesAggregatedCentroidssizeHeaders = [];
  public chartArrayAggregatedCentroidssizeGraphics;

  chartArrayAggregatedConstrainedsizeTrain = [];
  csvDataAggregatedConstrainedsizeHeader = [];
  csvDataAggregatedConstrainedsizeBody = [];
  numAggregatedConstrainedsize = 0;
  csvDataAggregatedConstrainedsizeBodyJustTwentyRows;
  withoutQuotesAnomalyesAggregatedConstrainedsizeHeaders = [];
  public chartArrayAggregatedConstrainedsizeGraphics;

  chartArrayAggregatedStandardsizeTrain = [];
  csvDataAggregatedStandardsizeHeader = [];
  csvDataAggregatedStandardsizeBody = [];
  numAggregatedStandardsize = 0;
  csvDataAggregatedStandardsizeBodyJustTwentyRows;
  withoutQuotesAnomalyesAggregatedStandardsizeHeaders = [];
  public chartArrayAggregatedStandardsizeGraphics;

  chartArrayAggregatedConstrained12sizeTrain = [];
  csvDataAggregatedConstrained12sizeHeader = [];
  csvDataAggregatedConstrained12sizeBody = [];
  numAggregatedConstrained12size = 0;
  csvDataAggregatedConstrained12sizeBodyJustTwentyRows;
  withoutQuotesAnomalyesAggregatedConstrained12sizeHeaders = [];
  public chartArrayAggregatedConstrained12sizeGraphics;

  chartArrayAggregatedConstrained14sizeTrain = [];
  csvDataAggregatedConstrained14sizeHeader = [];
  csvDataAggregatedConstrained14sizeBody = [];
  numAggregatedConstrained14size = 0;
  csvDataAggregatedConstrained14sizeBodyJustTwentyRows;
  withoutQuotesAnomalyesAggregatedConstrained14sizeHeaders = [];
  public chartArrayAggregatedConstrained14sizeGraphics;


  deleteColumnsLeyend = [];

  clusters = [];
  xaxisBar;
  host: any;
  loadImages = false;

  // Array final for basic graphics scatter
  arraySeriesAggregated12: { name: string; data: Array<number[]> }[] = [];
  arraySeriesAggregated14: { name: string; data: Array<number[]> }[] = [];
  arraySeriesAggregatedCentroids: { name: string; data: Array<number[]> }[] = [];
  arraySeriesAggregatedConstrained: { name: string; data: Array<number[]> }[] = [];
  arraySeriesAggregatedStandard: { name: string; data: Array<number[]> }[] = [];
  arraySeriesAggregatedConstrained12: { name: string; data: Array<number[]> }[] = [];
  arraySeriesAggregatedConstrained14: { name: string; data: Array<number[]> }[] = [];

  content = '<span>' +
    '<strong>' +
    '<span style="font-size: 50px;color:red">' +
    'Pull la la la' +
    '</span>' +
    '</strong>' +
    '</span>';


  cluster = false;
  matrix = false;
  prediction = false;
  regression = false;
  phase = false;
  predictor = false;
  aggregated12size = false;
  aggregated14size = false;
  aggregatedCentroidssize = false;
  aggregatedConstrainedsize = false;
  aggregatedStandardsize = false;
  aggregatedConstrained12size = false;
  aggregatedConstrained14size = false;

  numberColumnasPredict: number;

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
    this.currentUser = { id: '', username: '', firstName: '', lastName: '' }; this.host = self.location.host.split(':')[0];

    this.loadPlotsApex();

  }


  // tslint:disable-next-line:typedef
  async ngOnInit() {

    this.tableLanguage();
    this.isLoggedIn = await this.keycloak.isLoggedIn();

    if (this.isLoggedIn) {
      this.userProfile = await this.keycloak.loadUserProfile();
      this.currentUser.id = this.userProfile.id;
      this.currentUser.username = this.userProfile.username;
      this.currentUser.firstName = this.userProfile.firstName;
      this.currentUser.lastName = this.userProfile.lastName;

    }

    // Detect if rest is the mymodels or the new model
    this.route.params.subscribe(params =>
      // tslint:disable-next-line:no-string-literal
      this.receivedParameters = params['namemodels']
    );
    // if the data get of the mymodels build the date
    if (typeof this.receivedParameters !== 'undefined') {
      this.modelName = this.receivedParameters.split('.')[0];
      this.dateModelCreateModels = this.receivedParameters.split('.')[1].substring(0, this.receivedParameters.split('.')[1].length - 1);
      this.getJSONItemClass();
      this.mymodels = true;
    } else {
      this.numberColumnasPredict = this.predic.split(',').length;
    }

    this.getFilesGeneratedPicturesOfScriptsAlgorithm();


    // buid the url where is the csv with information of the model
    this.csvUrlClusterBoxplot = [this.modelName + '.' + this.dateModelCreateModels, 'Cluster_boxplot.csv'];
    this.csvUrlKMeansDataMatrix = [this.modelName + '.' + this.dateModelCreateModels, this.modelName + '.' + this.dateModelCreateModels + '1_data_matrix.csv'];
    this.csvUrlDataPrediction = [this.modelName + '.' + this.dateModelCreateModels, this.modelName + '.' + this.dateModelCreateModels + '1_data_prediction.csv'];
    this.csvUrlDataRegresion = [this.modelName + '.' + this.dateModelCreateModels, this.modelName + '.' + this.dateModelCreateModels + '_data_regresion.csv'];
    this.csvUrlPhaseBoxplot = [this.modelName + '.' + this.dateModelCreateModels, this.modelName + '.' + this.dateModelCreateModels + '_Phase_boxplot.csv'];
    this.csvUrlPredictorBoxplot = [this.modelName + '.' + this.dateModelCreateModels, this.modelName + '.' + this.dateModelCreateModels + '1_Predictor_boxplot.csv'];
    if (this.numberColumnasPredict < 3 || this.mymodels) {
      // tslint:disable-next-line: max-line-length
      this.csvUrlAggregated12size = [this.modelName + '.' + this.dateModelCreateModels, this.modelName + '.' + this.dateModelCreateModels + '_Aggregated_1_2_size_.csv'];
      // tslint:disable-next-line: max-line-length
      this.csvUrlAggregated14size = [this.modelName + '.' + this.dateModelCreateModels, this.modelName + '.' + this.dateModelCreateModels + '_Aggregated_1_4_size_.csv'];
      this.csvUrlCentroidsKmeans = [this.modelName + '.' + this.dateModelCreateModels, this.modelName + '.' + this.dateModelCreateModels + '_Centroids_of_Constrained_K-means_.csv'];
      this.csvUrlConstrained = [this.modelName + '.' + this.dateModelCreateModels, this.modelName + '.' + this.dateModelCreateModels + '_Predictions_of_Constrained_K-means_trained_Pancreatic-Cancer_Dataset_2.csv'];
      this.csvUrlStandard = [this.modelName + '.' + this.dateModelCreateModels, this.modelName + '.' + this.dateModelCreateModels + '_Predictions_of_standard_K-means.csv'];
      this.csvUrlConstrained12 = [this.modelName + '.' + this.dateModelCreateModels, this.modelName + '.' + this.dateModelCreateModels + '_Predictions_of_Constrained_K-means_trained_with_aggregated_1_4_of_size_Pancreatic-Cancer_Dataset_2.csv'];
      this.csvUrlConstrained14 = [this.modelName + '.' + this.dateModelCreateModels, this.modelName + '.' + this.dateModelCreateModels + '_Predictions_of_Constrained_K-means_trained_with_aggregated_1_2_of_size_Pancreatic-Cancer_Dataset_2.csv'];
    }



    this.loadImages = true;


    // Readin of back end all the files.
    this.getData('cluster', this.csvUrlClusterBoxplot);
    this.mappedNumberItemsToStringColumns();
    this.getData('matrix', this.csvUrlKMeansDataMatrix);
    this.getData('prediction', this.csvUrlDataPrediction);
    this.getData('regression', this.csvUrlDataRegresion);
    this.getData('phase', this.csvUrlPhaseBoxplot);
    this.getData('predictor', this.csvUrlPredictorBoxplot);
    if (this.numberColumnasPredict < 3 || this.mymodels) {
      this.getData('aggregated12size', this.csvUrlAggregated12size);
      this.getData('aggregated14size', this.csvUrlAggregated14size);
      this.getData('centroids', this.csvUrlCentroidsKmeans);
      this.getData('constrained', this.csvUrlConstrained);
      this.getData('standard', this.csvUrlStandard);
      this.getData('constrained12', this.csvUrlConstrained12);
      this.getData('constrained14', this.csvUrlConstrained14);

    }

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

      if (typeCorrelation === 'aggregated12size') {
        this.chartArrayAggregated12sizeTrain = [];
        this.csvDataAggregated12sizeHeader = [];
        this.csvDataAggregated12sizeBody = [];
      }

      if (typeCorrelation === 'aggregated14size') {
        this.chartArrayAggregated14sizeTrain = [];
        this.csvDataAggregated14sizeHeader = [];
        this.csvDataAggregated14sizeBody = [];
      }

      if (typeCorrelation === 'centroids') {
        this.chartArrayAggregatedCentroidssizeTrain = [];
        this.csvDataAggregatedCentroidssizeHeader = [];
        this.csvDataAggregatedCentroidssizeBody = [];
      }

      if (typeCorrelation === 'constrained') {
        this.chartArrayAggregatedConstrainedsizeTrain = [];
        this.csvDataAggregatedConstrainedsizeHeader = [];
        this.csvDataAggregatedConstrainedsizeBody = [];
      }

      if (typeCorrelation === 'standard') {
        this.chartArrayAggregatedStandardsizeTrain = [];
        this.csvDataAggregatedStandardsizeHeader = [];
        this.csvDataAggregatedStandardsizeBody = [];
      }

      if (typeCorrelation === 'constrained12') {
        this.chartArrayAggregatedConstrained12sizeTrain = [];
        this.csvDataAggregatedConstrained12sizeHeader = [];
        this.csvDataAggregatedConstrained12sizeBody = [];
      }

      if (typeCorrelation === 'constrained14') {
        this.chartArrayAggregatedConstrained14sizeTrain = [];
        this.csvDataAggregatedConstrained14sizeHeader = [];
        this.csvDataAggregatedConstrained14sizeBody = [];
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

          case 'boxplot':
            this.csvDataTrain.push(e.split(',')); // tables
            // if (this.numTrain !== 0) {
            //   this.chartArrayTrain.push({name: this.transformRowsDataName(e.split(',')),
            //                         data: this.transformRowsData(e.split(','), this.csvDataTrain) }); // charts correlation matrix
            //   console.log(this.transformRowsData(e.split(','), this.csvDataTrain), 'regression');
            // } else {
            //   this.numTrain = 1;
            //   console.log('header table out: ', e.split(','));
            // }
            break;

          case 'aggregated12size':
            this.chartArrayAggregated12sizeTrain.push(e.split(',')); // tables
            // if (this.numTrain !== 0) {
            //   this.chartArrayTrain.push({name: this.transformRowsDataName(e.split(',')),
            //                         data: this.transformRowsData(e.split(','), this.csvDataTrain) }); // charts correlation matrix
            //   console.log(this.transformRowsData(e.split(','), this.csvDataTrain), 'regression');
            // } else {
            //   this.numTrain = 1;
            //   console.log('header table out: ', e.split(','));
            // }
            break;

          case 'aggregated14size':
            this.chartArrayAggregated14sizeTrain.push(e.split(',')); // tables
            // if (this.numTrain !== 0) {
            //   this.chartArrayTrain.push({name: this.transformRowsDataName(e.split(',')),
            //                         data: this.transformRowsData(e.split(','), this.csvDataTrain) }); // charts correlation matrix
            //   console.log(this.transformRowsData(e.split(','), this.csvDataTrain), 'regression');
            // } else {
            //   this.numTrain = 1;
            //   console.log('header table out: ', e.split(','));
            // }
            break;

          case 'centroids':
            this.chartArrayAggregatedCentroidssizeTrain.push(e.split(',')); // tables
            // if (this.numTrain !== 0) {
            //   this.chartArrayTrain.push({name: this.transformRowsDataName(e.split(',')),
            //                         data: this.transformRowsData(e.split(','), this.csvDataTrain) }); // charts correlation matrix
            //   console.log(this.transformRowsData(e.split(','), this.csvDataTrain), 'regression');
            // } else {
            //   this.numTrain = 1;
            //   console.log('header table out: ', e.split(','));
            // }
            break;

          case 'constrained':
            this.chartArrayAggregatedConstrainedsizeTrain.push(e.split(',')); // tables
            // if (this.numTrain !== 0) {
            //   this.chartArrayTrain.push({name: this.transformRowsDataName(e.split(',')),
            //                         data: this.transformRowsData(e.split(','), this.csvDataTrain) }); // charts correlation matrix
            //   console.log(this.transformRowsData(e.split(','), this.csvDataTrain), 'regression');
            // } else {
            //   this.numTrain = 1;
            //   console.log('header table out: ', e.split(','));
            // }
            break;

          case 'standard':
            this.chartArrayAggregatedStandardsizeTrain.push(e.split(',')); // tables
            // if (this.numTrain !== 0) {
            //   this.chartArrayTrain.push({name: this.transformRowsDataName(e.split(',')),
            //                         data: this.transformRowsData(e.split(','), this.csvDataTrain) }); // charts correlation matrix
            //   console.log(this.transformRowsData(e.split(','), this.csvDataTrain), 'regression');
            // } else {
            //   this.numTrain = 1;
            //   console.log('header table out: ', e.split(','));
            // }
            break;

          case 'constrained12':
            this.chartArrayAggregatedConstrained12sizeTrain.push(e.split(',')); // tables
            // if (this.numTrain !== 0) {
            //   this.chartArrayTrain.push({name: this.transformRowsDataName(e.split(',')),
            //                         data: this.transformRowsData(e.split(','), this.csvDataTrain) }); // charts correlation matrix
            //   console.log(this.transformRowsData(e.split(','), this.csvDataTrain), 'regression');
            // } else {
            //   this.numTrain = 1;
            //   console.log('header table out: ', e.split(','));
            // }
            break;

          case 'constrained14':
            this.chartArrayAggregatedConstrained14sizeTrain.push(e.split(',')); // tables
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

        this.cluster = true;
        this.startViewAll();



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

        this.matrix = true;
        this.startViewAll();

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
        this.prediction = true;
        this.startViewAll();

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

        // this.transformRowsDataAnomalies(this.chartArrayregression, 'boxplot');

        this.regression = true;
        this.startViewAll();



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

        this.phase = true;
        this.startViewAll();

      }


      if (typeCorrelation === 'predictor') {
        this.csvDataPredictorBody = this.chartArrayPredictor.slice();
        this.csvDataPredictorHeader = this.csvDataPredictorBody.shift();
        this.csvDataAggregated12sizeBody.pop();

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
        this.predictor = true;
        this.startViewAll();

      }

      if (typeCorrelation === 'aggregated12size') {
        this.csvDataAggregated12sizeBody = this.chartArrayAggregated12sizeTrain.slice();
        this.csvDataAggregated12sizeHeader = this.csvDataAggregated12sizeBody.shift();
        this.csvDataAggregated12sizeBody.pop();
        this.csvDataAggregated12sizeBody.pop();

        this.csvDataAggregated12sizeBodyJustTwentyRows = [];
        for (let i = 0; i <= 20; i++) {
          this.csvDataAggregated12sizeBodyJustTwentyRows.push(this.csvDataAggregated12sizeBody[i]);
        }

        this.withoutQuotesAnomalyesAggregated12sizeHeaders = [];
        for (let i = 0; i <= this.csvDataAggregated12sizeHeader.length - 1; i++) {
          this.withoutQuotesAnomalyesAggregated12sizeHeaders.push(this.csvDataAggregated12sizeHeader[i].replace(/['"]+/g, '').replace(/\W/g, ''));
        }

        this.aggregated12size = true;


        this.startViewAll();


      }

      if (typeCorrelation === 'aggregated14size') {
        this.csvDataAggregated14sizeBody = this.chartArrayAggregated14sizeTrain.slice();
        this.csvDataAggregated14sizeHeader = this.csvDataAggregated14sizeBody.shift();
        this.csvDataAggregated14sizeBody.pop();
        this.csvDataAggregated14sizeBody.pop();

        this.csvDataAggregated14sizeBodyJustTwentyRows = [];
        for (let i = 0; i <= 20; i++) {
          this.csvDataAggregated14sizeBodyJustTwentyRows.push(this.csvDataAggregated14sizeBody[i]);
        }

        this.withoutQuotesAnomalyesAggregated14sizeHeaders = [];
        for (let i = 0; i <= this.csvDataAggregated14sizeHeader.length - 1; i++) {
          this.withoutQuotesAnomalyesAggregated14sizeHeaders.push(this.csvDataAggregated14sizeHeader[i].replace(/['"]+/g, '').replace(/\W/g, ''));
        }

        this.aggregated14size = true;


        this.startViewAll();


      }

      if (typeCorrelation === 'centroids') {
        this.csvDataAggregatedCentroidssizeBody = this.chartArrayAggregatedCentroidssizeTrain.slice();
        this.csvDataAggregatedCentroidssizeHeader = this.csvDataAggregatedCentroidssizeBody.shift();
        this.csvDataAggregatedCentroidssizeBody.pop();
        // this.csvDataAggregatedCentroidssizeBody.pop();

        this.csvDataAggregatedCentroidssizeBodyJustTwentyRows = [];
        for (let i = 0; i <= 20; i++) {
          this.csvDataAggregatedCentroidssizeBodyJustTwentyRows.push(this.csvDataAggregatedCentroidssizeBody[i]);
        }

        this.withoutQuotesAnomalyesAggregatedCentroidssizeHeaders = [];
        for (let i = 0; i <= this.csvDataAggregatedCentroidssizeHeader.length - 1; i++) {
          // tslint:disable-next-line: max-line-length
          this.withoutQuotesAnomalyesAggregatedCentroidssizeHeaders.push(this.csvDataAggregatedCentroidssizeHeader[i].replace(/['"]+/g, '').replace(/\W/g, ''));
        }

        this.aggregatedCentroidssize = true;


        this.startViewAll();


      }

      if (typeCorrelation === 'constrained') {
        this.csvDataAggregatedConstrainedsizeBody = this.chartArrayAggregatedConstrainedsizeTrain.slice();
        this.csvDataAggregatedConstrainedsizeHeader = this.csvDataAggregatedConstrainedsizeBody.shift();
        this.csvDataAggregatedConstrainedsizeBody.pop();
        this.csvDataAggregatedConstrainedsizeBody.pop();

        this.csvDataAggregatedConstrainedsizeBodyJustTwentyRows = [];
        for (let i = 0; i <= 20; i++) {
          this.csvDataAggregatedConstrainedsizeBodyJustTwentyRows.push(this.csvDataAggregatedConstrainedsizeBody[i]);
        }

        this.withoutQuotesAnomalyesAggregatedConstrainedsizeHeaders = [];
        for (let i = 0; i <= this.csvDataAggregatedConstrainedsizeHeader.length - 1; i++) {
          // tslint:disable-next-line: max-line-length
          this.withoutQuotesAnomalyesAggregatedConstrainedsizeHeaders.push(this.csvDataAggregatedConstrainedsizeHeader[i].replace(/['"]+/g, '').replace(/\W/g, ''));
        }

        this.aggregatedConstrainedsize = true;


        this.startViewAll();


      }

      if (typeCorrelation === 'standard') {
        this.csvDataAggregatedStandardsizeBody = this.chartArrayAggregatedStandardsizeTrain.slice();
        this.csvDataAggregatedStandardsizeHeader = this.csvDataAggregatedStandardsizeBody.shift();
        this.csvDataAggregatedStandardsizeBody.pop();
        this.csvDataAggregatedStandardsizeBody.pop();

        this.csvDataAggregatedStandardsizeBodyJustTwentyRows = [];
        for (let i = 0; i <= 20; i++) {
          this.csvDataAggregatedStandardsizeBodyJustTwentyRows.push(this.csvDataAggregatedStandardsizeBody[i]);
        }

        this.withoutQuotesAnomalyesAggregatedStandardsizeHeaders = [];
        for (let i = 0; i <= this.csvDataAggregatedStandardsizeHeader.length - 1; i++) {
          // tslint:disable-next-line: max-line-length
          this.withoutQuotesAnomalyesAggregatedStandardsizeHeaders.push(this.csvDataAggregatedStandardsizeHeader[i].replace(/['"]+/g, '').replace(/\W/g, ''));
        }

        this.aggregatedStandardsize = true;


        this.startViewAll();


      }


      if (typeCorrelation === 'constrained12') {
        this.csvDataAggregatedConstrained12sizeBody = this.chartArrayAggregatedConstrained12sizeTrain.slice();
        this.csvDataAggregatedConstrained12sizeHeader = this.csvDataAggregatedConstrained12sizeBody.shift();
        this.csvDataAggregatedConstrained12sizeBody.pop();
        this.csvDataAggregatedConstrained12sizeBody.pop();

        this.csvDataAggregatedConstrained12sizeBodyJustTwentyRows = [];
        for (let i = 0; i <= 20; i++) {
          this.csvDataAggregatedConstrained12sizeBodyJustTwentyRows.push(this.csvDataAggregatedConstrained12sizeBody[i]);
        }

        this.withoutQuotesAnomalyesAggregatedConstrained12sizeHeaders = [];
        for (let i = 0; i <= this.csvDataAggregatedConstrained12sizeHeader.length - 1; i++) {
          // tslint:disable-next-line: max-line-length
          this.withoutQuotesAnomalyesAggregatedConstrained12sizeHeaders.push(this.csvDataAggregatedConstrained12sizeHeader[i].replace(/['"]+/g, '').replace(/\W/g, ''));
        }

        this.aggregatedConstrained12size = true;


        this.startViewAll();


      }

      if (typeCorrelation === 'constrained14') {
        this.csvDataAggregatedConstrained14sizeBody = this.chartArrayAggregatedConstrained14sizeTrain.slice();
        this.csvDataAggregatedConstrained14sizeHeader = this.csvDataAggregatedConstrained14sizeBody.shift();
        this.csvDataAggregatedConstrained14sizeBody.pop();
        this.csvDataAggregatedConstrained14sizeBody.pop();

        this.csvDataAggregatedConstrained14sizeBodyJustTwentyRows = [];
        for (let i = 0; i <= 20; i++) {
          this.csvDataAggregatedConstrained14sizeBodyJustTwentyRows.push(this.csvDataAggregatedConstrained14sizeBody[i]);
        }

        this.withoutQuotesAnomalyesAggregatedConstrained14sizeHeaders = [];
        for (let i = 0; i <= this.csvDataAggregatedConstrained14sizeHeader.length - 1; i++) {
          // tslint:disable-next-line: max-line-length
          this.withoutQuotesAnomalyesAggregatedConstrained14sizeHeaders.push(this.csvDataAggregatedConstrained14sizeHeader[i].replace(/['"]+/g, '').replace(/\W/g, ''));
        }

        this.aggregatedConstrained14size = true;


        this.startViewAll();


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



  startViewAll(): void {

    if (this.cluster && this.matrix && this.regression && this.phase && this.predictor && this.aggregated12size && this.prediction &&
      this.aggregated14size && this.aggregated14size && this.aggregatedCentroidssize && this.aggregatedConstrainedsize &&
      this.aggregatedStandardsize && this.aggregatedConstrained12size && this.aggregatedConstrained14size &&
      this.numberColumnasPredict < 3) {
      this.cluster = false;
      this.matrix = false;
      this.prediction = false;
      this.regression = false;
      this.phase = false;
      this.predictor = false;
      this.aggregated12size = false;
      this.aggregated14size = false;
      this.aggregatedCentroidssize = false;
      this.aggregatedConstrainedsize = false;
      this.aggregatedStandardsize = false;
      this.aggregatedConstrained12size = false;
      this.aggregatedConstrained14size = false;


      this.viewAll();




      // tslint:disable-next-line: max-line-length
    } else if (this.cluster && this.matrix && this.regression && this.phase && this.predictor && this.prediction && this.numberColumnasPredict > 2) {
      this.cluster = false;
      this.matrix = false;
      this.prediction = false;
      this.regression = false;
      this.phase = false;
      this.predictor = false;

      this.viewAll();

    }



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
      categories: categ,
      labels: {
        style: {
          fontSize: '14px', // Ajusta el tamaño de la fuente aquí
        }
      },
      
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
          categories: valNumber,
          labels: {
            style: {
              fontSize: '14px', // Ajusta el tamaño de la fuente aquí
            }
          },
          
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

    this.transformRowsDataAnomalies(this.chartArrayregression, 'boxplot');
    this.chartArrayClusterBoxplotGraphics = this.transformRowsData(this.chartArrayClusterBoxplot);
    this.chartArrayMatrixGraphics = this.transformRowsDataMatrix(this.chartArrayMatrix);
    this.chartArrayPhaseGraphics = this.transformRowsDataPredictorCluster(this.chartArrayPhase);

    if (this.numberColumnasPredict < 3 || this.mymodels) {

      this.arraySeriesAggregated12 = this.transformClassPrediction(this.csvDataAggregated12sizeBody);
      this.arraySeriesAggregated14 = this.transformClassPrediction(this.csvDataAggregated14sizeBody);
      this.arraySeriesAggregatedCentroids = this.transformClassPrediction(this.csvDataAggregatedCentroidssizeBody);
      this.arraySeriesAggregatedConstrained = this.transformClassPrediction(this.csvDataAggregatedConstrainedsizeBody);
      this.arraySeriesAggregatedStandard = this.transformClassPrediction(this.csvDataAggregatedStandardsizeBody);
      this.arraySeriesAggregatedConstrained12 = this.transformClassPrediction(this.csvDataAggregatedConstrained12sizeBody);
      this.arraySeriesAggregatedConstrained14 = this.transformClassPrediction(this.csvDataAggregatedConstrained14sizeBody);

      this.arraySeriesAggregated12.push(this.arraySeriesAggregatedCentroids[0]);
      this.arraySeriesAggregatedConstrained.push(this.arraySeriesAggregatedCentroids[1]);
      this.arraySeriesAggregatedConstrained12.push(this.arraySeriesAggregatedCentroids[2]);
    }
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

  saveFinish(): void {

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
  deleteModels(): void {


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
          // console.log(json);
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
        if (element.name === header[index].replace(/['"]+/g, '')) {
          // tslint:disable-next-line: prefer-for-of
          for (let i = 0; i < mapped.length; i++) {
            // console.log(element.name + ': ', this.csvDataMatrixBody[i][index]);
            // tslint:disable-next-line:prefer-for-of
            for (let j = 0; j < element.possibleValues.length; j++) {
              if (parseInt(mapped[i][index], 10) === j) {
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

    const finalBar: { data: number[]; }[] = [];
    let xaxis: { xaxis: { categories: string[] } };

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
    xaxis = {
      xaxis: {
        categories: temp
      }
    };

    finalBar.push({ data: indices });
    console.log(this.chartOptionsBar.xaxis);
    console.log(xaxis);
    this.xaxisBar = xaxis;
    this.clusters = finalBar;
    console.log(indices);


  }

  /**
   * Serialize datasets predictions and ture label, for view in animated plots.
   */
  transformClassPrediction(array: Array<string[]>): { name: string; data: Array<number[]> }[] { // { name: string; data: Array<number[]>; }[]{

    const classC = this.valuesClassColumn.split(',');

    const clases = new Array(classC.length);


    for (let idx = 0; idx < clases.length; idx++) {
      clases[idx] = new Array(1);
      clases[idx][0] = [];

    }

    const arraySeries: { name: string; data: Array<number[]> }[] = [];


    let tempSeries;
    let count = 0;
    let nameclassTest = true;
    let index = 0;

    try {

      for (let j = 0; j < array[j].length - 1; j++) {

        for (let i = 0; i < array.length; i += 1) {

          switch (j) {
            case 0:
              // count = 0;
              // console.log('Class Label: ' +  array[i][2]);
              tempSeries = array[i][2]; // .slice(1, 0);  // replace(/\\/g, '');
              // console.log('Class Label Position: ' +  tempSeries);
              const nameClass = classC[Number(tempSeries)];
              // console.log('Class Label Name: ' +  nameClass);

              // centroid array
              if (!nameClass) {

                nameclassTest = false;


                if (i % 3 === 0) {
                  index = i / 3;
                }


                clases[index][0].push([Number(array[i][0]), // .substring(0, array[i][0].length - 3)),
                Number(array[i][1])]); // .substring(0, array[i][1].length - 3))]);

                // console.log('Add Array with: ' +  nameClass + ' this valors: ' +
                //               Number(array[i][0]) + ' , ' + Number(array[i][1]) + ' Number Array: ' + count);


                count++;

                // Normal plots arrays
              } else {

                classC.forEach(element => {

                  if (nameClass === element) {
                    clases[Number(tempSeries)][0].push([Number(array[i][0]), // .substring(0, array[i][0].length - 3)),
                    Number(array[i][1])]); // .substring(0, array[i][1].length - 3))]);

                    // console.log('Add Array with: ' +  nameClass + ' this valors: ' +
                    //               Number(array[i][0]) + ' , ' + Number(array[i][1]) + ' Number Array: ' + count);
                  }

                  count++;

                });
              }
              break;

          }



        }

      }

      if (!nameclassTest) {
        // save in final arrays for view in centroid array
        for (let idx = 0; idx < classC.length; idx++) {
          arraySeries.push({ name: 'Centroids', data: clases[idx][0] });
          console.log(clases[idx][0]);


        }

      } else {
        // save in final arrays for view
        for (let idx = 0; idx < classC.length; idx++) {
          arraySeries.push({ name: classC[idx], data: clases[idx][0] });
          console.log(clases[idx][0]);


        }
      }

    } catch (error) {
      console.log('Error, Empty Array');
    }
    return arraySeries;

  }

  /**
   * Load all Plots in Contructor
   */
  loadPlotsApex(): void {

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


    this.chartOptionsCluster = {

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
      // xaxis: {
      //   type: 'numeric',
      //   categories: this.rank
      // },
      title: {
        text: this.translate.instant('Clustering.text'),
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
        min: this.valueMinCharLinearY,
        max: this.valueMaxCharLinearY,
        title: {
          text: this.translate.instant('Engagement.text')
        },
        labels: {
          style: {
            fontSize: '14px', // Ajusta el tamaño de la fuente aquí
          }
        },
      },
      legend: {
        position: "bottom",
        horizontalAlign: "center",
        fontSize: "14px"
      },
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
      // xaxis: {
      //   type: 'numeric',
      //   categories: this.rank
      // },
      title: {
        text: this.translate.instant('DataMatrix.text'),
        align: 'center',
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
        },
        labels: {
          style: {
            fontSize: '14px', // Ajusta el tamaño de la fuente aquí
          }
        },
      },
      legend: {
        position: "bottom",
        horizontalAlign: "center",
        fontSize: "14px"
      },
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
          },
          style: {
            fontSize: '14px', // Ajusta el tamaño de la fuente aquí
          }
        }
      },
      yaxis: {
        tickAmount: 7,
        labels: {
          style: {
            fontSize: '14px', // Ajusta el tamaño de la fuente aquí
          }
        },
      },
      legend: {
        position: "bottom",
        horizontalAlign: "center",
        fontSize: "14px"
      },
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
        },
      },
      yaxis: {
        tooltip: {
          enabled: true
        },
        labels: {
          style: {
            fontSize: '14px', // Ajusta el tamaño de la fuente aquí
          }
        },
      },
      legend: {
        position: "bottom",
        horizontalAlign: "center",
        fontSize: "14px"
      },
    };

    this.chartPredictionCluster12 = {
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
          },
          style: {
            fontSize: '14px', // Ajusta el tamaño de la fuente aquí
          },
        }
      },
      yaxis: {
        tickAmount: 7,
        labels: {
          style: {
            fontSize: '14px', // Ajusta el tamaño de la fuente aquí
          }
        },
      },
      title: {
        text: 'Original Data',
        align: 'center',
        style: {
          fontSize: '14px', // Ajusta el tamaño de la fuente aquí
        },
      },
      legend: {
        position: "bottom",
        horizontalAlign: "center",
        fontSize: "14px"
      },
    };

    this.chartPredictionCluster14 = {
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
          },
          style: {
            fontSize: '14px', // Ajusta el tamaño de la fuente aquí
          },
        }
      },
      yaxis: {
        tickAmount: 7,
        labels: {
          style: {
            fontSize: '14px', // Ajusta el tamaño de la fuente aquí
          }
        },
      },
      title: {
        text: 'Original Data 2',
        align: 'center',
        style: {
          fontSize: '14px', // Ajusta el tamaño de la fuente aquí
        },
      },
      legend: {
        position: "bottom",
        horizontalAlign: "center",
        fontSize: "14px"
      },
    };


    this.chartPredictionClusterCentroids = {
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
          },
          style: {
            fontSize: '14px', // Ajusta el tamaño de la fuente aquí
          },
        }
      },
      yaxis: {
        tickAmount: 7,
        labels: {
          style: {
            fontSize: '14px', // Ajusta el tamaño de la fuente aquí
          }
        },
      },
      title: {
        text: 'Centroids',
        align: 'center',
        style: {
          fontSize: '14px', // Ajusta el tamaño de la fuente aquí
        },
      },
      legend: {
        position: "bottom",
        horizontalAlign: "center",
        fontSize: "14px"
      },
    };

    this.chartPredictionClusterConstrained = {
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
          },
          style: {
            fontSize: '14px', // Ajusta el tamaño de la fuente aquí
          },
        }
      },
      yaxis: {
        tickAmount: 7,
        labels: {
          style: {
            fontSize: '14px', // Ajusta el tamaño de la fuente aquí
          }
        },
      },
      title: {
        text: 'Standard Model',
        align: 'center',
        style: {
          fontSize: '14px', // Ajusta el tamaño de la fuente aquí
        },
      },
      legend: {
        position: "bottom",
        horizontalAlign: "center",
        fontSize: "14px"
      },
    };

    this.chartPredictionClusterStandard = {
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
          },
          style: {
            fontSize: '14px', // Ajusta el tamaño de la fuente aquí
          },
        }
      },
      yaxis: {
        tickAmount: 7,
        labels: {
          style: {
            fontSize: '14px', // Ajusta el tamaño de la fuente aquí
          }
        },
      },
      title: {
        text: 'Standard',
        align: 'center',
        style: {
          fontSize: '14px', // Ajusta el tamaño de la fuente aquí
        },
      },
      legend: {
        position: "bottom",
        horizontalAlign: "center",
        fontSize: "14px"
      },
    };

    this.chartPredictionClusterConstrained12 = {
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
          },
          style: {
            fontSize: '14px', // Ajusta el tamaño de la fuente aquí
          },
        }
      },
      yaxis: {
        tickAmount: 7,
        labels: {
          style: {
            fontSize: '14px', // Ajusta el tamaño de la fuente aquí
          }
        },
      },
      title: {
        text: 'Retrain Standard Model',
        align: 'center',
        style: {
          fontSize: '14px', // Ajusta el tamaño de la fuente aquí
        },
      },
      legend: {
        position: "bottom",
        horizontalAlign: "center",
        fontSize: "14px"
      },
    };

    this.chartPredictionClusterConstrained14 = {
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
          },
          style: {
            fontSize: '14px', // Ajusta el tamaño de la fuente aquí
          },
        }
      },
      yaxis: {
        tickAmount: 7,
        labels: {
          style: {
            fontSize: '14px', // Ajusta el tamaño de la fuente aquí
          }
        },
      },
      title: {
        text: 'Constrained',
        align: 'center',
        style: {
          fontSize: '14px', // Ajusta el tamaño de la fuente aquí
        },
      },
      legend: {
        position: "bottom",
        horizontalAlign: "center",
        fontSize: "14px"
      },
    };
  }

  /**
   * Get names images generated by the python script saved in json file for view in web UI or frond-end
   */
  getFilesGeneratedPicturesOfScriptsAlgorithm(): void {


    this.userService.getFilesGeneratedPicturesOfScriptsAlgorithm(this.currentUser, this.modelName + '.' + this.dateModelCreateModels)
      .subscribe(
        file => {
          console.log(file);

          file.Observation.forEach(element => {
            // tslint:disable-next-line: max-line-length
            const url = 'http://' + this.host + ':3001/users/files/downloadPicturesShapPlots/' + element + '?username=' + this.currentUser.username + '&modelsname=' + this.modelName + '.' + this.dateModelCreateModels;

            const temp = { path: url };
            this.observation.push(temp);
          });

          file.BarForce.forEach(element => {
            // tslint:disable-next-line: max-line-length
            const url = 'http://' + this.host + ':3001/users/files/downloadPicturesShapPlots/' + element + '?username=' + this.currentUser.username + '&modelsname=' + this.modelName + '.' + this.dateModelCreateModels;

            const temp = { path: url };
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
  getJSONItemClass(): void {

    let value = '';

    this.userService.compressItemClases(this.currentUser, this.modelName + '.' + this.dateModelCreateModels)
      .subscribe(
        file => {
          console.log('######################   ' + file.predictorClasses);
          this.numberColumnasPredict = file.predictorClasses.length;
          file.classesStringToNumberDictionary.forEach(element => {

            if (element.name === file.targetClass) {

              element.possibleValues.forEach(e => {
                value += e + ',';
              });

            }

          });
          console.log(value.substring(0, value.length - 1));
          this.valuesClassColumn = value.substring(0, value.length - 1);
          console.log(this.valuesClassColumn);

        },
        error => {
          console.log('error');

        });



    // return 'no pancreatic disease,benign hepatobilliary disease,pancreatic ductal adenocarcinoma';
  }



  openPopup(): void {
    this.displayStyle = 'block';
    // window.open(this.url, '_self');
  }
  closePopup(): void {
    this.displayStyle = 'none';
  }


}
