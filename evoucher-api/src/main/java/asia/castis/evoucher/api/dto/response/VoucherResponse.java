package asia.castis.evoucher.api.dto.response;

import asia.castis.evoucher.api.common.enums.EnumShowPopupYn;
import asia.castis.evoucher.api.common.enums.TransferStatusCode;
import asia.castis.evoucher.api.common.enums.VoucherStatusCode;
import asia.castis.evoucher.api.common.enums.VoucherTypeCode;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VoucherResponse {
    @Id
    private String id;
    private String shortLink;
    private String userName; // encrypted
    private String userMobileNumber; // encrypted
    private String subject;
    private String content;
    private String useInfo;
    private String sticker;
    private Double voucherPrice;
    private Double discountRate;
    private Double discountLimitPrice;
    private Double initialAmount;
    private Double balance;

    private String createDate;
    private String expireDate;
    private String publishDate;
    private String lastExchangeDate;
    private String disuseDate;
    private String cancelDate;
    private String transferDate;
    private String activationDate;
    private String originalVoucherId;
    private String originalUserName;
    private String transferMessage;
    @JsonProperty("isPosLink")
    private boolean isPosLink;

    private long extPinId;
    private String extPinNo;
    private String extPinType;

    private String system;
    private String contentLink;
    private String contentImagePath;
    private String contentImageName;
    private String externalPinPassword;
    private String parentToken;
    private String parentEv;
    private String smsType;

    private Integer usageCount;
    private Integer usageRemainingCount;

    private GoodsResponse goods;
    private CustomerResponse customer;
    private VoucherTypeCode voucherType;
    private VoucherStatusCode voucherStatus;
    private TransferStatusCode transferStatus;
    private String senderName;

    private EnumShowPopupYn showPopupYn;
    private String serialNo;
}
