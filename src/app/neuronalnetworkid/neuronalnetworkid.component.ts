import { Component, OnInit , ViewChild, AfterViewInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FileService } from '../services/file.service';
import { Observable } from 'rxjs';
import { UploadFileComponent } from '../neuronalnetworkcolumns/uploadfile/uploadfile.component';
import { UserService } from '../services/index_services';
import { User } from '../models/index_models';
import { KeycloakService } from 'keycloak-angular';
import { KeycloakProfile } from 'keycloak-js';

@Component({
  selector: 'app-neuronalnetworkid',
  templateUrl: './neuronalnetworkid.component.html',
  styleUrls: ['./neuronalnetworkid.component.css']
})
export class NeuronalnetworkidComponent implements  OnInit {

  serverData: JSON;
  employeeData: JSON;
  file: File;
  uploadedFilesTrain: Array<File>;
  uploadedFilesTest: Array<File>;

  modelName: string;
 @ViewChild(UploadFileComponent) update;
  currentUser: User;
  public isLoggedIn = false;
  public userProfile: KeycloakProfile | null = null;

  constructor(private readonly keycloak: KeycloakService) {

        // this.currentUser = JSON.parse(localStorage.getItem('currentUser'));
        this.currentUser = { id: '', username: '', firstName: '', lastName: '' };
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





}
