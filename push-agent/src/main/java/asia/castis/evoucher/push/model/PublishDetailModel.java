package asia.castis.evoucher.push.model;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@ToString
public class PublishDetailModel {
    @Id
    private Long id;
    @Field(type = FieldType.Text)
    private String messageId;
    @Field(type =FieldType.Text)
    private String messageType;
    @Field(type =FieldType.Text)
    private EnumPublishDetailStatus publishStatusCode;
    @Field(type =FieldType.Text)
    private String publishResultMessage;
    @Field(type = FieldType.Text)
    private String voucherId;
    private Integer voucherResendHistoryId;

}