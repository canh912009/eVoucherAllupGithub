package asia.castis.evoucher.push.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "#{@environment.getProperty('elastic.index.message')}")
public class PAMessage {
    /*
    public PAMessage(int publishScheduleId, String phone, String brandName, String message) {
       //this.messageId = messageId;
        this.publishScheduleId = publishScheduleId;
       this.phone = phone;
       this.brandName = brandName;
       this.message = message;
    }
     */
    /*
    public PAMessage() {}
     */
    @Id
    @Field(type = FieldType.Keyword)
    private String id;
    @Field(type = FieldType.Keyword)
    private String messageId;

    @Field(type = FieldType.Keyword)
    private Long publishScheduleId;

    @Field(type = FieldType.Keyword)
    private Long publishDetailId;

    @Field(type = FieldType.Keyword)
    private Long campaignId;

    @Field(type = FieldType.Keyword)
    private int partnerId;

    @Field(type = FieldType.Text)
    private String campaignName;

    @Field(type = FieldType.Keyword)
    private String smsType;

    @Field(type = FieldType.Keyword)
    private String brandName;

    @Field(type = FieldType.Keyword)
    private String telco;
    private String phone;
    @Field(type = FieldType.Text)
    private String message;

    @Field(type = FieldType.Keyword)
    private String type;
    // default is pending no result
    private int status = 2;
    private String znsMsgId;
    private String failoverSentTime;
    private String failoverReceivedTime;
    private int mtCount;


    // it is the same as status?
    // 1 is delivered, 0 not
    private int delivered = 0;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public Long getPublishScheduleId() {
        return publishScheduleId;
    }

    public void setPublishScheduleId(Long publishScheduleId) {
        this.publishScheduleId = publishScheduleId;
    }

    public Long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }

    public int getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(int partnerId) {
        this.partnerId = partnerId;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public String getSmsType() {
        return smsType;
    }

    public void setSmsType(String smsType) {
        this.smsType = smsType;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getTelco() {
        return telco;
    }

    public void setTelco(String telco) {
        this.telco = telco;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getZnsMsgId() {
        return znsMsgId;
    }

    public void setZnsMsgId(String znsMsgId) {
        this.znsMsgId = znsMsgId;
    }

    public String getFailoverSentTime() {
        return failoverSentTime;
    }

    public void setFailoverSentTime(String failoverSentTime) {
        this.failoverSentTime = failoverSentTime;
    }

    public String getFailoverReceivedTime() {
        return failoverReceivedTime;
    }

    public void setFailoverReceivedTime(String failoverReceivedTime) {
        this.failoverReceivedTime = failoverReceivedTime;
    }

    public int getMtCount() {
        return mtCount;
    }

    public void setMtCount(int mtCount) {
        this.mtCount = mtCount;
    }
    public Long getPublishDetailId() {
        return publishDetailId;
    }

    public void setPublishDetailId(Long publishDetailId) {
        this.publishDetailId = publishDetailId;
    }
    public int getDelivered() {
        return delivered;
    }

    public void setDelivered(int delivered) {
        this.delivered = delivered;
    }

}
