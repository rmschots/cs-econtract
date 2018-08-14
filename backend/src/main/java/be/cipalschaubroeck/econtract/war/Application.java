package be.cipalschaubroeck.econtract.war;

import lombok.extern.slf4j.Slf4j;
import org.keycloak.adapters.springboot.KeycloakAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(exclude = {KeycloakAutoConfiguration.class, LiquibaseAutoConfiguration.class})
@EnableJpaRepositories("be.cipalschaubroeck.econtract.*")
@EnableTransactionManagement
@ComponentScan("be.cipalschaubroeck.econtract.*")
@EntityScan("be.cipalschaubroeck.econtract.*")
@Slf4j
public class Application {

    public static void main(final String[] args) {
        final SpringApplication app = new SpringApplication(Application.class);
        System.setProperty("com.sun.xml.ws.transport.http.client.HttpTransportPipe.dump", "true");
        System.setProperty("com.sun.xml.internal.ws.transport.http.client.HttpTransportPipe.dump", "true");
        System.setProperty("com.sun.xml.ws.transport.http.HttpAdapter.dump", "true");
        System.setProperty("com.sun.xml.internal.ws.transport.http.HttpAdapter.dump", "true");
        System.setProperty("https.protocols", "SSLv3,TLSv1,TLSv1.1,TLSv1.2");
        final Environment env = app.run(args).getEnvironment();
        log.info(String.format("Started e-contract on environment: %s", env));
    }
}
