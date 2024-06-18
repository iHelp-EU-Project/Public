import { enableProdMode } from '@angular/core';
import { AppBrowserModule } from '.././src/app/app.browser.module';
import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';

import { AppModule } from './app/app.module';
import { environment } from './environments/environment';

if (environment.production) {
  enableProdMode();
}

document.addEventListener('DOMContentLoaded', () => {
  platformBrowserDynamic().bootstrapModule(AppBrowserModule) // (AppModule)
  .catch(err => console.error(err));
});
