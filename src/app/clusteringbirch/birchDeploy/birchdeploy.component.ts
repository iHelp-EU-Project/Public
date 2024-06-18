// import {summary} from 'summary';
const summary = require('summary');
import { Component, OnInit, Input, ViewChild } from '@angular/core';
// import { UploadFileComponent } from '../../uploadfile/uploadfile.component';
import { HttpClient } from '@angular/common/http';
// import { Observable } from 'rxjs';
import { ToastrService } from 'ngx-toastr';
import { UserService } from '../../services/index_services';
import { User } from '../../models/index_models';
import { ActivatedRoute } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { KeycloakService } from 'keycloak-angular';
import { KeycloakProfile } from 'keycloak-js';
import { TranslateService } from '@ngx-translate/core';
import { LanguageApp } from '../../services/LanguageApp';
import { TranslationService } from '../../services/translation.service';
import { DomSanitizer, SafeResourceUrl, SafeUrl, SafeHtml , SafeStyle, SafeScript} from '@angular/platform-browser';
import {
  ChartComponent,
  ApexAxisChartSeries,
  ApexTitleSubtitle,
  ApexDataLabels,
  ApexChart,
  ApexPlotOptions,
  ApexXAxis,
  ApexGrid,
  ApexStroke,
  ApexYAxis,
  ApexLegend



} from 'ng-apexcharts';


export type ChartOptions = {
  series: ApexAxisChartSeries;
  chart: ApexChart;
  dataLabels: ApexDataLabels;
  fill: any;
  colors: any;
  title: ApexTitleSubtitle;
  xaxis: ApexXAxis;
  yaxis: ApexYAxis;
  grid: ApexGrid;
  plotOptions: ApexPlotOptions;
  stroke: ApexStroke;
  legend: ApexLegend;


};

@Component({
  selector: 'app-birchdeploy',
  templateUrl: './birchdeploy.component.html',
  styleUrls: ['./birchdeploy.component.css']
})
export class BirchdeployComponent implements OnInit {

 
  @ViewChild('chart') chart: ChartComponent;
  public chartOptions: Partial<ChartOptions>;

  @ViewChild('chartLine') chartLine: ChartComponent;
  public chartOptionsLine: Partial<ChartOptions>;

  @ViewChild('chartLineBar') chartLineBar: ChartComponent;
  public chartOptionsLineBar: Partial<ChartOptions>;

  @ViewChild('ScatterCluster') ScatterCluster: ChartComponent;
  public chartScatterCluster: Partial<ChartOptions>;

  @ViewChild('ScatterTrueLabel') ScatterTrueLabel: ChartComponent;
  public chartScatterTrueLabel: Partial<ChartOptions>;

  @Input() modelName: string;
  @Input() dateModelCreateModels: string;
  @Input() classColumn: string;
  currentUser: User;

  host: any;

  mymodels = false;

  arraySerieTL: { name: string; data: Array<number[]>}[] = [];

  rank: { type: string, categories: string[]; labels: { style: { fontSize: string}}};
  rankSnake: { type: string, categories: string[]; labels: { style: { fontSize: string}}};

///////////////////////////////////////////
  arraySerieTLScatter: { name: string; data: Array<number[]>}[] = [];
  arraySeriesPCScatter: { name: string; data: Array<number[]>}[] = [];

  public chartArraySkatterRefreshTrueWardScatter;

  mYScatter = [];
  mXScatter = [];

//////////////////////////////////////////////////

  valueMaxCharLinearY;
  valueMinCharLinearY;
  public xaxis;

  deleteColumnsLeyend = [];

  dtOptions: DataTables.Settings = { pagingType: 'full_numbers', pageLength: 10 };
  receivedParameters: string;

  observation = [{ path : '' }];
  barForce = [{ path : '' }];

  csvUrlDataHeadMap: string[];
  csvUrlDataSnake: string[];
  csvUrlData: string[];
  csvScatter: string[];


