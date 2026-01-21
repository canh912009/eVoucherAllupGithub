package asia.castis.evoucher.api.dto.response;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class TransferResponse {
    private String otp;
    private String to;
    private String toName;
}
