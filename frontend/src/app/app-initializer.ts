import { HttpClient } from '@angular/common/http';
import { BaseHttpService, Config, ConfigurationService } from '@cipalschaubroeck/jangosquare-common';
import {
  KeycloakService,
  OptionalHeaderAttributeEnum,
  TenantZonesService
} from '@cipalschaubroeck/jangosquare-keycloak-integration';
import { throwError } from 'rxjs';
import { catchError, map, switchMap, tap } from 'rxjs/operators';

export function appInitializer(
  configurationService: ConfigurationService,
  httpClient: HttpClient,
  keycloakService: KeycloakService,
  tenantZonesService: TenantZonesService
): () => Promise<any> {
  return () => {
    return configurationService.config$.pipe(
      map(config => createAPI_BASE(config)),
      tap(API_BASE => BaseHttpService.API_BASE = API_BASE),
      switchMap(API_BASE => httpClient.get<KeyCloakInfoResponse>(`${API_BASE}keycloakInfo`)),
      switchMap(keyCloakInfo => {
        tenantZonesService.init(`${BaseHttpService.API_BASE}tenants/zones`);

        return keycloakService.init({
          config: {
            url: keyCloakInfo.serverUrl,
            clientId: keyCloakInfo.clientId,
            optionalHeaderAttr: [OptionalHeaderAttributeEnum.ZONE]
          },
          bearerExcludedUrls: ['./assets/*']
        });
      }),
      catchError(error => {
        const _error = error.message || error;
        alert(_error);

        return throwError(_error);
      })
    )
    .toPromise();
  };
}

function createAPI_BASE(config: Config): string {
  return `${config.connection}${config.api_base}`;
}

interface KeyCloakInfoResponse {
  serverUrl: string;
  clientId: string;
}
