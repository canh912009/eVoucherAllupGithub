package asia.castis.otpservice.common;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ValidateResult {
    private boolean isValid;
    private String validationMessage;
}
