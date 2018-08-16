package be.cipalschaubroeck.econtract.resource.common;

import lombok.Builder;
import lombok.Getter;

/**
 * KeycloakInfoDto.
 *
 * @Author: Frédéric Henry
 */
@Builder
@Getter
public class KeycloakInfoDto {
    private String serverUrl;
    private String clientId;

}
