import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router } from '@angular/router';
import { KeycloakAuthGuard, KeycloakService } from 'keycloak-angular';
import { ToastrService } from 'ngx-toastr';
import { TranslationService } from '../services/translation.service';
import { TranslateService } from '@ngx-translate/core';

@Injectable({
  providedIn: 'root'
})
export class AuthGuardKeyNN extends KeycloakAuthGuard {
    nn = false;
    constructor(
        protected readonly router: Router,
        protected readonly keycloak: KeycloakService,
        private toastr: ToastrService,
        private translate: TranslateService) {
        super(router, keycloak);
    }

    async isAccessAllowed(
        route: ActivatedRouteSnapshot,
        state: RouterStateSnapshot): Promise<boolean | UrlTree> {

        if (!this.authenticated) {
            await this.keycloak.login({
                redirectUri: window.location.origin + state.url,
            });
        } else {
            this.roles.forEach(async element => {
                if (element === 'nn') {

                    this.nn = true;
                }
            });


        }

        if (!this.nn){
            this.showToasterError(this.translate.instant('Authorization.Text'));
        }


        console.log(this.roles);
        return this.nn;
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
