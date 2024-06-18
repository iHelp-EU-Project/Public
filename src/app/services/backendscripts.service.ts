import { Injectable } from '@angular/core';
import { UserService,  AlertService } from '../services/index_services';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { User } from '../models/index_models';

@Injectable()
export class BackendscriptsService{


    constructor(private userService: UserService,
                private alertService: AlertService,
                private toastr: ToastrService,
                private translate: TranslateService) {


    }


   loginAutorization(currentUser: User, isLoggedIn: string): any {
          this.userService.loginAutorization(currentUser, isLoggedIn)
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


    logoutAutorization(currentUser: User): any {
          this.userService.logoutAutorization(currentUser, 'false')
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

    showToaster(message: string): any {
        this.toastr.success(message);
    }

    showToasterError(message: string): any {
        this.toastr.error(message);
    }
}
