package be.cipalschaubroeck.econtract.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "sign_session_data")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignSessionData {

    @Id
    private String securityTokenId;

    @NotNull
    @Column(name = "completion_url")
    private String completionUrl;

    @Lob
    @NotNull
    @Column(name = "digital_signature_service_session")
    private byte[] digitalSignatureServiceSession;
}
