import { Injectable, OnInit } from '@angular/core';
// import { Router, NavigationStart } from '@angular/router';
// import { Observable, Subject } from 'rxjs';
// import { Subject } from 'rxjs/Subject';
import { TranslateService } from '@ngx-translate/core';
import { ToastrService } from 'ngx-toastr';
import { UserService } from './index_services';
import { User } from '../models/index_models';
import { TranslationService } from './translation.service';
import { KeycloakService } from 'keycloak-angular';
import { KeycloakProfile } from 'keycloak-js';



@Injectable()
export class TimeLoadingService{

  interval;
  currentUser: User;
  loadNumberProgress: {progress: number, state: string, error: boolean};
  offProgressBar: boolean;
  public isLoggedIn = false;
  public userProfile: KeycloakProfile | null = null;

    constructor(private toastr: ToastrService,
                private userService: UserService,
                private translate: TranslateService,
                private readonly keycloak: KeycloakService) {

        // this.currentUser = JSON.parse(localStorage.getItem('currentUser'));
        this.currentUser = { id: '', username: '', firstName: '', lastName: '' };
        this.loadNumberProgress = null;
    }


 // tslint:disable-next-line:typedef
 async timeLoading() {

    this.loadNumberProgress = null;
    this.isLoggedIn = await this.keycloak.isLoggedIn();

    if (this.isLoggedIn) {
        this.userProfile = await this.keycloak.loadUserProfile();
        this.currentUser.id = this.userProfile.id;
        this.currentUser.username = this.userProfile.username;
        this.currentUser.firstName = this.userProfile.firstName;
        this.currentUser.lastName = this.userProfile.lastName;

    }


    this.interval = setInterval(() => {

        this.userService.readLoadingJSON(this.currentUser, 'loading.json', 'settings/' + this.currentUser.username)
              .subscribe(
                // tslint:disable-next-line:no-shadowed-variable
                data => {
                  this.loadNumberProgress = JSON.parse(data);
                  if (this.loadNumberProgress.error){
                    if (this.loadNumberProgress.state === 'Error in algorithm, sorry, please try again, thanks'){

                      this.showToasterError(this.translate.instant('ErrorDataInputPythonScripts.text'));

                    }else if (this.loadNumberProgress.state === 'Error in data input, sorry, please try again, thanks'){

                      this.showToasterError(this.translate.instant('ErrorAlgorithmPythonScripts.text'));

                    }else{

                      this.showToasterError(this.translate.instant('ErrorFailsPythonScripts.text'));
                    }
                    clearInterval(this.interval);
                    this.offProgressBar = false;

                  }


                  if (this.loadNumberProgress.progress === 100 && this.loadNumberProgress.state === 'Ending') {
                      // console.log('Progress in OnChanges END: ', this.loadNumberProgress);
                      clearInterval(this.interval);
                  }

                },
                error => {
                });
      }, 100000);


  }

  showToaster(message: string): any{
      this.toastr.success(message);
    }

     showToasterError(message: string): any{
      this.toastr.error(message);
  }
}
