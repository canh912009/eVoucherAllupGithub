package asia.castis.evoucherservicefe.common.utils;

import lombok.*;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidateResult {
    private boolean isValid;
    private String validationMessage;
}
