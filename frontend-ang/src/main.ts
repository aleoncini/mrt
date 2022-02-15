import { enableProdMode } from '@angular/core';
import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';
import * as Keycloak from 'keycloak-js';

import { AppModule } from './app/app.module';
import { environment } from './environments/environment';

if (environment.production) {
  enableProdMode();
}


//keycloak init options
let initConfig = {
  url: 'https://access-sso.apps.ocp4.rhocplab.com/auth/',
  realm: 'rh',
  clientId: 'mrt'
}

let keycloak = Keycloak(initConfig);

keycloak.init({ onLoad: "login-required" });