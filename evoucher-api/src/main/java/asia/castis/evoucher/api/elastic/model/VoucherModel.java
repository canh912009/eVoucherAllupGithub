package asia.castis.evoucher.api.elastic.model;

import asia.castis.evoucher.api.elastic.enums.EnumMessageType;
import asia.castis.evoucher.api.elastic.enums.EnumVoucherStatus;
import asia.castis.evoucher.api.elastic.enums.EnumVoucherTransferStatus;
import asia.castis.evoucher.api.elastic.enums.EnumVoucherType;
import asia.castis.evoucher.api.elastic.model.publish.GoodsModel;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;


@Data
@Document(indexName = "#{@environment.getProperty('elastic.voucher.indexName')}")
public class VoucherModel {
    @Id
    private String id;
    @Field(type = FieldType.Text)
    private String originalVoucherId;
    @Field(type = FieldType.Text)
    private String transferMessage;
    @Field(type = FieldType.Long)
    private long publishDetailId;
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
    private String subject;
    @Field(type = FieldType.Text)
    private String content;
    @Field(type = FieldType.Text)
    private String useInfo;
    @Field(type = FieldType.Text)
    private String sticker;
    @Field(type = FieldType.Text)
    private EnumVoucherStatus voucherStatus;
    @Field(type = FieldType.Text)
    private EnumVoucherTransferStatus transferStatus;
    @Field(type = FieldType.Double)
    private double voucherPrice;
    @Field(type = FieldType.Double)
    private double discountRate;
    @Field(type = FieldType.Double)
    private double discountLimitPrice;
    @Field(type = FieldType.Double)
    private double initialAmount;
    @Field(type = FieldType.Double)
    private double balance;
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
    /*
    * new part for external pin
    * */
    @Field(type = FieldType.Long)
    private long extPinId;
    @Field(type = FieldType.Text)
    private String extPinNo;
    @Field(type = FieldType.Text)
    /*
    *  URBOX/VNPT/EXT_PIN/
    * */
    private String extPinType;
    @Field(type = FieldType.Text)
    /*
    * QR/BARCODE/TEXT/
    * */
    private String system;
    private String contentLink;
    private String contentImagePath;
    private String contentImageName;
    private String externalPinPassword;
    @Field(type = FieldType.Text)
    private String choiceToken;
    @Field(type = FieldType.Text)
    private String choiceVoucherEv;
    @Field(type = FieldType.Text)
    private EnumMessageType smsType;

    @Field(type = FieldType.Text)
    private String serialNo;
    @Field(type = FieldType.Text)
    private String activationUrl;
    @Field(type = FieldType.Text)
    private String activationId;
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd' 'HH:mm:ss")
    private Date activationDate;
    @Version
    private Long version;
    @Field(type = FieldType.Text)
    private String externalPinType;
}
