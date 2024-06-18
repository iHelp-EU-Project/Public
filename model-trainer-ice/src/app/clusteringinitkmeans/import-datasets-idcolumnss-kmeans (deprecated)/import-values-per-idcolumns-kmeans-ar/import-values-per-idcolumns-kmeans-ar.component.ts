import { Component, OnInit, Input, ViewChild } from '@angular/core';
// import { UploadFileComponent } from '../../uploadfile/uploadfile.component';
import { HttpClient } from '@angular/common/http';
// import { Observable } from 'rxjs';
import { ToastrService } from 'ngx-toastr';
import { UserService } from '../../../services/index_services';
import { User } from '../../../models/index_models';


@Component({
  selector: 'app-import-values-per-idcolumns-kmeans-ar',
  templateUrl: './import-values-per-idcolumns-kmeans-ar.component.html',
  styleUrls: ['./import-values-per-idcolumns-kmeans-ar.component.css']
})
export class ImportValuesPerIDColumnsKmeansARComponent implements OnInit {

  @Input() modelName: string;
  @Input() uploadedFilesTrain: Array<File>;
  @Input() uploadedFilesTest: Array<File>;
  @Input() selIds: string;
  @Input() selectedIdsRows: string;
  @Input() buttonCorreWorking: boolean;
  @Input() dateModelCreateModels: string;

  selectedIdsRowsSecondworkflow = [];

  informationWorkflow: string;
  gifCorr = false;
  correlationTrain: Array<string[]> = [];
  headersTemps: Array<string> = [];

  columnsId = ' ';
  sendColumnsIDSecondWorkflow = '';


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

   runningScriptsMLKmeansID(): void {
      this.gifCorr = true;
      this.corrView = true;
      this.selectedIdsRows = this.saveColumnsIDstring(this.selectedIdsRows);
      // tslint:disable-next-line: max-line-length
      this.http.get(`http://127.0.0.1:5002/importValuesPerIDColumnsKmeansAR/${this.modelName}/${this.uploadedFilesTrain[0].name}/Tabber-Stringer/${this.selIds}/${this.selectedIdsRows}/${this.currentUser.username}`).subscribe(data => {
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
      if (arrlines[i].includes('END-TESTING')) {
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
    this.csvDataTrainBody.shift();
    this.csvDataTrainBody.pop();

    const temp = this.csvDataTrainBody;
    this.csvDataTrainBody = [];
    temp.forEach(element => {

      this.csvDataTrainBody.push(element.filter(Boolean));
    });

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

  OnCheckboxSelectKmeans(id, event): void {


      this.selectedIdsRowsSecondworkflow = [];
      if (event.target.checked === true) {
        // tslint:disable-next-line: object-literal-shorthand
        this.selectedIdsRowsSecondworkflow.push({ id: id, checked: event.target.checked });
      }
      if (event.target.checked === false) {
        this.selectedIdsRowsSecondworkflow = this.selectedIdsRowsSecondworkflow.filter((item) => item.id !== id);
      }

      if (this.selectedIdsRowsSecondworkflow.length < 1){
        this.checktable = false;
      } else{
        this.checktable = true;
      }

      this.csvDataTrainHeader.forEach(it => {
        if ((it + '_AR') !== (id + '_AR')){
          // tslint:disable-next-line:no-string-literal
          document.getElementById(it + '_AR')['checked'] = false;
        }else{
          // it.id = true;
          // tslint:disable-next-line:no-string-literal
          document.getElementById(it + '_AR')['checked'] = true;
        }
      });
  }


}
