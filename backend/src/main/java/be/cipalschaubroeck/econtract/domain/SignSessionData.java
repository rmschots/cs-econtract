package be.cipalschaubroeck.econtract.domain;

import be.cipalschaubroeck.econtract.dto.common.FileType;
import be.cipalschaubroeck.econtract.util.AuthenticationUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "sign_session_data")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignSessionData {

    public static final String NO_LOGGED_ON_USER = "UNKNOWN";

    @Id
    @NotNull
    @Column(name = "security_token_id")
    private String securityTokenId;

    @NotNull
    @Column(name = "completion_url")
    private String completionUrl;

    @NotNull
    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @NotNull
    @Column(name = "creation_user")
    private String creationUser;

    @NotNull
    @Column(name = "filename")
    private String filename;

    @NotNull
    @Column(name = "mime_type")
    @Enumerated(EnumType.STRING)
    private FileType mimeType;

    @Lob
    @Column(name = "signed_document")
    private byte[] signedDocument;

    @Lob
    @NotNull
    @Column(name = "digital_signature_service_session")
    private byte[] digitalSignatureServiceSession;

    @PrePersist
    public void initCreationDate() {
        creationDate = LocalDateTime.now();
        creationUser = AuthenticationUtil.getLoggedInUser().orElse(NO_LOGGED_ON_USER);
    }
}
