package be.cipalschaubroeck.econtract.dto.sign;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SigningCompleteDto {
    private String redirectUrl;
}
