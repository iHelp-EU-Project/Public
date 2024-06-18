import { Component, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { LanguageApp } from '../services/LanguageApp';
import { TranslationService } from '../services/translation.service';
import {FormBuilder, FormGroup} from '@angular/forms';
import { User } from '../models/index_models';
import { UserService } from '../services/index_services';
import { ToastrService } from 'ngx-toastr';
import { KeycloakService } from 'keycloak-angular';
import { KeycloakProfile } from 'keycloak-js';






@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.css']
})
export class SettingsComponent implements OnInit {

  favoriteLanguage = 'es';
  languages: string[] = ['es', 'en'];
  currentUser: User;
  settings: {language: string};
  checkerSpanish = false;
  checkerEnglish = true;
  radioOptions = 'es';
  public isLoggedIn = false;
  public userProfile: KeycloakProfile | null = null;

  constructor(private translate: TranslateService,
              private transLang: TranslationService,
              private userService: UserService,
              private toastr: ToastrService,
              private readonly keycloak: KeycloakService) {

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

        this.loadLenguageFavouriteJSONFileSettings();

  }

 changeLanguageInSettings(str: string): void{
    this.transLang.changeLanguage(str);
    this.writeLenguageFavouriteJSONFileSettings(str);

  }

  // Infortation OK
  showToaster(message: string): any {
    this.toastr.success(message);
  }
  // Information no Ok
  showToasterError(message: string): any {
    this.toastr.error(message);
  }

  loadLenguageFavouriteJSONFileSettings(): void{
    this.userService.readSettingsJSONFiles(this.currentUser, 'settings.json', 'settings/' + this.currentUser.username)
              .subscribe(
                data => {
                  this.settings = JSON.parse(data);
                  console.log(this.settings);
                  // set success message and pass true paramater to persist the message after redirecting to the login page
                  this.showToaster(this.translate.instant('ChargeSettings.text')  + this.translate.instant('Successful.text'));
                  this.favoriteLanguage = this.settings.language;
                  this.transLang.changeLanguage(this.favoriteLanguage);
                  this.radioOptions = this.favoriteLanguage;

                },
                error => {
                  this.showToasterError(this.translate.instant('Error.text')  + this.translate.instant('NoReadSuccessful.text'));
                });


  }

   writeLenguageFavouriteJSONFileSettings(language: string): void{
    this.userService.writeSettingsJSONFiles(this.currentUser, language , 'settings/' + this.currentUser.username + '/settings.json')
              .subscribe(
                data => {
                  //  this.settings = JSON.parse(data);
                this.showToaster(this.translate.instant('SettingsSave.text')  + this.translate.instant('Successful.text'));

                },
                error => {
                  this.showToasterError(this.translate.instant('Error.text')  + this.translate.instant('NoReadSuccessful.text'));
                });


  }

}
