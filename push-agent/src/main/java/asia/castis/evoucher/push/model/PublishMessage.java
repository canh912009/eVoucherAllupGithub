package asia.castis.evoucher.push.model;

import java.io.Serializable;

public class PublishMessage  implements Serializable {
    private String mobileNumber;
    private String message;
    private int publishDetailId;
    private String imageUrl;
    private int templateId;
    private ZaloTemplateDataOld templateData;
    private String smsMsg;
    private int failover;
    private String callbackUrl;
    public PublishMessage() {}
    public PublishMessage(String mobileNumber, String message, String brandName) {
        this.mobileNumber = mobileNumber;
        this.message = message;
        this.brandName = brandName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    private String brandName;
    public int getTemplateId() {
        return templateId;
    }

    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }

    public ZaloTemplateDataOld getTemplateData() {
        return templateData;
    }

    public void setTemplateData(ZaloTemplateDataOld templateData) {
        this.templateData = templateData;
    }

    public String getSmsMsg() {
        return smsMsg;
    }

    public void setSmsMsg(String smsMsg) {
        this.smsMsg = smsMsg;
    }

    public int getFailover() {
        return failover;
    }

    public void setFailover(int failover) {
        this.failover = failover;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
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
    public int getPublishDetailId() {
        return this.publishDetailId;
    }

    public void setPublishDetailId(int publishDetailId) {
        this.publishDetailId = publishDetailId;
    }

}
