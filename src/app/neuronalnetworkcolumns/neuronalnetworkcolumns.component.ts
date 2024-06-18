import { Component, OnInit , ViewChild, AfterViewInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FileService } from '../services/file.service';
import { Observable } from 'rxjs';
import { UploadFileComponent } from '../neuronalnetworkcolumns/uploadfile/uploadfile.component';
import { UserService } from '../services/index_services';
import {BackendscriptsService} from '../services/backendscripts.service';
import { User } from '../models/index_models';
import { KeycloakService } from 'keycloak-angular';
import { KeycloakProfile } from 'keycloak-js';



@Component({
  selector: 'app-neuronalnetworkcolumns',
  templateUrl: './neuronalnetworkcolumns.component.html',
  styleUrls: ['./neuronalnetworkcolumns.component.css']
})
export class NeuronalnetworkcolumnsComponent implements AfterViewInit, OnInit {

  serverData: JSON;
  employeeData: JSON;
  file: File;

  @ViewChild(UploadFileComponent) update;
  currentUser: User;
  host: any;
  uploadedFilesTrain: Array<File>;
  uploadedFilesTest: Array<File>;

  modelName: string;
  public isLoggedIn = false;
  public userProfile: KeycloakProfile | null = null;

  constructor(private httpClient: HttpClient,
              private fileService: FileService,
              private userService: UserService,
              private backEndPython: BackendscriptsService,
              private readonly keycloak: KeycloakService) {

    // this.currentUser = JSON.parse(localStorage.getItem('currentUser'));
    this.currentUser = { id: '', username: '', firstName: '', lastName: '' };
    this.host = self.location.host.split(':')[0];

  }

  // tslint:disable-next-line:typedef
  async ngOnInit() {

    this.isLoggedIn = await this.keycloak.isLoggedIn();

    if (this.isLoggedIn) {
      this.userProfile = await this.keycloak.loadUserProfile();
      this.currentUser.id = this.userProfile.id;
      this.currentUser.username = this.userProfile.username;
      this.currentUser.firstName = this.userProfile.firstName;
      this.currentUser.lastName = this.userProfile.lastName;

    }


  }



  ngAfterViewInit(): void {

  }



  runningScriptsML(): void {
    this.backEndPython.loginAutorization(this.currentUser, this.isLoggedIn.toString());
    setTimeout(() => {

      this.uploadedFilesTrain = this.update.uploadedFilesTrain;
      this.uploadedFilesTest = this.update.uploadedFilesTest;
      this.modelName = this.update.modelName;
      // tslint:disable-next-line: max-line-length
      this.httpClient.get(`/scripts/scriptsml/${this.modelName}/${this.uploadedFilesTrain[0].name}/${this.uploadedFilesTest[0].name}/${this.currentUser.username}`).subscribe(data => {
        this.employeeData = data as JSON;
        console.log(this.employeeData);
      });
    }, 1500);

  }
}
