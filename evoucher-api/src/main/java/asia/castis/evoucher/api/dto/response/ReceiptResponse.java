package asia.castis.evoucher.api.dto.response;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ReceiptResponse {
    private String otp;
    private String name;
    private String birthday;
    private String gender;
    private String province;
    private String district;
}
