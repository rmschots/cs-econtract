package be.cipalschaubroeck.econtract.config;

import be.cipalschaubroeck.econtract.util.AppConstants;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.AuthorizationCodeGrantBuilder;
import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.GrantType;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.service.TokenEndpoint;
import springfox.documentation.service.TokenRequestEndpoint;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

import static springfox.documentation.builders.PathSelectors.regex;

/**
 * SpringFox Swagger configuration.
 *
 * @author David Debuck
 */
@Configuration
@EnableSwagger2
@Profile({AppConstants.APP_PROFILE_DEV, AppConstants.APP_PROFILE_NB1, AppConstants.APP_PROFILE_NB2})
public class SwaggerConfiguration implements InitializingBean {

    private static final String NOT_EMPTY = "not empty";

    @Value("${keycloak.auth-server-url}")
    private String baseUrl;

    @Value("${keycloak.resource}")
    private String clientId;

    @Value("${swagger.keycloak.realm.default}")
    private String realm;

    private String authUrl;

    private String tokenUrl;

    @Override
    public void afterPropertiesSet() throws Exception {
        authUrl = this.baseUrl + "/realms/" + this.realm + "/protocol/openid-connect/auth";
        tokenUrl = this.baseUrl + "/realms/" + this.realm + "/protocol/openid-connect/token";
    }

    @Bean
    public Docket productApi(final List<SecurityScheme> authorizationTypes) {
        return new Docket(DocumentationType.SWAGGER_2)
                .select().apis(RequestHandlerSelectors.basePackage("be.cipalschaubroeck.econtract.resource"))
                .paths(regex(AppConstants.API_V1 + "/.*"))
                .build().securitySchemes(authorizationTypes);
    }

    @Bean
    List<SecurityScheme> authorizationTypes() {
        return Lists.newArrayList(new OAuthBuilder()
                .name("oauth2")
                .grantTypes(grantTypes())
                .scopes(new ArrayList<>())
                .build());
    }

    /**
     * Create a security configuration bean to provide all information for building our URL.
     * Warning! we must provide a value, even if they aren't used, so null becomes foo :p
     *
     * @return a {@link springfox.documentation.swagger.web.SecurityConfiguration} object.
     */
    @Bean
    public SecurityConfiguration security() {
        return new SecurityConfiguration(
                clientId,
                NOT_EMPTY,
                this.realm,
                NOT_EMPTY,
                NOT_EMPTY,
                ApiKeyVehicle.HEADER,
                "api_key",
                ","
        );
    }

    /**
     * Define a list of grant types that will be used as options in Swagger-ui to authenticate against.
     *
     * @return list of {@link springfox.documentation.service.GrantType} objects.
     */
    private List<GrantType> grantTypes() {
        List<GrantType> grantType = new ArrayList<>();

        // Authorization grant
        // This is the normal flow that our application follows also.
        GrantType authorizationGrant = new AuthorizationCodeGrantBuilder()
                .tokenEndpoint(new TokenEndpoint(this.tokenUrl, "access_token"))
                .tokenRequestEndpoint(new TokenRequestEndpoint(this.authUrl, clientId, NOT_EMPTY))
                .build();
        grantType.add(authorizationGrant);

        return grantType;
    }
}
