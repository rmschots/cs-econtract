import { BrowserModule } from '@angular/platform-browser';
import { APP_INITIALIZER, NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { BaseModule, ConfigurationService, GlobalMessageModule, MultiTranslateHttpLoader } from '@cipalschaubroeck/jangosquare-common';
import { KeycloakModule, KeycloakService, TenantZonesService } from '@cipalschaubroeck/jangosquare-keycloak-integration';
import { appInitializer } from './app-initializer';
import { TranslateModule } from '@ngx-translate/core';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    KeycloakModule,
    TranslateModule.forRoot(),
    GlobalMessageModule.forRoot(),
    BaseModule
  ],
  providers: [
    ConfigurationService,
    {
      provide: APP_INITIALIZER,
      useFactory: appInitializer,
      multi: true,
      deps: [
        ConfigurationService,
        HttpClient,
        KeycloakService,
        TenantZonesService
      ]
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
