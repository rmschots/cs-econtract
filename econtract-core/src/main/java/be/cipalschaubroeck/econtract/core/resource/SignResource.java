package be.cipalschaubroeck.econtract.core.resource;

import be.cipalschaubroeck.econtract.core.dto.PendingRequestDto;
import be.cipalschaubroeck.econtract.core.dto.SignDocumentRequestDto;
import be.cipalschaubroeck.econtract.core.dto.SignResponseDto;
import be.cipalschaubroeck.econtract.core.dto.SigningCompleteDto;
import be.cipalschaubroeck.econtract.core.services.SignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

@RestController
public class SignResource {

    @Autowired
    private SignService signService;

    @GetMapping("/sign-document")
    public String test() {
        return "HOI";
    }

    @PostMapping("/sign-document")
    public PendingRequestDto signDocument(@RequestBody @Valid SignDocumentRequestDto signDocumentRequestDto) {
        return signService.uploadDocument(signDocumentRequestDto);
    }

    @PostMapping("/sign-document-complete/{securityTokenId}")
    public ResponseEntity signDocumentComplete(@PathVariable String securityTokenId, @Valid SignResponseDto signResponseDto) {
        SigningCompleteDto signingCompleteDto = signService.completeSigning(securityTokenId, signResponseDto);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(signingCompleteDto.getRedirectUrl()));
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }
}
