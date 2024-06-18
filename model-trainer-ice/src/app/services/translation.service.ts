import { Injectable } from '@angular/core';
import { Router, NavigationStart } from '@angular/router';
import { Observable, Subject } from 'rxjs';
// import { Subject } from 'rxjs/Subject';
import { TranslateService } from '@ngx-translate/core';


@Injectable()
export class TranslationService {

    public activeLang = 'en';


    constructor( private translate: TranslateService){

        this.translate.setDefaultLang(this.activeLang);

    }

     public changeLanguage(lang): any {
        this.activeLang = lang;
        this.translate.use(lang);
    }

    public getLanguage(): string{
        return this.activeLang;
    }

    // public setLenguage(la: string): void{
    //     this.changeLanguage(la);
    // }
}
