import { Component, OnInit, EventEmitter, Output } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { AlertService, UserService, AuthenticationService } from '../services/index_services';
import { User } from '../models/index_models';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { LanguageApp } from '../services/LanguageApp';
import { TranslationService } from '../services/translation.service';
import { KeycloakService } from 'keycloak-angular';
import { KeycloakProfile } from 'keycloak-js';


@Component({
  selector: 'app-myaccount',
  templateUrl: './myaccount.component.html',
  styleUrls: ['./myaccount.component.css']
})
export class MyaccountComponent implements OnInit {

    model: any = {};
    loading = false;
    currentUser: User;
    returnUrl: string;
    users: User[] = [];
    @Output() fireIsLoggedOut: EventEmitter<any> = new EventEmitter<any>();
    public isLoggedIn = false;
    public userProfile: KeycloakProfile | null = null;


    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private userService: UserService,
        private authenticationService: AuthenticationService,
        private alertService: AlertService,
        private toastr: ToastrService,
        private translate: TranslateService,
        private transLang: TranslationService,
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


    }


    update(): any {
        this.loading = true;
        this.model.id = this.currentUser.id;

        this.userService.update(this.currentUser.id, this.model)
          .subscribe(
              data => {
                  // set success message and pass true paramater to persist the message after redirecting to the login page
                  this.alertService.success(this.translate.instant('UpdateSuccess.text'), true);
                  this.router.navigate(['/login']);
                //   this.loadAllUsers();
                  this.currentUser = JSON.parse(localStorage.getItem('currentUser'));
                  this.loading = false;
                  this.showToaster(this.translate.instant('UserUpdated.text'));
              },
              error => {
                  this.alertService.error(error);
                  this.loading = false;
                  this.showToasterError(this.translate.instant('UserErrorUpdate.text'));
              });



    }

    deleteUser(): any {
        this.userService.delete(this.currentUser.id)
            .subscribe(() => {
                // this.loadAllUsers();
                this.router.navigate(['/login']);
                this.authenticationService.getEmitterOut().subscribe((customObject) => {
                    this.currentUser.firstName = '';
                    this.currentUser.lastName = '';
                    this.currentUser.username = '';
                });
                this.showToaster(this.translate.instant('UserDelete.text'));
            },
                error => {
                    this.alertService.error(error);
                    this.loading = false;
                    this.showToasterError(this.translate.instant('UserError.text'));
                });

        this.userService.deleteworkspace(this.currentUser)
        .subscribe(() => {
            // this.loadAllUsers();
            this.showToaster(this.translate.instant('WorkspaceDeleted.text'));
        },
            error => {
                this.alertService.error(error);
                this.loading = false;
                this.showToasterError(this.translate.instant('UserDelete.text'));
            });
    }

    // private loadAllUsers(): any {
    //     this.userService.getAll().subscribe(users => { this.users = users; });
    // }

    getEmitterAccount(): any {
        return this.fireIsLoggedOut;
    }

    showToaster(message: string): any{
      this.toastr.success(message);
    }

    showToasterError(message: string): any{
      this.toastr.error(message);
    }


}
