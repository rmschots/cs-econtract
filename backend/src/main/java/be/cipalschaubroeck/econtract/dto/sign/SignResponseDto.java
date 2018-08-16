package be.cipalschaubroeck.econtract.dto.sign;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SignResponseDto {
    @NotNull
    private String SignResponse;
}
