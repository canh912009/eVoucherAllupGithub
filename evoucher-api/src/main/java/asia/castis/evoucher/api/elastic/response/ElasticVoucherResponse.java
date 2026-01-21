package asia.castis.evoucher.api.elastic.response;

import asia.castis.evoucher.api.elastic.enums.EnumVoucherStatus;
import asia.castis.evoucher.api.elastic.enums.EnumVoucherTransferStatus;
import asia.castis.evoucher.api.elastic.enums.EnumVoucherType;
import asia.castis.evoucher.api.elastic.model.publish.CustomerModel;
import asia.castis.evoucher.api.elastic.model.publish.GoodsModel;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Data
@ToString
public class ElasticVoucherResponse {
    private GoodsModel goods;
    private CustomerModel customer;
    @Id
    private String id;
    private String shortLink;
    private EnumVoucherType voucherType;
    private String userName; // encrypted
    private String userMobileNumber; // encrypted
    private String subject;
    private String content;
    private String useInfo;
    private String sticker;
    private EnumVoucherStatus voucherStatus;
    private EnumVoucherTransferStatus transferStatus;
    private Double voucherPrice;
    private Double discountRate;
    private Double discountLimitPrice;
    private Double initialAmount;
    private Double balance;
    private Date createDate;
    private Date expireDate;
    private Date publishDate;
    private Date lastExchangeDate;
    private Date disuseDate;
    private Date cancelDate;
    private Date transferDate;
    private String originalVoucherId;
    private String originalUserName;
    private String transferMessage;
    @JsonProperty("isPosLink")
    private boolean isPosLink;
    private long extPinId;
    private String extPinNo;
    /*
     *  URBOX/VNPT/EXT_PIN/
     * */
    private String extPinType;
    /*
     * QR/BARCODE/TEXT/
     * */
    private String system;
    private String contentLink;
    private String contentImagePath;
    private String contentImageName;
    private String externalPinPassword;
    private String choiceToken;
    private String choiceVoucherEv;
    private String smsType;
}
