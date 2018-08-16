package be.cipalschaubroeck.econtract.resource.common;

import be.cipalschaubroeck.econtract.dto.common.BuildInformationDto;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Default resource.
 *
 * @author David Debuck
 */
@RestController
@RequestMapping(value = "/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class DefaultResource {

    private final MessageSource messageSource;

    public DefaultResource(final MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Get build information.
     *
     * @return information about this build
     */
    @GetMapping
    public ResponseEntity<BuildInformationDto> getInformation() {
        // Example of i18, message usage
        final String[] params = {"CSdabs"};
        final String msg = messageSource.getMessage("site.greeting", params, LocaleContextHolder.getLocale());

        return new ResponseEntity<>(new BuildInformationDto(
                msg,
                getClass().getPackage().getImplementationVersion()
        ), HttpStatus.OK);
    }


}
