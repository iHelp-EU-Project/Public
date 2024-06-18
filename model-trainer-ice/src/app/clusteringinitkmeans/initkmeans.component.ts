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
  selector: 'app-initkmeans',
  templateUrl: './initkmeans.component.html',
  styleUrls: ['./initkmeans.component.css']
})
export class InitkmeansComponent implements AfterViewInit, OnInit {

  serverData: JSON;
  employeeData: JSON;
  file: File;
  public isLoggedIn = false;
  public userProfile: KeycloakProfile | null = null;

 @ViewChild(UploadFileComponent) update;
  currentUser: User;


  constructor(private httpClient: HttpClient,
              private fileService: FileService,
              private userService: UserService,
              private readonly keycloak: KeycloakService) {

        // this.currentUser = JSON.parse(localStorage.getItem('currentUser'));
        this.currentUser = { id: '', username: '', firstName: '', lastName: '' };

      }



  uploadedFilesTrain: Array<File>;
  uploadedFilesTest: Array<File>;

  modelName: string;

  ngAfterViewInit(): void {

  }

  // tslint:disable-next-line:typedef
  async ngOnInit() {
        // this.loadAllUsers();

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
