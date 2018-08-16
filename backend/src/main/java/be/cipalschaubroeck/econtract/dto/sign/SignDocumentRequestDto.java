package be.cipalschaubroeck.econtract.dto.sign;

import be.cipalschaubroeck.econtract.dto.common.FileType;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SignDocumentRequestDto {
    @NotNull
    private String completionUrl;

    @NotNull
    private String fileBase64;

    @NotNull
    private String filename;

    @NotNull
    private FileType mimeType;
}
