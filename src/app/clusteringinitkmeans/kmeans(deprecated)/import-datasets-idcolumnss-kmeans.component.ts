import { Component, OnInit, Input, ViewChild } from '@angular/core';
// import { UploadFileComponent } from '../../uploadfile/uploadfile.component';
import { HttpClient } from '@angular/common/http';
// import { Observable } from 'rxjs';
import { ToastrService } from 'ngx-toastr';
import { UserService } from '../../services/index_services';
import { User } from '../../models/index_models';
import { TranslateModule } from '@ngx-translate/core';
import { TranslateService } from '@ngx-translate/core';
import { LanguageApp } from '../../services/LanguageApp';
import { TranslationService } from '../../services/translation.service';
import { TimeLoadingService } from '../../services/timeloading.service';


@Component({
  selector: 'app-import-datasets-idcolumnss-kmeans',
  templateUrl: './import-datasets-idcolumnss-kmeans.component.html',
  styleUrls: ['./import-datasets-idcolumnss-kmeans.component.css']
})
export class ImportDatasetsIDColumnssKmeansComponent implements OnInit {

  @Input() modelName: string;
  @Input() uploadedFilesTrain: Array<File>;
  @Input() uploadedFilesTest: Array<File>;
  @Input() selectsIds: any;
  @Input() buttonCorreWorking: boolean;
  @Input() dateModelCreateModels: string;

  dtOptions: DataTables.Settings = {pagingType: 'full_numbers', pageLength: 10};

  informationWorkflow: string;
  gifCorr = false;
  correlationTrain: Array<string[]> = [];
  correlationTest: Array<string[]> = [];
  headersTemps: Array<string> = [];

  columnsId = ' ';
  sendColumnsID = '';

  selectedIdsRows = [];

  checktable = false;


  currentUser: User;
  corrView = false;
  anomaly = false;
  nextNeu = false;

  csvDataTrainHeader;
  csvDataTrainBody;
  csvDataTrain: Array<string[]> = [];
  host: any;

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


  runningScriptsMLCorrID(): void {
    this.tableLanguage();
    this.loading.timeLoading();
    this.gifCorr = true;
    this.corrView = true;
    this.sendColumnsID = this.saveColumnsIDstring(this.selectsIds);
    try {
      // tslint:disable-next-line: max-line-length
      this.http.get(`http://${this.host}:5002/ScriptsimportDatasetsIDColumnssKmeans/${this.modelName}/${this.uploadedFilesTrain[0].name}/Tabber-Stringer/${this.sendColumnsID}/${this.currentUser.username}`).subscribe(data => {
        this.informationWorkflow = data as string;
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
    // this.csvDataTrainBody.shift();
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

  OnCheckboxSelect(id, event): void {

      this.selectedIdsRows = [];

      if (event.target.checked === true) {
        // tslint:disable-next-line: object-literal-shorthand
        this.selectedIdsRows.push({ id: id, checked: event.target.checked });
      }
      if (event.target.checked === false) {
        this.selectedIdsRows = this.selectedIdsRows.filter((item) => item.id !== id);
      }

      if (this.selectedIdsRows.length < 1){
        this.checktable = false;
      } else{
        this.checktable = true;
      }

      this.csvDataTrainBody.forEach(it => {
            console.log(it[0]);
            if (it[0] !== id){
              try {
                // tslint:disable-next-line:no-string-literal
                document.getElementById(it[0])['checked'] = false;
              } catch (error) {
                console.log('Not yet generated in the WEB UI, ignore');
              }
            }else{
              // it.id = true;
              // tslint:disable-next-line:no-string-literal
              document.getElementById(it[0])['checked'] = true;
            }


    });
  }

  tableLanguage(): void{
      if (this.transLang.activeLang === 'es'){
      this.dtOptions = { pagingType: 'full_numbers', pageLength: 10, language: LanguageApp.spanishdatatables };
    }else{
      this.dtOptions = { pagingType: 'full_numbers', pageLength: 10};
    }
  }

}
