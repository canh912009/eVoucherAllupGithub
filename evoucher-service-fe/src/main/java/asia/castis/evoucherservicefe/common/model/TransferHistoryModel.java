package asia.castis.evoucherservicefe.common.model;

import asia.castis.evoucherservicefe.common.enums.EnumTransferStatus;
import asia.castis.evoucherservicefe.common.enums.EnumVoucherType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Document(indexName = "#{@environment.getProperty('elastic.transferHistory.indexName')}")
@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransferHistoryModel {
    @Id
    private String id;
    @Field(type = FieldType.Text)
    private EnumTransferStatus status;
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd' 'HH:mm:ss")
    private Date transactionDate;
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd' 'HH:mm:ss")
    private Date receiptConfirmDate;
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd' 'HH:mm:ss")
    private Date returnDate;
    @Field(type = FieldType.Text)
    private String fromVoucherShortLink;
    @Field(type = FieldType.Text)
    private String fromVoucherId;
    @Field(type = FieldType.Text)
    private String from;
    @Field(type = FieldType.Text)
    private String toVoucherShortLink;
    @Field(type = FieldType.Text)
    private String toVoucherId;
    @Field(type = FieldType.Text)
    private String to;
    @Field(type = FieldType.Text)
    private EnumVoucherType voucherTypeCode;
    @Field(type = FieldType.Double)
    private Double initAmount;
    @Field(type = FieldType.Double)
    private Double transferAmount;
}
