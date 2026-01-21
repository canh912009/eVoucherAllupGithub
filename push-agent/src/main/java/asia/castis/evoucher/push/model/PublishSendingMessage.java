package asia.castis.evoucher.push.model;

import java.io.Serializable;

public class PublishSendingMessage implements Serializable {
    private Long publishDetailId;

    private Long publishId;
    private String messageId;
    private int campaignId;
    private int partnerId;
    private String smsType;

    private String brandName;
    private String telco;
    private String phone;
    private String message;
    private String type;
    private int status;
   private String znsMsgId;
   private String receivedTime;
   private String errorCode;
   private String error;
   private String failoverSentTime;
   private String failOverReceivedTime;
   private String mtCount;
    private String callbackUrl;
    private String imageUrl;


    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
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

    public String getSmsType() {
        return smsType;
    }

    public void setSmsType(String smsType) {
        this.smsType = smsType;
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

    public String getReceivedTime() {
        return receivedTime;
    }

    public void setReceivedTime(String receivedTime) {
        this.receivedTime = receivedTime;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getFailoverSentTime() {
        return failoverSentTime;
    }

    public void setFailoverSentTime(String failoverSentTime) {
        this.failoverSentTime = failoverSentTime;
    }

    public String getFailOverReceivedTime() {
        return failOverReceivedTime;
    }

    public void setFailOverReceivedTime(String failOverReceivedTime) {
        this.failOverReceivedTime = failOverReceivedTime;
    }

    public String getMtCount() {
        return mtCount;
    }

    public void setMtCount(String mtCount) {
        this.mtCount = mtCount;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public Long getPublishDetailId() {
        return publishDetailId;
    }

    public void setPublishDetailId(Long publishDetailId) {
        this.publishDetailId = publishDetailId;
    }
    public Long getPublishId() {
        return publishId;
    }

    public void setPublishId(Long publishId) {
        this.publishId = publishId;
    }

}
