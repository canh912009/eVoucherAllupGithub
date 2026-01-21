package asia.castis.evoucherservicefe.common.model;

import asia.castis.evoucherservicefe.common.enums.EnumMessageType;
import asia.castis.evoucherservicefe.common.enums.EnumTransferStatus;
import asia.castis.evoucherservicefe.common.enums.EnumVoucherStatus;
import asia.castis.evoucherservicefe.common.enums.EnumVoucherType;
import asia.castis.evoucherservicefe.common.model.publish.GoodsModel;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;


@Document(indexName = "#{@environment.getProperty('elastic.voucher.indexName')}")
@Data
@ToString
public class VoucherModel {
    @Id
    private String id;
    @Field(type = FieldType.Text)
    private String originalVoucherId;
    @Field(type = FieldType.Text)
    private String transferMessage;
    @Field(type = FieldType.Long)
    private Long publishDetailId;
    @Field(type = FieldType.Nested)
    private GoodsModel goods;
    @Field(type = FieldType.Text)
    private String shortLink;
    @Field(type = FieldType.Text)
    private EnumVoucherType voucherType;
    @Field(type = FieldType.Text)
    private String userName; // encrypted
    @Field(type = FieldType.Text)
    private String userMobileNumber; // encrypted
    @Field(type = FieldType.Text)
    private String userEmail; // encrypted
    @Field(type = FieldType.Text)
    private String subject;
    @Field(type = FieldType.Text)
    private String content;
    @Field(type = FieldType.Text)
    private String contentLink;// Only available for Channel type voucher. Could be null for other types
    @Field(type = FieldType.Text)
    private String contentImagePath;
    @Field(type = FieldType.Text)
    private String externalPinPassword;// Only available for Channel type voucher. Could be null for other types
    @Field(type = FieldType.Text)
    private String contentImageName; // Only available for Channel type voucher. Could be null for other types
    @Field(type = FieldType.Text)
    private String useInfo;
    @Field(type = FieldType.Text)
    private String sticker;
    @Field(type = FieldType.Text)
    private EnumVoucherStatus voucherStatus;
    @Field(type = FieldType.Text)
    private EnumTransferStatus transferStatus;
    @Field(type = FieldType.Double)
    private Double voucherPrice;
    @Field(type = FieldType.Double)
    private Double discountRate;
    @Field(type = FieldType.Double)
    private Double discountLimitPrice;
    @Field(type = FieldType.Double)
    private Double initialAmount;
    @Field(type = FieldType.Double)
    private Double balance;
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd' 'HH:mm:ss")
    private Date createDate;
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd' 'HH:mm:ss")
    private Date expireDate;
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd' 'HH:mm:ss")
    private Date publishDate;
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd' 'HH:mm:ss")
    private Date lastExchangeDate;
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd' 'HH:mm:ss")
    private Date disuseDate;
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd' 'HH:mm:ss")
    private Date cancelDate;
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd' 'HH:mm:ss")
    private Date transferDate;

    @Field(type = FieldType.Text)
    private String system;
    @Field(type = FieldType.Long)
    private Long extPinId;
    @Field(type = FieldType.Text)
    private String extPinNo;
    @Field(type = FieldType.Text)
    private String choiceToken;
    @Field(type = FieldType.Text)
    private String choiceVoucherEv;
    @Field(type = FieldType.Text)
    private EnumMessageType smsType;
    @Field(type = FieldType.Text)
    private String outgoingRequest;
    @Field(type = FieldType.Text)
    private String serialNo;
    @Field(type = FieldType.Text)
    private String activationUrl;
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd' 'HH:mm:ss")
    private Date activationDate;
    @Field(type = FieldType.Text)
    private String activationId;
    @Field(type = FieldType.Text)
    private String extPinType;
}
