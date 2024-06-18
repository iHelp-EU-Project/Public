import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { User } from '../models/index_models';
import { UserService, AuthenticationService, TranslationService, AlertService } from '../services/index_services';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { LanguageApp } from '../services/LanguageApp';
import { KeycloakService } from 'keycloak-angular';
import { KeycloakProfile } from 'keycloak-js';
import { timeInterval } from 'rxjs/operators';



@Component({
    selector: './app-nav-bar',
    templateUrl: './app-nav-bar.component.html',
    styleUrls: ['./styles_nav.css']

})

export class NavBarComponent implements OnInit {
    currentUser: User;
    users: User[] = [];
    host: any;


    language;

    trans: TranslationService;
    public isLoggedIn = false;
    public userProfile: KeycloakProfile | null = null;

    constructor(private userService: UserService,
                private alertService: AlertService,
                private authServ: AuthenticationService,
                private toastr: ToastrService,
                private translate: TranslateService,
                private transLang: TranslationService,
                private router: Router,
                private readonly keycloak: KeycloakService) {

        // this.currentUser = JSON.parse(localStorage.getItem('currentUser'));
        this.currentUser = { id: '', username: '', firstName: '', lastName: '' };
        this.language = transLang.getLanguage();
        this.trans = transLang;
        this.host = self.location.host.split(':')[0].toLocaleLowerCase();


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
            this.createWorkSpace();
            // this.loginAutorization();


        }


    }


    // deleteUser(id: string): any {
    //     this.userService.delete(id).subscribe(() => { this.loadAllUsers(); });
    // }

    // private loadAllUsers(): any {
    //     this.userService.getAll().subscribe(users => { this.users = users; });
    // }

    refresh(): void {
        window.location.reload();
    }

    // logOut(): any {
    //     this.currentUser.firstName = '';
    //     this.currentUser.lastName = '';
    //     this.currentUser.username = '';
    //     this.currentUser.id = null;
    //     this.showToaster(this.translate.instant('Goodbye.text'));
    //     this.router.navigateByUrl('/login');
    //     this.refresh();


    // }

    showToaster(message: string): any {
        this.toastr.success(message);
    }

    showToasterError(message: string): any {
        this.toastr.error(message);
    }


    public changeL(str): any {
        this.trans.changeLanguage(str);

    }

    public logOut(): any {
        // this.logoutAutorization();
        // .then((result) => {
        //     this.showToaster(this.translate.instant('Goodbye.text'));
        //     this.keycloak.logout('http://localhost:4200/');

        // });

        // setTimeout(() => {
        this.keycloak.logout('http://' + this.host); // + ':4200/');

        // }, 1500);
        // this.router.navigateByUrl('/');
        // this.refresh();

    }

     public login(): any {
        this.keycloak.login();
        this.showToaster(this.translate.instant('Welcome.text') + ' ' + this.currentUser.username);
    }

    createWorkSpace(): any {

         this.userService.createworkspace(this.currentUser)
            .subscribe(
                data => {
                    // set success message and pass true paramater to persist the message after redirecting to the login page
                    this.alertService.success(this.translate.instant('CreateWorkSpaceSuccessful.text'), true);
                    // this.router.navigate(['/login']);
                    this.showToaster(this.translate.instant('WorkSpaceCreated.text'));
                },
                error => {
                    this.alertService.error(error);
                    // this.loading = false;
                    this.showToasterError(this.translate.instant('ErrorWorkSpace.text'));
                });



    }

    // loginAutorization(): any {
    //       this.userService.loginAutorization(this.currentUser, this.isLoggedIn.toString())
    //         .subscribe(
    //             data => {
    //                 // set success message and pass true paramater to persist the message after redirecting to the login page
    //                 this.alertService.success(this.translate.instant('CreateWorkSpaceSuccessful.text'), true);
    //                 // this.router.navigate(['/login']);
    //                 this.showToaster(this.translate.instant('WorkSpaceCreated.text'));
    //             },
    //             error => {
    //                 this.alertService.error(error);
    //                 // this.loading = false;
    //                 this.showToasterError(this.translate.instant('ErrorWorkSpace.text'));
    //             });

    // }


    // logoutAutorization(): any {
    //       this.userService.logoutAutorization(this.currentUser, 'false')
    //         .subscribe(
    //             data => {
    //                 // set success message and pass true paramater to persist the message after redirecting to the login page
    //                 this.alertService.success(this.translate.instant('CreateWorkSpaceSuccessful.text'), true);
    //                 // this.router.navigate(['/login']);
    //                 this.showToaster(this.translate.instant('WorkSpaceCreated.text'));
    //             },
    //             error => {
    //                 this.alertService.error(error);
    //                 // this.loading = false;
    //                 this.showToasterError(this.translate.instant('ErrorWorkSpace.text'));
    //             });

    // }



}