  numClusterBoxplot = 0;

  chartArrayHeadMap = [];
  csvDataHeadMapHeader = [];
  csvDataHeadMapBody = [];


  chartArraySnake = [];
  csvDataSnakeHeader = [];
  csvDataSnakeBody = [];

  chartArrayScatter = [];
  csvDataScatterHeader = [];
  csvDataScatterBody = [];

  csvDataScatterBodyJustTwentyRows;
  withoutQuotesAnomalyesScatterHeaders;
  public chartArrayScatterGraphics;


  csvDataHeadMapBodyJustTwentyRows;
  withoutQuotesAnomalyesHeadMapHeaders;
  public chartArrayHeadMapGraphics;

  public chartArrayHeadMapPre;

  csvDataSnakeBodyJustTwentyRows;
  withoutQuotesAnomalyesSnakeHeaders;
  public chartArraySnakeGraphics;

  chartArrayData = [];
  csvDataDataHeader = [];
  csvDataDataBody = [];

  csvDataDataBodyJustTwentyRows;
  withoutQuotesAnomalyesDataHeaders;
  public chartArrayDataGraphics;

  public chartArraySnakePre;
  public chartArrayScatterPre;


  displayStyle = 'none';
  userSummaryDothtml;
  url;
  colors = ['#FF0000', '#00FF00', '#3366FF', '#9900FF', '#CCFF00', '#FF33FF', '#FFFF00', '#66FFFF', '#FF6666', '#000000', '#FF0040', '#B43104', ' #0B6121', '#01DFA5', '#2E2EFE', '#DF01A5', '#088A4B', '#B40431', '#1C1C1C', '#B45F04'];

  public chartArrayBarRefresh;
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
    this.currentUser = { id: '', username: '' , firstName: '' , lastName: ''};

    this.host = self.location.host.split(':')[0];



