package be.cipalschaubroeck.econtract.dto.sign;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DownloadSignedDocumentRequestDto {
    private String securityTokenId;
}
