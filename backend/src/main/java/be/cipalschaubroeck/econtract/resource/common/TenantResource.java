package be.cipalschaubroeck.econtract.resource.common;

import be.cipalschaubroeck.econtract.util.AppConstants;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static be.cipalschaubroeck.econtract.resource.common.TenantResource.TENANT_RESOURCE_ENDPOINT;

/**
 * TenantDto resource.
 *
 * @author David Debuck
 */
@RestController
@RequestMapping(value = TENANT_RESOURCE_ENDPOINT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class TenantResource {

    public static final String TENANT_RESOURCE_ENDPOINT = AppConstants.API_V1 + "/tenants";

    @GetMapping(value = "zones")
    public ResponseEntity getTenantZones() {
        return ResponseEntity.ok(null);
    }
}
