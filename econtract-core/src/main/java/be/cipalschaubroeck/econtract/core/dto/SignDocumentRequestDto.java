package be.cipalschaubroeck.econtract.core.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SignDocumentRequestDto {
    @NotNull
    private String completionUrl;
}
