package asia.castis.evoucher.api.dto.request;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserVoucherDetailRequest {
    private String voucher;
    private String otp;
}
