import { Component, OnInit, OnChanges, Renderer2, Input, Output, ViewChild, ChangeDetectionStrategy } from '@angular/core';
import { EventEmitter } from 'eventemitter3';
import { TranslateModule } from '@ngx-translate/core';
import { TranslateService } from '@ngx-translate/core';
import { LanguageApp } from '../services/LanguageApp';
import { TranslationService } from '../services/translation.service';


@Component({
  selector: 'app-progressbar',
  templateUrl: './progressbar.component.html',
  styleUrls: ['./progressbar.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ProgressbarComponent implements OnChanges {

  @Input() value: number;
  @Input() state: string;
  stateTranslation = '';
  // tslint:disable-next-line:no-output-native
  @Output() complete = new EventEmitter();
  @ViewChild('progress', {static: true}) progress;

  constructor(private rederer: Renderer2,
              private translate: TranslateService,
              private transLang: TranslationService) { }

  ngOnChanges(): void {
    this.setWidth(`${this.value}%`);
    this.translationStatePythonScripts();
    if (this.value === 100 ){
      this.emitComplete();
    }
  }

  setWidth = (width: string) => this.rederer.setStyle(this.progress.nativeElement, 'width', width);
  emitComplete = () => setTimeout(() => this.complete.emit, 300);


  translationStatePythonScripts(): void {

    if (this.transLang.activeLang !== 'en'){

      switch (this.state) {
        // Columns ID start
        case 'Awaiting data, please be patient':
          this.stateTranslation = this.translate.instant('WaitingPythonScripts.text');
          break;
        case 'Ending':
          this.stateTranslation = this.translate.instant('EndingPythonScripts.text');
          break;
        case 'Starting':
          this.stateTranslation = this.translate.instant('StartingPythonScripts.text');
          break;
        case 'Building training data':
          this.stateTranslation = this.translate.instant('BuildingtraindataPythonScripts.text');
          break;
        case 'Building testing data':
          this.stateTranslation = this.translate.instant('BuildingtestdataPythonScripts.text');
          break;
        case 'Building the graphics':
          this.stateTranslation = this.translate.instant('BuildingGraphicsPythonScripts.text');
          break;
        case 'Saving the graphics':
          this.stateTranslation = this.translate.instant('SavingGraphicsPythonScripts.text');
          break;
        case 'Building correlations':
          this.stateTranslation = this.translate.instant('BuildingCorrelationsPythonScripts.text');
          break;
        case 'Printing coorelation analysis':
          this.stateTranslation = this.translate.instant('PrintingCoorelationAnalysisPythonScripts.text');
          break;
        case 'Saving coorelations':
          this.stateTranslation = this.translate.instant('SavingCoorelationsPythonScripts.text');
          break;
        case 'Buiding object to create model':
          this.stateTranslation = this.translate.instant('BuidingOjectCreateModelPythonScripts.text');
          break;
        case 'Buiding model':
          this.stateTranslation = this.translate.instant('BuidingModelPythonScripts.text');
          break;
        case 'Buiding reconstruction error (MSE)':
          this.stateTranslation = this.translate.instant('BuidingMSEPythonScripts.text');
          break;
        case 'Buiding models anomalies':
          this.stateTranslation = this.translate.instant('BuidingModelsAnomaliesPythonScripts.text');
          break;
        case 'Recovering model':
          this.stateTranslation = this.translate.instant('RecoveringModelPythonScripts.text');
          break;
        case 'Buiding predictions':
          this.stateTranslation = this.translate.instant('BuidingPredictionsPythonScripts.text');
          break;
        case 'Ordering model, predictions and anomalies':
          this.stateTranslation = this.translate.instant('OrderingModelPredictionsAnomaliesPythonScripts.text');
          break;
        case 'Sorting coorelations':
          this.stateTranslation = this.translate.instant('SortingcoorelationsPythonScripts.text');
          break;
        case 'Building dataset':
          this.stateTranslation = this.translate.instant('BuildingDatasetPythonScripts.text');
          break;
        case 'Building data chooseen users':
          this.stateTranslation = this.translate.instant('BuildingDataChooseenUsersPythonScripts.text');
          break;
        case 'Building data regression':
          this.stateTranslation = this.translate.instant('BuildingDataRegressionPythonScripts.text');
          break;
        case 'Draw plots':
          this.stateTranslation = this.translate.instant('DrawPlotsPythonScripts.text');
          break;
        case 'Error in data input':
          this.stateTranslation = this.translate.instant('ErrorDataInputPythonScripts.text');
          break;
        case 'Error in algorithm':
          this.stateTranslation = this.translate.instant('ErrorAlgorithmPythonScripts.text');
          break;

        case 'Clean data':
          this.stateTranslation = this.translate.instant('CleanDataPythonScripts.Text');
          break;
        case 'Generate Shap Plots & Data':
          this.stateTranslation = this.translate.instant('GenerateShapPlotsDataPythonScripts.Text');
          break;
        case 'Building K-MEANS Clustering SSE: Elbow Chart':
          this.stateTranslation = this.translate.instant('BuildingKMEANSAlgorithmPythonScripts.Text');
          break;
        case 'Building Number of Clusters vs. Silhouette Score':
          this.stateTranslation = this.translate.instant('ClustersSilhouettePythonScripts.Text');
          break;
        case 'Building model Birch':
          this.stateTranslation = this.translate.instant('BuildingModelBirchPythonScripts.Text');
          break;
        case 'Building Relative Importance of Attributes':
          this.stateTranslation = this.translate.instant('BuildingRelativeImportanceAttributesPythonScripts.Text');
          break;
        case 'Building Relative Importance Heatmap':
          this.stateTranslation = this.translate.instant('BuildingRelativeImportanceHeatmapPythonScripts.Text');
          break;
        case 'Save model & JSON file':
          this.stateTranslation = this.translate.instant('SaveModelJSONPythonScripts.Text');
          break;
        case 'Completing final operations':
          this.stateTranslation = this.translate.instant('CompletingFinalOperationsPythonScripts.Text');
          break;
        case 'Building model Single':
          this.stateTranslation = this.translate.instant('BuildingModelSingle.Text');
          break;
        case 'Building model Complete':
          this.stateTranslation = this.translate.instant('BuildingModelComplete.Text');
          break;
        case 'Building model Ward':
          this.stateTranslation = this.translate.instant('BuildingModelWard.Text');
          break;
        case 'Building model Average':
          this.stateTranslation = this.translate.instant('BuildingModelAverage.Text');
          break;
        // case 'Error in algorithm':
        //   this.stateTranslation = this.translate.instant('ErrorAlgorithmPythonScripts.Text');
        //   break;
        // case 'Error in algorithm':
        //   this.stateTranslation = this.translate.instant('ErrorAlgorithmPythonScripts.Text');
        //   break;
        // case 'Error in algorithm':
        //   this.stateTranslation = this.translate.instant('ErrorAlgorithmPythonScripts.Text');
        //   break;
        // case 'Error in algorithm':
        //   this.stateTranslation = this.translate.instant('ErrorAlgorithmPythonScripts.Text');
        //   break;
        default:
          break;
      }


    }else {
      this.stateTranslation = this.state;
    }


  }

}
