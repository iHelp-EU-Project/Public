import { Component, OnInit, Input, ViewChild } from '@angular/core';
// import { UploadFileComponent } from '../../uploadfile/uploadfile.component';
import { HttpClient } from '@angular/common/http';
// import { Observable } from 'rxjs';
import { ToastrService } from 'ngx-toastr';
import { UserService } from '../../../../../../services/index_services';
import { User } from '../../../../../../models/index_models';
import { TranslateModule } from '@ngx-translate/core';
import { TranslateService } from '@ngx-translate/core';
import { LanguageApp } from '../../../../../../services/LanguageApp';
import { TranslationService } from '../../../../../../services/translation.service';
import { TimeLoadingService } from '../../../../../../services/timeloading.service';



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

export type ChartOptions = {
  series: ApexAxisChartSeries;
  chart: ApexChart;
  xaxis: ApexXAxis;
  yaxis: ApexYAxis;
  title: ApexTitleSubtitle;
};

@Component({
  selector: 'app-import-datasets-kmeans-end',
  templateUrl: './import-datasets-kmeans-end.component.html',
  styleUrls: ['./import-datasets-kmeans-end.component.css']
})
export class ImportDatasetsKmeansEndComponent implements OnInit {

  @Input() modelName: string;
  @Input() uploadedFilesTrain: Array<File>;
  @Input() selectedIdsRowsSecondworkflow: string;
  @Input() selIds: string;
  @Input() selectedIdsRows: string;
  @Input() selectedIdsRowsSecondworkflowEnd: string;
  @Input() choseRegresionColumns: string;
  @Input() buttonCorreWorking: boolean;
  @Input() dateModelCreateModels: string;

  @ViewChild('chartRegression2') chart: ChartComponent;
  public chartOptions: Partial<ChartOptions>;



  @ViewChild('chartRegression') chartTrain: ChartComponent;
  public chartOptionsRegression: Partial<ChartOptionsBoxplot>;
  public chartArrayRegressionGraphics;
  public chartArrayRegressionGraphics2;

  dtOptions: DataTables.Settings = {pagingType: 'full_numbers', pageLength: 10};

  host: any;


  informationWorkflow: string;
  gifCorr = false;
  correlationTrain: Array<string[]> = [];
  headersTemps: Array<string> = [];

  columnsId = ' ';
  sendColumnsIDSecondWorkflow = '';

  sendColumnsIDSecondWorkflowEnd = '';

  checktable = false;

  csvDataRegresion;

  currentUser: User;
  corrView = false;
  anomaly = false;
  nextNeu = false;

  public chartArrayTrain;
  csvDataTrainHeader;
  csvDataTrainBody;
  csvDataTrainBodyJustTwentyRows;
  csvDataTrain: Array<string[]> = [];
  public withoutQuotesAnomalyesTrainHeaders;


  blobbox = false;

  numTrain: number;

  // loadNumberProgress: {progress: number, state: string};
  // interval;

