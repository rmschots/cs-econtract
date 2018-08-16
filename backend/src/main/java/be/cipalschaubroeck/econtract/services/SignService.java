package be.cipalschaubroeck.econtract.services;

import be.cipalschaubroeck.econtract.domain.SignSessionData;
import be.cipalschaubroeck.econtract.dto.common.FileType;
import be.cipalschaubroeck.econtract.dto.sign.DownloadSignedDocumentRequestDto;
import be.cipalschaubroeck.econtract.dto.sign.PendingRequestDto;
import be.cipalschaubroeck.econtract.dto.sign.SignDocumentRequestDto;
import be.cipalschaubroeck.econtract.dto.sign.SignResponseDto;
import be.cipalschaubroeck.econtract.dto.sign.SigningCompleteDto;
import be.cipalschaubroeck.econtract.mappers.DigitalSignatureServiceSessionMapper;
import be.cipalschaubroeck.econtract.repository.SignSessionDataRepository;
import be.e_contract.dssp.client.DigitalSignatureServiceClient;
import be.e_contract.dssp.client.DigitalSignatureServiceSession;
import be.e_contract.dssp.client.PendingRequestFactory;
import be.e_contract.dssp.client.SignResponseVerifier;
import be.e_contract.dssp.client.exception.ApplicationDocumentAuthorizedException;
import be.e_contract.dssp.client.exception.AuthenticationRequiredException;
import be.e_contract.dssp.client.exception.ClientRuntimeException;
import be.e_contract.dssp.client.exception.IncorrectSignatureTypeException;
import be.e_contract.dssp.client.exception.SubjectNotAuthorizedException;
import be.e_contract.dssp.client.exception.UnknownDocumentException;
import be.e_contract.dssp.client.exception.UnsupportedDocumentTypeException;
import be.e_contract.dssp.client.exception.UnsupportedSignatureTypeException;
import be.e_contract.dssp.client.exception.UserCancelException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.xml.security.exceptions.Base64DecodingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.transaction.Transactional;
import javax.xml.bind.JAXBException;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.Objects;

import static be.cipalschaubroeck.econtract.resource.sign.SignResource.SIGN_RESOURCE_URL;

@Service
@Slf4j
public class SignService {

    @Value("${server.base-url}:${server.port}${server.contextPath}")
    private String serverBaseUrl;

    @Autowired
    private DigitalSignatureServiceClient digitalSignatureServiceClient;

    @Autowired
    private SignSessionDataRepository signSessionDataRepository;

    @Autowired
    private DigitalSignatureServiceSessionMapper sessionMapper;

    @Transactional
    public PendingRequestDto uploadDocument(SignDocumentRequestDto requestDto) {
        log.info("signing document");
        byte[] fileData = Base64.getDecoder().decode(requestDto.getFileBase64());
        DigitalSignatureServiceSession session = uploadDocument(fileData, requestDto.getMimeType());

        String destination = String.format("%s%s/complete/%s", serverBaseUrl, SIGN_RESOURCE_URL, session.getSecurityTokenId());
        String pendingRequestString = PendingRequestFactory.createPendingRequest(session, destination, "nl");

        SignSessionData signSessionData = SignSessionData.builder()
                .securityTokenId(session.getSecurityTokenId())
                .completionUrl(requestDto.getCompletionUrl())
                .digitalSignatureServiceSession(sessionMapper.mapToBytes(session))
                .filename(requestDto.getFilename())
                .mimeType(requestDto.getMimeType())
                .build();
        signSessionDataRepository.save(signSessionData);
        return PendingRequestDto.builder()
                .pendingRequest(pendingRequestString)
                .securityTokenId(session.getSecurityTokenId())
                .build();
    }

    public SigningCompleteDto completeSigning(String securityTokenId, SignResponseDto signResponseDto) {
        log.info("completing signing document");
        SignSessionData signSessionData = signSessionDataRepository.findOne(securityTokenId);
        DigitalSignatureServiceSession session = sessionMapper.mapToSession(signSessionData.getDigitalSignatureServiceSession());
        validateSignResponse(signResponseDto, session);
        byte[] signedDocument = downloadSignedDocument(session);
        writeDocumentToFile(signedDocument);
        return SigningCompleteDto.builder()
                .redirectUrl(signSessionData.getCompletionUrl())
                .build();
    }

    public SignSessionData downloadSignedDocument(DownloadSignedDocumentRequestDto request) {
        SignSessionData signSessionData = signSessionDataRepository.findOne(request.getSecurityTokenId());
        Objects.requireNonNull(signSessionData, String.format("No signing request found for %s", request.getSecurityTokenId()));
        Objects.requireNonNull(signSessionData.getSignedDocument(), String.format("No signed document found for %s", request.getSecurityTokenId()));
        return signSessionData;
    }

    private void validateSignResponse(SignResponseDto signResponseDto, DigitalSignatureServiceSession session) {
        try {
            SignResponseVerifier.checkSignResponse(signResponseDto.getSignResponse(), session);
        } catch (JAXBException | ParserConfigurationException | IOException | SAXException | XMLSignatureException
                | MarshalException | UserCancelException | Base64DecodingException
                | SubjectNotAuthorizedException | ClientRuntimeException e) {
            throw new IllegalArgumentException("Could not check sign response", e);
        }
    }

    private void writeDocumentToFile(byte[] signedDocument) {
        try {
            FileUtils.writeByteArrayToFile(new File("signed-document.xml"), signedDocument);
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not write signed document to file", e);
        }
    }

    private byte[] downloadSignedDocument(DigitalSignatureServiceSession session) {
        try {
            return digitalSignatureServiceClient.downloadSignedDocument(session);
        } catch (UnknownDocumentException e) {
            throw new IllegalArgumentException("Could not download signed document", e);
        }
    }

    private DigitalSignatureServiceSession uploadDocument(byte[] pdfData, FileType mimeType) {
        try {
            return digitalSignatureServiceClient.uploadDocument(mimeType.getTextValue(), pdfData);
        } catch (UnsupportedDocumentTypeException | UnsupportedSignatureTypeException | AuthenticationRequiredException | IncorrectSignatureTypeException |
                ApplicationDocumentAuthorizedException e) {
            throw new IllegalArgumentException("could not upload document", e);
        }
    }
}
