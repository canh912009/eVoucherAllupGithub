package asia.castis.web_hook.bean.entity;

import asia.castis.web_hook.common.SystemType;
import asia.castis.web_hook.common.VoucherStatusCode;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@FieldDefaults(level= AccessLevel.PRIVATE)
@Table(name = "tb_voucher")
public class Voucher {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ev")
    private String id;
    @Basic
    @Column(name = "balance")
    private Double balance;
    @Basic
    @Column(name = "campaign_id")
    private long campaignId;
    @Basic
    @Column(name = "cancel_dt")
    private Date cancelDate;
    @Basic
    @Column(name = "content")
    private String content;
    @Basic
    @Column(name = "creation_dt")
    private Date creationDate;
    @Basic
    @Column(name = "dc_limit_price")
    private Double discountLimitPrice;
    @Basic
    @Column(name = "dc_rate")
    private Double discountRate;
    @Basic
    @Column(name = "disuse_dt")
    private Date disuseDate;
    @Basic
    @Column(name = "expiration_dt")
    private Date expirationDate;
    @Column(name = "goods_id")
    private Long goods_id;
    @Basic
    @Column(name = "img_url")
    private String imgUrl;
    @Basic
    @Column(name = "init_amount")
    private Double initialAmount;
    @Basic
    @Column(name = "last_exchange_dt")
    private Date lastExchangeDate;
    @Basic
    @Column(name = "publish_dt")
    private Date publishDate;
    @Basic
    @Column(name = "publish_dtl_id")
    private long publishDtlId;
    @Basic
    @Column(name = "publish_id")
    private long publishId;
    @Basic
    @Column(name = "reg_dt")
    private Date regDt;
    @Basic
    @Column(name = "short_link")
    private String shortLink;
    @Basic
    @Column(name = "sticker")
    private String sticker;
    @Basic
    @Column(name = "subject")
    private String subject;
    @Basic
    @Column(name = "test_yn")
    private String testYn;
    @Basic
    @Column(name = "transfer_dt")
    private Date transferDate;
    @Basic
    @Column(name = "transfer_status_cd")
    private String transferStatusCode;
    @Basic
    @Column(name = "useinfo")
    private String useInfo;
    @Basic
    @Column(name = "user_mobile_num")
    private String userMobileNumber;
    @Basic
    @Column(name = "user_nm")
    private String userName;
    @Basic
    @Column(name = "voucher_price")
    private Double voucherPrice;
    @Basic
    @Column(name = "voucher_status_cd")
    @Enumerated(EnumType.STRING)
    private VoucherStatusCode voucherStatusCode;
    @Basic
    @Column(name = "voucher_type_cd")
    private String voucherTypeCd;
    @Basic
    @Column(name = "ORIG_EV")
    private String origEv;
    @Basic
    @Column(name = "TRANSFER_MSG")
    private String transferMsg;
    @Column(name = "EXT_PIN_ID")
    private Long extPinId;
    @Column(name = "EXT_PIN_NO")
    private String extPinNo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EXT_PIN_NO", referencedColumnName = "ext_pin_no", insertable = false, updatable = false)
    private ExtPin extPin;

    @Column(name = "EXT_PIN_TYPE")
    private String externalPinType;
    @Column(name = "SYSTEM")
    @Enumerated(EnumType.STRING)
    private SystemType system;
    @Column(name = "CONTENT_LINK")
    private String contentLink;
    @Column(name = "content_image_path")
    private String contentImagePath;
    @Column(name = "content_image_name")
    private String contentImageName;
    @Column(name = "EXT_PIN_PASSWORD")
    private String externalPinPassword;
    @Column(name = "parent_voucher_ev")
    private String parentVoucherEv;
    @Column(name = "parent_voucher_token")
    private String parentVoucherToken;
    @Column(name = "SERIAL_NO")
    private String serialNo;
    @Column(name = "ACTIVATION_URL")
    private String activationUrl;
    @Column(name = "ACTIVATION_DT")
    private Date activationDate;
    @Column(name = "ACTIVATION_ID")
    private String activationId;

}
