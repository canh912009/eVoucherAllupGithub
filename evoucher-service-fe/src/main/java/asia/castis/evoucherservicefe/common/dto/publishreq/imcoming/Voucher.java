package asia.castis.evoucherservicefe.common.dto.publishreq.imcoming;

import asia.castis.evoucherservicefe.common.enums.EnumTransferStatus;
import asia.castis.evoucherservicefe.common.enums.EnumVoucherStatus;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class Voucher {
    private String id;
    private String originalVoucherId;
    private String transferMessage;
    private String shortLink;
    private String voucherType;
    private Goods goods;
    private String userName; // encrypted
    private String userMobileNumber; // encrypted
    private String userEmail; // encrypted
    private String subject;
    private String content;
    private String contentLink; // Only available for Channel type voucher. Could be null for other types
    private String contentImageName; // Only available for Channel type voucher. Could be null for other types
    private String contentImagePath;
    private String externalPinPassword;
    // Only available for Channel type voucher. Could be null for other types
    private String useInfo;
    private String sticker;
    private EnumVoucherStatus voucherStatus;
    private EnumTransferStatus transferStatus;
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
    private String system;
    private Long extPinId;
    private String extPinNo;
    private String parentVoucherToken;
    private String parentVoucherEv;
    private Long publishDetailId;
    private String serialNo;
    private String activationUrl;
    private Date activationDate;
    private String activationId;
    /**
     * use for urbox external voucher
     *      QRCODE,
     *     BARCODE,
     *     TEXT,
     *     QRBAR;
     */
    private String extPinType;//
}
