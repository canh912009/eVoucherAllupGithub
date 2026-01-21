package asia.castis.evoucher.push.model;

import java.io.Serializable;
import java.util.List;

public class PublishVoucherMessage {
    public static final String SMS_TYPE_ZALO = "zalo";
    public static final String SMS_TYPE_SMS = "sms";
    int publishScheduleId;
    int campaignId;
    String campaignName;
    String smsType;
    int totalCount;
    List<PublishMessage> publishMessageList;

    public PublishVoucherMessage() {
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

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<PublishMessage> getPublishMessageList() {
        return publishMessageList;
    }

    public void setPublishMessageList(List<PublishMessage> publishMessageList) {
        this.publishMessageList = publishMessageList;
    }
}
