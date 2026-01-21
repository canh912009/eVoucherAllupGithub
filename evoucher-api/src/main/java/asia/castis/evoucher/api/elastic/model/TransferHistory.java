package asia.castis.evoucher.api.elastic.model;

import asia.castis.evoucher.api.elastic.enums.EnumVoucherTransferStatus;
import asia.castis.evoucher.api.elastic.enums.EnumVoucherType;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Data
@ToString
@Document(indexName = "#{@environment.getProperty('elastic.transfer.history.indexName')}")
public class TransferHistory {
    @Id
    private String id;
    @Field(type = FieldType.Text)
    private EnumVoucherTransferStatus status;
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
