package asia.castis.evoucher.api.dto.request;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ReceiptRequest {

    private String otp;
    private String name;
    private String birthday; //yyyy-MM-dd
    private String gender; //MEN/WOMEN
    private String province;
    private String district;
}
