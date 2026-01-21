package asia.castis.evoucher.push.model;

import java.util.List;

public class SendingVoucherMessageInquiry {
    Long publishScheduleId;
    Long campaignId;
    String campaignName;
    String smsType;
    int totalCount;


    int sendFailed;


    int sendSuccess;


    int totalSend;
    int totalReceiver;
    List<PublishSendingMessage> publishMessageList;

    public SendingVoucherMessageInquiry() {
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

    public List<PublishSendingMessage> getPublishMessageList() {
        return publishMessageList;
    }

    public void setPublishMessageList(List<PublishSendingMessage> publishMessageList) {
        this.publishMessageList = publishMessageList;
    }
    public int getSendFailed() {
        return sendFailed;
    }

    public void setSendFailed(int sendFailed) {
        this.sendFailed = sendFailed;
    }
    public int getSendSuccess() {
        return sendSuccess;
    }

    public void setSendSuccess(int sendSuccess) {
        this.sendSuccess = sendSuccess;
    }
    public int getTotalSend() {
        return totalSend;
    }

    public void setTotalSend(int totalSend) {
        this.totalSend = totalSend;
    }

    public int getTotalReceiver() {
        return totalReceiver;
    }

    public void setTotalReceiver(int totalReceiver) {
        this.totalReceiver = totalReceiver;
    }

}
