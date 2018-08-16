package be.cipalschaubroeck.econtract.war;

import lombok.extern.slf4j.Slf4j;
import org.keycloak.adapters.springboot.KeycloakAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

@Slf4j
@SpringBootApplication(exclude = {KeycloakAutoConfiguration.class, DataSourceAutoConfiguration.class})
@ComponentScan("be.cipalschaubroeck.econtract.*")
@EntityScan("be.cipalschaubroeck.econtract.*")
public class Application {

    public static void main(final String[] args) {
        final SpringApplication app = new SpringApplication(Application.class);
        final Environment env = app.run(args).getEnvironment();
        log.info(String.format("Started e-contract on environment: %s", env));
    }
}
