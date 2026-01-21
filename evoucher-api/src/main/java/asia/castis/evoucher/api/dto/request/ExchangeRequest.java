package asia.castis.evoucher.api.dto.request;

import asia.castis.evoucher.api.elastic.enums.PosType;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ExchangeRequest {
    private String otp;
    private String storeId;
    private Double listPrice;
    private long paymentAmount;
    private String staffMobileNum;
    private long goodsId;
    private String goodsName;
    private Integer posType = PosType.WEBPOS; //1 : WEBPOS - Default 2 : POS
    private Integer otpInputType; //0: Barcode Input - 1: Manual Input
    private String posCd;
    private String posVerType;
}