    // this.plot();

  }
  // tslint:disable-next-line:typedef
  async ngOnInit() {

    this.tableLanguage();

    this.isLoggedIn = await this.keycloak.isLoggedIn();

    if (this.isLoggedIn) {
      this.userProfile = await this.keycloak.loadUserProfile();
      // this.currentUser = await this.keycloak.loadUserProfile();
      // this.currentUser.password = 'x';
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
      this.dateModelCreateModels = this.receivedParameters.split('.')[1].substring(0, this.receivedParameters.split('.')[1].length - 0);
      this.getJSONItemClass();
      this.mymodels = true;
    }

    this.getFilesGeneratedPicturesOfScriptsAlgorithm();

    // buid the url where is the csv with information of the model
    if (this.mymodels){
      this.csvUrlData = [this.modelName + '.' + this.dateModelCreateModels, this.modelName + '.' + this.dateModelCreateModels + '_dataFinal.csv'];
    }
    this.csvUrlDataHeadMap = [this.modelName + '.' + this.dateModelCreateModels, this.modelName + '.' + this.dateModelCreateModels + 'birch_cluster.csv'];
    this.csvUrlDataSnake = [this.modelName + '.' + this.dateModelCreateModels, this.modelName + '.' + this.dateModelCreateModels + 'birch_snake_plot.csv'];
    this.csvScatter = [this.modelName + '.' + this.dateModelCreateModels, this.modelName + '.' + this.dateModelCreateModels + '_data_regresion_prediction_birch.csv'];




    // Readin of back end all the files.
    // this.getData('cluster', this.csvUrlClusterBoxplot);
    this.mappedNumberItemsToStringColumns();
    // this.getData('matrix', this.csvUrlKMeansDataMatrix);
    if (this.mymodels){
      this.getData('data', this.csvUrlData);
    }
    this.getData('headmap', this.csvUrlDataHeadMap);
    this.getData('snake', this.csvUrlDataSnake);
    this.getData('scatter', this.csvScatter);

  }

   // Information OK
  showToaster(message: string): any {
    this.toastr.success(message);
  }
  // Information no OK
  showToasterError(message: string): any {
    this.toastr.error(message);
  }

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

  public generateData(count, yrange): any {
    let i = 0;
    const series = [];
    while (i < count) {
      const y = Math.floor(Math.random() * (yrange.max - yrange.min + 1)) + yrange.min;

      series.push(y);
      i++;
    }
    return series;
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
      if (typeCorrelation === 'headmap') {
        this.chartArrayHeadMap = [];
        this.csvDataHeadMapHeader = [];
        this.csvDataHeadMapBody = [];
      }

      if (typeCorrelation === 'snake') {
        this.chartArraySnake = [];
        this.csvDataSnakeHeader = [];
        this.csvDataSnakeBody = [];
      }

      if (typeCorrelation === 'data') {
        this.chartArrayData = [];
        this.csvDataDataHeader = [];
        this.csvDataDataBody = [];
      }

      if (typeCorrelation === 'scatter') {
        this.chartArrayScatter = [];
        this.csvDataScatterHeader = [];
        this.csvDataScatterBody = [];
      }




      list.forEach(e => {
        
        switch (typeCorrelation) {
          case 'headmap':
            this.chartArrayHeadMap.push(e.split(',')); 

            break;

          case 'snake':
            this.chartArraySnake.push(e.split(',')); 

            break;

            case 'data':
            this.chartArrayData.push(e.split(',')); 

            break;

            case 'scatter':
            this.chartArrayScatter.push(e.split(',')); 

            break;

          default:
            break;
        }
        // console.log(e);
        // get JSON with columns mapped number to string for see.

      });




      // serialize data for tables
      if (typeCorrelation === 'data') {

        this.csvDataDataBody = [];
        this.csvDataDataBody = this.chartArrayData.slice();

        this.csvDataDataHeader = [];
        this.csvDataDataHeader = this.csvDataDataBody.shift();
        this.csvDataDataBody.pop();


        this.csvDataDataBodyJustTwentyRows = [];
        for (let i = 0; i <= 20; i++) {
          this.csvDataDataBodyJustTwentyRows.push(this.csvDataDataBody[i]);
        }

        this.withoutQuotesAnomalyesDataHeaders = [];
        for (let i = 0; i <= this.csvDataDataHeader.length - 1; i++) {
          this.withoutQuotesAnomalyesDataHeaders.push(this.csvDataDataHeader[i].replace(/['"]+/g, '').replace(/\W/g, ''));
        }
      }

      if (typeCorrelation === 'headmap') {
        this.csvDataHeadMapBody = this.chartArrayHeadMap.slice();
        // this.csvDataHeadMapHeader = this.csvDataHeadMapBody.shift();
        this.csvDataHeadMapBody.pop();

        this.csvDataHeadMapBodyJustTwentyRows = [];
        for (let i = 0; i <= 20; i++) {
          this.csvDataHeadMapBodyJustTwentyRows.push(this.csvDataHeadMapBody[i]);
        }

        this.withoutQuotesAnomalyesHeadMapHeaders = [];
        for (let i = 0; i <= this.csvDataHeadMapHeader.length - 1; i++) {
          this.withoutQuotesAnomalyesHeadMapHeaders.push(this.csvDataHeadMapHeader[i].replace(/['"]+/g, '')
                                                                .replace(/\W/g, ''));
        }



      }

      if (typeCorrelation === 'snake') {

        this.csvDataSnakeBody = [];
        this.csvDataSnakeBody = this.chartArraySnake.slice();

        this.csvDataSnakeHeader = [];
        this.csvDataSnakeHeader = this.csvDataSnakeBody.shift();
        this.csvDataSnakeBody.pop();


        this.csvDataSnakeBodyJustTwentyRows = [];
        for (let i = 0; i <= 20; i++) {
          this.csvDataSnakeBodyJustTwentyRows.push(this.csvDataSnakeBody[i]);
        }

        this.withoutQuotesAnomalyesSnakeHeaders = [];
        for (let i = 0; i <= this.csvDataSnakeHeader.length - 1; i++) {
          this.withoutQuotesAnomalyesSnakeHeaders.push(this.csvDataSnakeHeader[i].replace(/['"]+/g, '').replace(/\W/g, ''));
        }

        this.viewAll();

      }



      if (typeCorrelation === 'scatter') {

        this.csvDataScatterBody = [];
        this.csvDataScatterBody = this.chartArrayScatter.slice();

        this.csvDataScatterHeader = [];
        this.csvDataScatterHeader = this.csvDataScatterBody.shift();
        this.csvDataScatterBody.pop();


        this.csvDataScatterBodyJustTwentyRows = [];
        for (let i = 0; i <= 20; i++) {
          this.csvDataScatterBodyJustTwentyRows.push(this.csvDataScatterBody[i]);
        }

        this.withoutQuotesAnomalyesScatterHeaders = [];
        for (let i = 0; i <= this.csvDataScatterHeader.length - 1; i++) {
          this.withoutQuotesAnomalyesScatterHeaders.push(this.csvDataScatterHeader[i].replace(/['"]+/g, '').replace(/\W/g, ''));
        }

        this.viewAll();

      }





    });
    this.numClusterBoxplot = 0;


  }

    // Serialises data so that they can be consumed by tables and graphs headmap
  transformRowsData(array: Array<string[]>): { name: string; data: number[]; }[] {



    const tmp: number[] = [];
    const categ: string[] = [];

    const final: { name: string; data: number[]; }[] = [];

    this.xaxis = [];
    let yaxis = [];
    const maxMin = [];



    for (let j = 1; j < array.length; j++) {
      // console.log('Cluster: ' + (j - 1));
      // tslint:disable-next-line:prefer-for-of
      for (let i = 0; i < array[j].length; i++) {
        yaxis.push(String(Math.round(parseFloat(array[j][i]) * 100) / 100));
        maxMin.push(Math.round(parseFloat(array[j][i]) * 100) / 100);
        // console.log(String(Math.round(parseFloat(array[j][i]) * 100) / 100));

      }
      final.push({name: String(j - 1), data: yaxis });
      yaxis = [];
    }




    // calcule quartile for threshold in headmap
    const data = summary(maxMin);
    const min = data.min();
    const q25 = data.quartile(0.25);
    const median = data.median();
    const q75 = data.quartile(0.75);
    const max = data.max();




    // x axis in head map
    this.rank = {
      type: 'category',
      categories: array[0],
      labels: {
        style: {
          fontSize: '14px', // Ajusta el tamaño de la fuente aquí
        }
      },
      
    };

    // launch plot
    this.plot(min, q25, median, q75, max);

    return final;
  }

     // Serialises data so that they can be consumed by tables and graphs snake
  transformRowsDataSnake(array: Array<string[]>): { name: string; data: number[]; }[] {



    const tmp: number[] = [];
    const categ: string[] = [];
    // this.rankSnake = [];
    const attributes = [];
    const cluster = [];

    const arraObject: { cluster: string; attribute: string; value: string } [] = [];

    // const final: { name: string; data: number[]; color: string; type: string}[] = []; // with type bar or line
    const final: { name: string; data: number[]; color: string}[] = [];



    // Tranform array in array object for find the key for split the data for columns for draw plot snake
    for (let j = 1; j < array.length; j++) {
      attributes.push(array[j][1]);
      cluster.push(array[j][0]);
      arraObject.push({ cluster: String(array[j][0]), attribute: String(array[j][1]), value: String(array[j][2]) });
      // console.log(array[j]);


    }

    // x axis in head map
    this.rankSnake = {
      type: 'category',
      categories: [...new Set(attributes)],
      labels: {
        style: {
          fontSize: '14px', // Ajusta el tamaño de la fuente aquí
        }
      },
      
    };

    const clusterFinal = [];
    let count = 0;
    // name of columns in plot snake
    let nameCluster = 'Cluster ';
    const clusterinArray = [...new Set(cluster)].sort();
    clusterinArray.forEach(element => {
        clusterFinal.push(String(count));
        count ++;
        clusterFinal.push(String(count));
        count ++;
        clusterFinal.push(String(count));
        count ++;
        nameCluster = nameCluster + String(element);
        final.push({name: nameCluster + ' Max', data: [], color: ''});
        final.push({name: nameCluster, data: [], color: ''});
        final.push({name: nameCluster + ' Min', data: [], color: ''});
        nameCluster = 'Cluster ';
    });


    // Fuction for group by key of object
    // tslint:disable-next-line:only-arrow-functions
    const groupBy = function(xs, key): any {
      // tslint:disable-next-line:only-arrow-functions
      return xs.reduce(function(rv, x): any {
        (rv[x[key]] = rv[x[key]] || []).push(x);
        return rv;
      }, {});
    };

    // make group by attribute
    const byAttributes = [groupBy(arraObject, 'attribute')];
    // console.log(byAttributes);


    // iterate for elements of array object
    byAttributes.forEach(element => {
      // tslint:disable-next-line:forin
      for (const key in element) {// iterate by key of object
        // console.log(`${key}: ${element[key]}`);
        // console.log(groupBy(element[key], 'cluster'));
        const t = groupBy(element[key], 'cluster'); // group by cluster
        let n: number[] = []; // array for save values pero atribute and cluster
        clusterinArray.forEach(c => {// iterate per cluster
          // cu = String(c);
          [t].forEach(ele => { // iterate por element values
            // console.log(ele[c]);
            ele[c].forEach(val => {
              n.push(Math.round(parseInt(val.value) * 100) / 100); // save in array and get just two decimals
              // console.log(val.value);
            });
          });
          const dataf = summary(n);
          // console.log(c + ' media: ' + dataf.median());
          // console.log(c + ' array: ' + n);
          if (parseInt(c, 10) === 0){
            final[parseInt(c, 10)].data.push(dataf.max()); // save array with atrributes and same cluster
            final[parseInt(c, 10) + 1].data.push(dataf.median()); // save array with atrributes and same cluster
            final[parseInt(c, 10) + 2].data.push(dataf.min()); // save array with atrributes and same cluster
            final[parseInt(c, 10)].color = this.colors[parseInt(c, 10)];
            final[parseInt(c, 10) + 1].color = this.colors[parseInt(c, 10)];
            final[parseInt(c, 10) + 2].color = this.colors[parseInt(c, 10)];
            // final[parseInt(c, 10)].type = 'line';
            // final[parseInt(c, 10) + 1].type = 'bar';
            // final[parseInt(c, 10) + 2].type = 'bar';

          }else{
            final[(parseInt(c, 10) * 3)].data.push(dataf.max()); // save array with atrributes and same cluster
            final[parseInt(c, 10) * 3 + 1].data.push(dataf.median()); // save array with atrributes and same cluster
            final[(parseInt(c, 10) * 3) + 2].data.push(dataf.min()); // save array with atrributes and same cluster
            final[parseInt(c, 10) * 3].color = this.colors[parseInt(c, 10)];
            final[(parseInt(c, 10) * 3) + 1].color = this.colors[parseInt(c, 10)];
            final[(parseInt(c, 10) * 3) + 2].color = this.colors[parseInt(c, 10)];
            // final[parseInt(c, 10) * 3].type = 'line';
            // final[(parseInt(c, 10) * 3) + 1].type = 'bar';
            // final[(parseInt(c, 10) * 3) + 2].type = 'bar';
          }

          n = []; // clean array
        });
      }

    });

    this.plotSnake();

    return final;
  }


  tableLanguage(): void {
    if (this.transLang.activeLang === 'es') {
      this.dtOptions = { pagingType: 'full_numbers', pageLength: 10, language: LanguageApp.spanishdatatables };
    } else {
      this.dtOptions = { pagingType: 'full_numbers', pageLength: 10 };
    }
  }

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

  viewAll(): void {

  
    this.chartArrayHeadMapPre = this.transformRowsData(this.csvDataHeadMapBody);
    this.chartArraySnakePre = this.transformRowsDataSnake(this.csvDataSnakeBody);
    this.transformClassPrediction(this.csvDataScatterBody, 'scatter');



    window.dispatchEvent(new Event('resize'));

  }
  saveFinish(): void{


    this.userService.compressDirectoryModelforSendToModelManager(this.currentUser, this.modelName + '.' + this.dateModelCreateModels)
      .subscribe(
        file => {
          console.log(file);
          this.showToaster(this.modelName + '.' + this.dateModelCreateModels + '  ' + this.translate.instant('ModelDeleted.text'));
          this.deleteModels();

        },
        error => {
          this.showToasterError(this.translate.instant('Error.text') + this.modelName + '.' + this.dateModelCreateModels
            + this.translate.instant('ModelDeletedError.text'));
          this.deleteModels();

        });



    this.showToaster(this.translate.instant('SaveModel.text'));

  }

  plot(min: number, q25: number, median: number, q75: number, max: number): void{

  this.chartOptions = {
      chart: {
        // height: 1050,
        width: 1000,
        type: 'heatmap',
        animations: {
          enabled: false,
          speed: 5
        },
      },
      dataLabels: {
        enabled: true
      },
      plotOptions: {
        heatmap: {
          shadeIntensity: 0.5,
          colorScale: {
            ranges: [
              {
                from: min,
                to: q25 - 0.1,
                name: 'low',
                color: '#FF0000'
              },
              {
                from: q25,
                to: median - 0.1,
                name: 'medium',
                color: '#FA8072'
              },
              {
                from: median,
                to: q75 - 0.1,
                name: 'high',
                color: '#ADFF2F'
              },
              {
                from: q75,
                to: max,
                name: 'extreme',
                color: '#32CD32'
              }
            ]
          }
        }
      },
      title: {
        text: this.translate.instant('BIRCHRelativeImportanceAttributes.Text'),
        align: 'center',
        style: {
          fontSize: '14px', // Ajusta el tamaño de la fuente aquí
        }
      },
      grid: {
        padding: {
          right: 20
        }
      },
      yaxis: {
        labels: {
          style: {
            fontSize: '14px', // Ajusta el tamaño de la fuente aquí
          }
        }
      }
    };
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
  transformClassPrediction(array: Array<string[]>, type: string): void{ 

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

    try {
      for (let j = 0; j < array[j].length - 1; j++) {

        for (let i = 0; i < array.length - 1; i += 1) {

          switch (j) {
            case 0:
              count = 0;
              // console.log('Prediction Label: ' +  array[i][2] , 'True Label: ' + array[i][3] );
              tempTL = array[i][3];//.slice(0, -1);  // replace(/\\/g, '');
              tempPC = array[i][2]; // .slice(1, -1);  // replace(/\\/g, '');
              // console.log('Prediction Label: ' +  tempPC , 'True Label: ' + tempTL);

              classC.forEach(element => {

                if (tempPC.split('_').join(' ') === element) {
                  clases[count][0].push([Number(array[i][0].substring(0, array[i][0].length - 12)),
                  Number(array[i][1].substring(0, array[i][1].length - 12))]);
                }
                if (tempTL.split('_').join(' ') === element) {
                  clases[count][1].push([Number(array[i][0].substring(0, array[i][0].length - 12)),
                  Number(array[i][1].substring(0, array[i][1].length - 12))]);
                }

                count++;

              });

              break;

          }



        }

      }



      switch (type) {
        case 'scatter':
          this.arraySeriesPCScatter = [];
          this.arraySerieTLScatter = [];
          // save in final arrays for view
          for (let index = 0; index < classC.length; index++) {
            this.arraySeriesPCScatter.push({ name: classC[index], data: clases[index][0] });
            this.arraySerieTLScatter.push({ name: classC[index], data: clases[index][1] });

          }

          // tslint:disable-next-line:prefer-for-of
          for (let index = 0; index < this.arraySeriesPCScatter.length; index++) {
            // tslint:disable-next-line: prefer-for-of
            for (let index2 = 0; index2 < this.arraySeriesPCScatter[index].data.length; index2++) {
              this.mXScatter.push(this.arraySeriesPCScatter[index].data[index2][0]);
              this.mYScatter.push(this.arraySeriesPCScatter[index].data[index2][1]);
            }
          }
          break;



        default:
          break;
      }


      // Deploy plots Scatter
      this.chargePlotScatter();
    } catch (error) {
      console.log('Array undefined');
    }


  }

plotSnake(): void{

  this.chartOptionsLine = {
      chart: {
        height: 800,
        width: 1000,
        type: 'line',
        zoom: {
          enabled: false
        },
        animations: {
          enabled: false,
          speed: 5
        },
      },
      dataLabels: {
        enabled: false
      },
      stroke: {
        curve: 'straight'
      },
      title: {
        text: this.translate.instant('BIRCHSnakeStandardizedAttributes.Text'),
        align: 'center',
        style: {
          fontSize: '14px', // Ajusta el tamaño de la fuente aquí
        }
      },
      grid: {
        row: {
          colors: ['#f3f3f3', 'transparent'], // takes an array which will be repeated on columns
          opacity: 0.5
        }
      },
      // xaxis: {
      //   labels: {
      //     style: {
      //       fontSize: '14px', // Ajusta el tamaño de la fuente aquí
      //     }
      //   },
      // },
      yaxis: {
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

  this.chartOptionsLineBar = {
      chart: {
        height: 800,
        width: 1000,
        type: 'bar',
        zoom: {
          enabled: false
        },
        animations: {
          enabled: false,
          speed: 5
        },
      },
      dataLabels: {
        enabled: false
      },
      stroke: {
        curve: 'straight'
      },
      title: {
        text: this.translate.instant('BIRCHBarStandardizedAttributes.Text'),
        align: 'center',
        style: {
          fontSize: '14px', // Ajusta el tamaño de la fuente aquí
        }
      },
      grid: {
        row: {
          colors: ['#f3f3f3', 'transparent'], // takes an array which will be repeated on columns
          opacity: 0.5
        }
      },
      legend: {
        position: "bottom",
        horizontalAlign: "center",
        fontSize: "14px"
      },
      yaxis: {
        labels: {
          style: {
            fontSize: '14px', // Ajusta el tamaño de la fuente aquí
          }
        },
      },
    };

  }

  chargePlotScatter(): void {

  this.chartScatterCluster = {
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
        min: Math.min(...this.mYScatter),
        max: Math.max(...this.mYScatter),
        labels: {
          style: {
            fontSize: '14px', // Ajusta el tamaño de la fuente aquí
          }
        },
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

  this.chartScatterTrueLabel = {
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
        min: Math.min(...this.mYScatter),
        max: Math.max(...this.mYScatter),
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

 openPopup(): void {
    this.displayStyle = 'block';
    // window.open(this.url, '_self');
  }
  closePopup(): void  {
    this.displayStyle = 'none';
  }


  /**
   * Function for view the secont graphics in mat-tab
   */
  onTabChanged($event, type): void {
    // const clickedIndex = $event.index;

    // console.log(clickedIndex);
    switch (type) {
      case 'snake':
        this.chartArrayBarRefresh = [];
        this.chartArrayBarRefresh = this.chartArraySnakePre.slice();

        break;
      case 'scatter':
        this.chartArraySkatterRefreshTrueWardScatter = [];
        this.chartArraySkatterRefreshTrueWardScatter = this.arraySerieTLScatter.slice();

        break;

        break;

      default:
        break;
    }

  }

}
