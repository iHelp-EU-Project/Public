import { AppComponent } from './app.component';
import { AppModule } from './app.module';
import { NgModule } from '@angular/core';
import { BrowserModule , BrowserTransferStateModule} from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NZ_I18N } from 'ng-zorro-antd/i18n';
import { uk_UA, en_US } from 'ng-zorro-antd/i18n';
import { registerLocaleData } from '@angular/common';
import uk from '@angular/common/locales/uk';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

registerLocaleData(uk);

@NgModule({
bootstrap: [AppComponent],
imports: [
BrowserModule.withServerTransition({appId: 'app-root'}),
BrowserTransferStateModule,
AppModule,
BrowserAnimationsModule,
FormsModule,
HttpClientModule,

],
providers: [{ provide: NZ_I18N, useValue: uk_UA }]
})
export class AppBrowserModule {}
