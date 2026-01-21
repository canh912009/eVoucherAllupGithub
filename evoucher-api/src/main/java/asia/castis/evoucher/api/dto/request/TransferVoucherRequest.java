package asia.castis.evoucher.api.dto.request;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class TransferVoucherRequest {
    private String otp;
    private String to;
    private String toName;
    private String message;
}
