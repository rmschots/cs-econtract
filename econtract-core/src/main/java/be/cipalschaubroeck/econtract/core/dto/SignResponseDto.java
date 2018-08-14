package be.cipalschaubroeck.econtract.core.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SignResponseDto {
    @NotNull
    private String SignResponse;
}
