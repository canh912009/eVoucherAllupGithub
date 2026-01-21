package asia.castis.evoucher.push.callback_receive.zalo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "#{@environment.getProperty('elastic.index')}", createIndex=false)
public class PAMessage {
    @Id
    @Field(type = FieldType.Keyword)
    private String id;
    @Field(type = FieldType.Keyword)
    private String messageId;

    @Field(type = FieldType.Keyword)
    private int publishScheduleId;

    @Field(type = FieldType.Keyword)
    private int campaignId;

    @Field(type = FieldType.Keyword)
    private int partnerId;

    @Field(type = FieldType.Text)
    private String campaignName;

    @Field(type = FieldType.Keyword)
    private String smsType;

    @Field(type = FieldType.Keyword)
    private String branchName;

    @Field(type = FieldType.Keyword)
    private String telco;
    private String phone;
    @Field(type = FieldType.Text)
    private String message;

    @Field(type = FieldType.Keyword)
    private String type;
    private int status;
    private String znsMsgId;
    private String sendTime;
    private String receivedTime;
    private String failoverSentTime;
    private String failoverReceivedTime;
    private int mtCount;


    private String errorCode;
    private String error;
    private String errorFailOver;
    private String failOverSentTime;

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getReceivedTime() {
        return receivedTime;
    }

    public void setReceivedTime(String receivedTime) {
        this.receivedTime = receivedTime;
    }

    public String getErrorFailOver() {
        return errorFailOver;
    }

    public void setErrorFailOver(String errorFailOver) {
        this.errorFailOver = errorFailOver;
    }

    public String getFailOverSentTime() {
        return failOverSentTime;
    }

    public void setFailOverSentTime(String failOverSentTime) {
        this.failOverSentTime = failOverSentTime;
    }

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

    public int getPublishScheduleId() {
        return publishScheduleId;
    }

    public void setPublishScheduleId(int publishScheduleId) {
        this.publishScheduleId = publishScheduleId;
    }

    public int getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(int campaignId) {
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

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
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

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

}
