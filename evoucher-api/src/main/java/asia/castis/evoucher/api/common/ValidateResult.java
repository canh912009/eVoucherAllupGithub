package asia.castis.evoucher.api.common;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ValidateResult {
    private boolean isValid;
    private String validationMessage;
}
