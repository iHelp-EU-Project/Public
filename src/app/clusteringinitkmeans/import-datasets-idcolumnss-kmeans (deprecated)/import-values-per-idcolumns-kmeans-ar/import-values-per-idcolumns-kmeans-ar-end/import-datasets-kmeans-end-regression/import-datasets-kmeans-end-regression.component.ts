import { Component, OnInit, Input, ViewChild } from '@angular/core';
// import { UploadFileComponent } from '../../uploadfile/uploadfile.component';
import { HttpClient } from '@angular/common/http';
// import { Observable } from 'rxjs';
import { ToastrService } from 'ngx-toastr';
import { UserService } from '../../../../../services/index_services';
import { User } from '../../../../../models/index_models';

@Component({
  selector: 'app-import-datasets-kmeans-end-regression',
  templateUrl: './import-datasets-kmeans-end-regression.component.html',
  styleUrls: ['./import-datasets-kmeans-end-regression.component.css']
})
export class ImportDatasetsKmeansEndRegressionComponent implements OnInit {

  @Input() modelName: string;
  @Input() uploadedFilesTrain: Array<File>;
  @Input() selectedIdsRowsSecondworkflow: string;
  @Input() selIds: string;
  @Input() selectedIdsRows: string;
  @Input() selectedIdsRowsSecondworkflowEnd: string;
  @Input() buttonCorreWorking: boolean;
  @Input() dateModelCreateModels: string;


  informationWorkflow: string;
  gifCorr = false;
  correlationTrain: Array<string[]> = [];
  headersTemps: Array<string> = [];

  columnsId = ' ';
  sendColumnsIDSecondWorkflow = '';

  sendColumnsIDSecondWorkflowEnd = '';

  choseRegresionColumns = [];

  checktable = false;


  currentUser: User;
  corrView = false;
  anomaly = false;
  nextNeu = false;

  csvDataTrainHeader;
  csvDataTrainBody;
  csvDataTrain: Array<string[]> = [];

  blobbox = false;

  constructor(private http: HttpClient, private toastr: ToastrService, private userService: UserService) {
    this.currentUser = JSON.parse(localStorage.getItem('currentUser'));
 }
  ngOnInit(): void {
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

   runningScriptsMLKmeansID_Regression(): void {
      this.gifCorr = true;
      this.corrView = true;
      this.selectedIdsRowsSecondworkflowEnd = this.saveColumnsIDstring(this.selectedIdsRowsSecondworkflowEnd);
      // tslint:disable-next-line: max-line-length
      this.http.get(`http://127.0.0.1:5002/importDatasetsKmeans_End_Regression/${this.modelName}/${this.uploadedFilesTrain[0].name}/${this.selIds}/${this.selectedIdsRows}/${this.selectedIdsRowsSecondworkflow}/${this.selectedIdsRowsSecondworkflowEnd}/${this.currentUser.username}`).subscribe(data => {
      this.informationWorkflow = data as string;
      this.processLineByLineTrain();
      this.gifCorr = false;
      this.nextNeu = true;
    });

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

  OnCheckboxSelectKmeansRegression(id, event): void {


      if (event.target.checked === true) {
        // tslint:disable-next-line: object-literal-shorthand
        this.choseRegresionColumns.push({ id: id, checked: event.target.checked });
      }
      if (event.target.checked === false) {
        this.choseRegresionColumns = this.choseRegresionColumns.filter((item) => item.id !== id);
      }

      if (this.choseRegresionColumns.length < 2){
        this.checktable = false;
      } else{
        this.checktable = true;
      }

      if (this.choseRegresionColumns.length > 2){
          this.choseRegresionColumns.shift();
      }

      let columms = '';
      for (const j of this.choseRegresionColumns) {
        columms += ',' + j.id;
        console.log(columms);
      }

      const ids = columms.split(',');
      console.log(ids);
      console.log(ids[0]);
      console.log(ids[1]);
      console.log(ids[2]);

      this.csvDataTrainHeader.forEach(it => {
        if (it !== ids[1]) {
          // tslint:disable-next-line:no-string-literal
          document.getElementById(it + '_REGRESSION')['checked'] = false;
        }else{
          // it.id = true;
          // tslint:disable-next-line:no-string-literal
          document.getElementById(it + '_REGRESSION')['checked'] = true;
        }
        if (it === ids[2]){
          // tslint:disable-next-line:no-string-literal
          document.getElementById(it + '_REGRESSION')['checked'] = true;
        }
      });
  }



}