  constructor(private http: HttpClient,
              private toastr: ToastrService,
              private userService: UserService,
              private translate: TranslateService,
              private transLang: TranslationService,
              public loading: TimeLoadingService)
              {
    this.currentUser = JSON.parse(localStorage.getItem('currentUser'));
    this.host = self.location.host.split(':')[0];


    this.chartOptionsRegression = {
    chart: {
      height: 600,
      width: 1200,
      type: 'boxPlot',
      animations: {enabled: false}
    },
    colors: ['#008FFB', '#FEB019'],
    title: {
      text: this.translate.instant('Clustering.text'),
      align: 'center'
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

    this.chartOptions = {
      chart: {
        type: 'candlestick',
        height: 350,
      animations: {enabled: false}
      },
      title: {
        text: this.translate.instant('ClusteringChart.text'),
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
  ngOnInit(): void {

      this.csvDataRegresion = [this.modelName + '.' + this.dateModelCreateModels, this.modelName + '.' + this.dateModelCreateModels + '_data_regresion.csv'];

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

   runningScriptsMLKmeansEnd(): void {
      this.tableLanguage();
      this.loading.timeLoading();
      this.gifCorr = true;
      this.corrView = true;
      // this.selectedIdsRowsSecondworkflowEnd = this.saveColumnsIDstring(this.selectedIdsRowsSecondworkflowEnd);
      try {
          // tslint:disable-next-line: max-line-length
          this.http.get(`http://${this.host}:5002/importDatasetsKmeans_End/${this.modelName}/${this.uploadedFilesTrain[0].name}/${this.selIds}/${this.selectedIdsRows}/${this.selectedIdsRowsSecondworkflow}/${this.selectedIdsRowsSecondworkflowEnd}/${this.currentUser.username}`).subscribe(data => {
          this.informationWorkflow = data as string;
          this.getData('regression', this.csvDataRegresion);
          this.processLineByLineTrain();
          this.gifCorr = false;
          this.nextNeu = true;
        });
      }catch (error){
          this.gifCorr = false;
          this.nextNeu = true;
      }

  }



   processLineByLineTrain(): void {
    let numTrain = 0;
    const filesModelTrain: string[] = [this.uploadedFilesTrain[0].name];
    const expression = /\s* \s*/;
    const arrlines = this.informationWorkflow.split(/\r?\n/);
    for (let i = 0, strLen = arrlines.length; i < strLen; i++) {
      if (arrlines[i].includes('Export File progress:')) {
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

    this.headersTemps  = this.csvDataTrainHeader;
    for ( let i = 0 ; i < this.headersTemps.length ; i++){

      this.csvDataTrainHeader[i] = this.headersTemps[i].replace(/\W/g, '');

    }
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

  getData(typeCorrelation: string, url: string[]): any {

    // for (const url of this.urls){

      // this.getInfo(url).subscribe(data => {

      this.userService.downloadJSONFiles(this.currentUser, url[1], url[0] ).subscribe(data => {
        const list = data.split('\n');
        if (typeCorrelation === 'regression') {
          this.chartArrayTrain = [];
          this.csvDataTrainHeader = [];
          this.csvDataTrainBody = [];
        }

        list.forEach(e => {
          // for (let i = 1; i < list.length ; i++) {}
            switch (typeCorrelation) {
              case 'regression':
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


              default:
                break;
            }
          // console.log(e);
        });

          // seeialize data for tables
        if (typeCorrelation === 'regression') {
          this.csvDataTrainBody = this.csvDataTrain.slice();
          this.csvDataTrainHeader = this.csvDataTrainBody.shift();
          this.csvDataTrainBody.pop();
          this.csvDataTrainBody.pop();

          this.csvDataTrainBodyJustTwentyRows = [];
          for (let i = 0; i <= 20; i ++){
            this.csvDataTrainBodyJustTwentyRows.push( this.csvDataTrainBody[i]);
          }

          this.withoutQuotesAnomalyesTrainHeaders = [];
          for (let i = 0; i <= this.csvDataTrainHeader.length - 1; i++) {
            this.withoutQuotesAnomalyesTrainHeaders.push(this.csvDataTrainHeader[i].replace(/['"]+/g, '').replace(/\W/g, ''));
          }

          this.viewAll();

        }

      });
    // }
      // console.log(this.urls, 'Harcodeado');
      console.log(this.chartArrayTrain, 'Final Regression');

      this.numTrain = 0;


  }

transformRowsDataAnomalies(array: Array<string[]>, s: string): void {



    if (s === 'regression') {
      this.chartArrayRegressionGraphics = [];
      // this.chartArrayRegressionGraphics2 = [];
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

    if (s === 'regression') {
      this.chartArrayRegressionGraphics.push({
        type: 'boxPlot',
        data: final
      });
      // this.chartArrayRegressionGraphics2.push({
      //   name: 'candle',
      //   data: final
      // });

    }




  }

  viewAll(): void {
    this.transformRowsDataAnomalies(this.csvDataTrain, 'regression');
    // this.initChartsLinear();
    this.blobbox = true;

  }

  tableLanguage(): void{
      if (this.transLang.activeLang === 'es'){
      this.dtOptions = { pagingType: 'full_numbers', pageLength: 10, language: LanguageApp.spanishdatatables };
    }else{
      this.dtOptions = { pagingType: 'full_numbers', pageLength: 10};
    }
  }


}
