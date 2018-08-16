package be.cipalschaubroeck.econtract.config;

import be.cipalschaubroeck.econtract.util.AppConstants;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.List;
import java.util.Locale;

/**
 * Web MVC configuration.
 *
 * @author David Debuck
 */
@Configuration
public class MvcConfiguration extends WebMvcConfigurerAdapter {

    @Value("${cors.allowedOrigins}")
    private String allowedOrigins;

    @Override
    public void extendMessageConverters(final List<HttpMessageConverter<?>> converters) {
        for (final HttpMessageConverter<?> converter : converters) {
            if (converter instanceof MappingJackson2HttpMessageConverter) {
                final MappingJackson2HttpMessageConverter jsonMessageConverter = (MappingJackson2HttpMessageConverter) converter;
                final ObjectMapper objectMapper = jsonMessageConverter.getObjectMapper();
                objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
                objectMapper.enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                break;
            }
        }
    }

    // http://docs.spring.io/spring/docs/2.5.x/javadoc-api/org/springframework/web/servlet/class-use/LocaleResolver.html
    // Change Accept-Language in the header to change the language of the responses.
    @Bean
    public LocaleResolver localeResolver() {
        final AcceptHeaderLocaleResolver acceptHeaderLocaleResolver = new AcceptHeaderLocaleResolver();
        acceptHeaderLocaleResolver.setDefaultLocale(Locale.ENGLISH);
        return acceptHeaderLocaleResolver;

    }

    /**
     * Create the messageSource for the application.
     *
     * @return MessageSource
     */
    @Bean(name = "messageSource")
    public MessageSource messageSource() {
        final ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.addBasenames("i18n/messages", "i18n/validationMessages", "i18n/specMessages");
        messageSource.setCacheSeconds(-1); // cache forever
        return messageSource;
    }

    /**
     * Override the default Validator bean to use i18n messages for our JPA validator annotations.
     *
     * @return Validator
     */
    @Override
    public Validator getValidator() {
        final LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.setValidationMessageSource(messageSource());
        return validator;
    }

    /**
     * Add a CORS filter to our application.
     *
     * @return FilterRegistrationBean
     */
    @Bean
    @ConditionalOnProperty("cors.enabled")
    public FilterRegistrationBean corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(Boolean.TRUE);
        config.addAllowedOrigin(allowedOrigins);

        config.addAllowedHeader("Origin");
        config.addAllowedHeader("Accept");
        config.addAllowedHeader("X-Requested-With");
        config.addAllowedHeader("Content-Type");
        config.addAllowedHeader("Access-Control-Request-Method");
        config.addAllowedHeader("Access-Control-Request-Headers");
        config.addAllowedHeader("Authorization");
        config.addAllowedHeader("Tenant");

        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("HEAD");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("PATCH");

        source.registerCorsConfiguration("/**", config);
        final FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(-1);
        return bean;
    }

    @Override
    @Profile("!" + AppConstants.APP_PROFILE_TEST)
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(realmInterceptor());
    }

    @Bean
    public HandlerInterceptor realmInterceptor() {
        return new RealmInterceptor();
    }

}
