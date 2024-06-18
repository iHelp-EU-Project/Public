import { DatePipe } from '@angular/common';
import { HttpClient, HttpEventType, HttpResponse } from '@angular/common/http';
import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { KeycloakService } from 'keycloak-angular';
import { KeycloakProfile } from 'keycloak-js';
import { ToastrService } from 'ngx-toastr';
import { Observable } from 'rxjs/internal/Observable';
import { User } from 'src/app/models/user';
import { AlertService } from 'src/app/services/alert.service';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { BackendscriptsService } from 'src/app/services/backendscripts.service';
import { TimeLoadingService } from 'src/app/services/timeloading.service';
import { TranslationService } from 'src/app/services/translation.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-gan-model',
  templateUrl: './gan-model.component.html',
  styleUrls: ['./gan-model.component.css']
})
export class GanModelComponent implements OnInit {

  @Input() modelName: string;
  @Input() dateModelCreateModels: string;
  @Input() classes: { id: string, files: string }[];
  @Input() layerConvolution: number;
  @Input() layer: number;

  fileInfos?: Observable<any>;
  currentUser: User;
  host: any;
  imagesViewMenu: boolean;
  endRadioCheck: boolean;
  selectedFiles: FileList;
  currentFile?: File;
  progress = 0;
  message = '';
  modelnameDate = '';
  // modelName: string = null;
  pipeDate: string = null;
  public isLoggedIn = false;
  public userProfile: KeycloakProfile | null = null;
  receivedParameters: string;
  nameModelSaved = '';
  dateModelSaved = '';
  workflow = true;
  mymodels = false;


  constructor(private http: HttpClient,
    private toastr: ToastrService,
    private userService: UserService,
    private backEndPython: BackendscriptsService,
    private translate: TranslateService,
    private authenticationService: AuthenticationService,
    private datePipe: DatePipe,
    private alertService: AlertService,
    private transLang: TranslationService,
    private route: ActivatedRoute,
    public loading: TimeLoadingService,
    private fb: FormBuilder,
    private readonly keycloak: KeycloakService) {

// this.currentUser = JSON.parse(localStorage.getItem('currentUser'));
this.currentUser = { id: '', username: '', firstName: '', lastName: '' };

this.host = self.location.host.split(':')[0];



}

async ngOnInit() {
    this.isLoggedIn = await this.keycloak.isLoggedIn();

    if (this.isLoggedIn) {
      this.userProfile = await this.keycloak.loadUserProfile();
      this.currentUser.id = this.userProfile.id;
      this.currentUser.username = this.userProfile.username;
      this.currentUser.firstName = this.userProfile.firstName;
      this.currentUser.lastName = this.userProfile.lastName;

    }

    this.route.params.subscribe(params =>
      // tslint:disable-next-line:no-string-literal
      this.receivedParameters = params['namemodels']);

    if (typeof this.receivedParameters !== 'undefined') {
      this.nameModelSaved = this.receivedParameters.split('.')[0];
      this.dateModelSaved = this.receivedParameters.split('.')[1];
      this.mymodels = true;
      this.workflow = false;
      console.log(this.receivedParameters)
    }

  }


  uploadImagesNormalTrain(): void{


    this.imagesViewMenu = true;
    this.endRadioCheck = true;
    let formData = new FormData();



    Array.from(this.selectedFiles).forEach(file => {
      this.progress = 0;
      formData = new FormData();
      this.currentFile = file; // this.selectedFiles.item(0);
      formData.append('file', file);

      // this.userService.updateFiletoUploads(formData);

      this.userService.uploadFilesGAN(this.currentUser, this.modelnameDate, 'train/normal/', this.currentFile).subscribe(
        event => {
          if (event.type === HttpEventType.UploadProgress) {
            this.progress = Math.round(100 * event.loaded / event.total);
          } else if (event instanceof HttpResponse) {
            this.message = event.body.message;
            // download name files for view in frond end
            this.fileInfos = new Observable<any>();
            this.fileInfos = this.userService.getFiles(this.currentUser,  this.modelName + '.' + this.pipeDate, 'train/normal/');
          }
        },
        err => {
          this.progress = 0;
          this.message = 'Could not upload the file!';
          this.currentFile = undefined;
        });
      this.selectedFiles = undefined;

    });


    // Reset the "progress" property to 0
    this.progress = 0;


  }

  deleteModels(): void{


    this.userService.deleteModels(this.currentUser, this.nameModelSaved + '.' + this.dateModelSaved)
      .subscribe(
        file => {
          console.log(file);
          this.showToaster(this.nameModelSaved + '.' + this.dateModelSaved + '  ' + this.translate.instant('ModelDeleted.text'));

        },
        error => {
          this.showToasterError(this.translate.instant('Error.text') + this.nameModelSaved + '.' + this.dateModelSaved
           + this.translate.instant('ModelDeletedError.text'));
        });



  }

    // Information OK
    showToaster(message: string): any {
      this.toastr.success(message);
    }
    // Information no OK
    showToasterError(message: string): any {
      this.toastr.error(message);
    }

}
