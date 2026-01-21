package asia.castis.evoucher.api.entity;

import asia.castis.evoucher.api.common.enums.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TB_VOUCHER")
@EntityListeners(AuditingEntityListener.class)
public class EVoucher {
    @Id
    @Column(name = "EV")
    private String eV;
    @Column(name = "PUBLISH_ID")
    private Integer publishId;
    @Column(name = "PUBLISH_DTL_ID")
    private Integer publishDetailId;
    @Column(name = "GOODS_ID")
    private Integer goodsId;
    @Column(name = "CAMPAIGN_ID")
    private Integer campaignId;
    @CreatedDate
    @Column(name = "REG_DT", updatable = false)
    private Date registDate;
    @Column(name = "USER_ID")
    private String userId;
    @Column(name = "USER_MOBILE_NUM")
    private String userMobileNumber;
    @Column(name = "USER_NM")
    private String userName;
    @Column(name = "TEST_YN")
    @Enumerated(EnumType.STRING)
    private EnumValidYn testYn;
    @Column(name = "CREATION_DT")
    private Date creationDate;
    @Column(name = "EXPIRATION_DT")
    private Date expirationDate;
    @Column(name = "PUBLISH_DT")
    private Date publishDate;
    @Column(name = "LAST_EXCHANGE_DT")
    private Date lastExchangeDate;
    @Column(name = "DISUSE_DT")
    private Date disuseDate;
    @Column(name = "CANCEL_DT")
    private Date cancelDate;
    @Column(name = "TRANSFER_DT")
    private Date transferDate;
    @Column(name = "SHORT_LINK")
    private String shortLink;
    @Column(name = "VOUCHER_TYPE_CD")
    @Enumerated(EnumType.STRING)
    private VoucherTypeCode voucherTypeCode;
    @Column(name = "STICKER")
    private String sticker;
    @Column(name = "VOUCHER_STATUS_CD")
    @Enumerated(EnumType.STRING)
    private VoucherStatusCode voucherStatusCode;
    @Column(name = "TRANSFER_STATUS_CD")
    @Enumerated(EnumType.STRING)
    private TransferStatusCode transferStatusCode;
    @Column(name = "SUBJECT")
    private String subject;
    @Column(name = "CONTENT")
    private String content;
    @Column(name = "USEINFO")
    private String useInfo;
    @Column(name = "IMG_URL")
    private String imageUrl;
    @Column(name = "VOUCHER_PRICE")
    private Double voucherPrice;
    @Column(name = "DC_RATE")
    private Double discountRate;
    @Column(name = "DC_LIMIT_PRICE")
    private Double discountLimitPrice;
    @Column(name = "INIT_AMOUNT")
    private Double initAmount;
    @Column(name = "BALANCE")
    private Double balance;
    @Column(name = "ORIG_EV")
    private String originalEv;
    @Column(name = "TRANSFER_MSG")
    private String transferMessage;
    @Column(name = "EXT_PIN_ID")
    private Integer externalPinId;
    @Column(name = "EXT_PIN_NO")
    private String externalPinNo;
    @Column(name = "EXT_PIN_TYPE")
    @Enumerated(EnumType.STRING)
    private PinDisplayType externalPinType;
    @Column(name = "SYSTEM")
    @Enumerated(EnumType.STRING)
    private SystemType system;
    @Column(name = "EXT_PIN_PASSWORD")
    private String externalPinPassword;
    @Column(name = "CONTENT_LINK")
    private String contentLink;
    @Column(name = "CONTENT_IMAGE_PATH")
    private String contentImagePath;
    @Column(name = "CONTENT_IMAGE_NAME")
    private String contentImageName;
    @Column(name = "PARENT_VOUCHER_EV")
    private String parentVoucherEv;
    @Column(name = "PARENT_VOUCHER_TOKEN")
    private String parentVoucherToken;
    @Column(name = "SERIAL_NO")
    private String serialNo;
    @Column(name = "ACTIVATION_URL")
    private String activationUrl;
    @Column(name = "ACTIVATION_ID")
    private String activationId;
    @Column(name = "ACTIVATION_DT")
    private Date activationDate;

    // LC
    @Column(name = "usage_count")
    private Integer usageCount;
    @Column(name = "usage_remaining_count")
    private Integer usageRemainingCount;

    @Column(name = "VOUCHER_VERSION")
    private Short voucherVersion;

}
