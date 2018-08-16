package be.cipalschaubroeck.econtract.resource.sign;

import be.cipalschaubroeck.econtract.domain.SignSessionData;
import be.cipalschaubroeck.econtract.dto.sign.DownloadSignedDocumentRequestDto;
import be.cipalschaubroeck.econtract.dto.sign.PendingRequestDto;
import be.cipalschaubroeck.econtract.dto.sign.SignDocumentRequestDto;
import be.cipalschaubroeck.econtract.dto.sign.SignResponseDto;
import be.cipalschaubroeck.econtract.dto.sign.SigningCompleteDto;
import be.cipalschaubroeck.econtract.services.SignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

import static be.cipalschaubroeck.econtract.util.AppConstants.API_V1;

@RestController
@RequestMapping(value = SignResource.SIGN_RESOURCE_URL, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class SignResource {

    public static final String SIGN_RESOURCE_URL = API_V1 + "/sign-document";

    @Autowired
    private SignService signService;

    @PostMapping
    public PendingRequestDto signDocument(@RequestBody @Valid SignDocumentRequestDto signDocumentRequestDto) {
        return signService.uploadDocument(signDocumentRequestDto);
    }

    @GetMapping(value = "/download", produces = MediaType.ALL_VALUE)
    public ResponseEntity<Resource> downloadSignedDocument(@RequestBody @Valid DownloadSignedDocumentRequestDto request) {
        SignSessionData document = signService.downloadSignedDocument(request);
        return ResponseEntity
                .ok()
                .contentType(document.getMimeType().getMediaType())
                .contentLength(document.getSignedDocument().length)
                .body(new ByteArrayResource(document.getSignedDocument()));
    }

    /**
     * This endpoint needs to be open for public
     *
     * @param securityTokenId uid for the signing request
     * @param signResponseDto contains the postback form data
     * @return
     */
    @PostMapping("/complete/{securityTokenId}")
    public ResponseEntity signDocumentComplete(@PathVariable String securityTokenId, @Valid SignResponseDto signResponseDto) {
        SigningCompleteDto signingCompleteDto = signService.completeSigning(securityTokenId, signResponseDto);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(signingCompleteDto.getRedirectUrl()));
        return ResponseEntity
                .status(HttpStatus.MOVED_PERMANENTLY)
                .headers(headers)
                .build();
    }
}
