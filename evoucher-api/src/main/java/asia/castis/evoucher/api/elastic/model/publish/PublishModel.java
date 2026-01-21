package asia.castis.evoucher.api.elastic.model.publish;

import asia.castis.evoucher.api.elastic.enums.EnumPublishStatus;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.List;

@Data
@ToString
@Document(indexName = "#{@environment.getProperty('elastic.publish.indexName')}")
public class PublishModel {
    @Id
    private Long id;
    @Field(type = FieldType.Text)
    private String name;
    @Field(type = FieldType.Nested)
    private CampaignModel campaign;
    @Field(type = FieldType.Nested)
    private CustomerModel customer;
    private List<PublishDetailModel> publishDetails;
    @Field(type = FieldType.Boolean)
    private boolean isBooking;
    @Field(type = FieldType.Text)
    private String messageSubject;
    @Field(type = FieldType.Text)
    private String messageContent;
    @Field(type = FieldType.Text)
    private String messageCallingNumber;
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd' 'HH:mm:ss")
    private Date bookingDate;
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd' 'HH:mm:ss")
    private Date publishDate;
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd' 'HH:mm:ss")
    private Date cancelDate;
    @Field(type = FieldType.Boolean)
    private boolean isTestSend;
    @Field(type = FieldType.Boolean)
    private boolean isReceiverNoDuplicateAllowed;
    @Field(type = FieldType.Text)
    private String smsType;
    @Field(type = FieldType.Text)
    private String templateId;
    @Field(type = FieldType.Text)
    private EnumPublishStatus publishStatusCode;
    @Field(type = FieldType.Text)
    private String activationId;
    @Field(type = FieldType.Text)
    private String activationUrl;
}
