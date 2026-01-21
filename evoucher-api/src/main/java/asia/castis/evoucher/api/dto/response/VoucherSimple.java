package asia.castis.evoucher.api.dto.response;

import asia.castis.evoucher.api.elastic.enums.EnumVoucherStatus;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class VoucherSimple {

    private String shortLink;
    private String type;
    private EnumVoucherStatus status;
    private String sticker;
    private String transferStatus;
    private String goodsName;
    private String goodsDesc;
    private String goodsImgurl;
    private String issueDate;
    private String expirationDate;
    private String title;
    private String imgUrl;
    private double voucherPrice;
    private int discountRate;
    private long dcLimitPrice;
    private long chargedAmount;
    private long balance;
}
