package be.cipalschaubroeck.econtract.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Database configuration.
 *
 * @author David Debuck
 */
@EnableJpaRepositories("be.cipalschaubroeck.econtract.*")
@EnableJpaAuditing
@EnableTransactionManagement
@Configuration
public class DatabaseConfiguration {

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Value("${spring.datasource.url}")
    private String jdbcUrl;

    @Value("${spring.datasource.username}")
    private String userName;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.pool-size:10}")
    private int poolSize;

    @Bean
    public DataSource primaryDataSource() {
        final HikariDataSource ds = new HikariDataSource();
        ds.setMaximumPoolSize(poolSize);
        ds.setDriverClassName(driverClassName);
        ds.setJdbcUrl(jdbcUrl);
        ds.setUsername(userName);
        ds.setPassword(password);

        Properties properties = new Properties();
        ds.setDataSourceProperties(properties);

        ds.setAutoCommit(false);
        return ds;
    }

    @Bean
    @Primary
    public DataSource dataSource() {
        return primaryDataSource();
    }
}
