package be.cipalschaubroeck.econtract.resource.common;

import be.cipalschaubroeck.econtract.util.AppConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static be.cipalschaubroeck.econtract.resource.common.KeycloakInfoResource.KEYCLOAK_INFO_RESOURCE_URL;

/**
 * KeycloakInfoResource.
 *
 * @Author: Frédéric Henry
 */
@RestController
@RequestMapping(value = KEYCLOAK_INFO_RESOURCE_URL, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class KeycloakInfoResource {

    public static final String KEYCLOAK_INFO_RESOURCE_URL = AppConstants.API_V1 + "/keycloakInfo";

    @Value("${keycloak.auth-server-url}")
    private String serverUrl;

    @Value("${keycloak.resource}")
    private String clientId;

    @GetMapping
    public ResponseEntity<KeycloakInfoDto> getKeycloakInfo() {
        return new ResponseEntity<>(
                KeycloakInfoDto.builder()
                        .clientId(clientId)
                        .serverUrl(serverUrl)
                        .build(), HttpStatus.OK);
    }
}
