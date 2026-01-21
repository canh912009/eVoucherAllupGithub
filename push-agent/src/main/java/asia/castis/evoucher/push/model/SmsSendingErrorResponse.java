package asia.castis.evoucher.push.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SmsSendingErrorResponse {
    private int error;
    private String error_description;
}