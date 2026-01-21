package asia.castis.evoucher.push.callback_receive.sms.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "#{@environment.getProperty('elastic.index.callback_receive_sms')}")
public class PAMessageCallBackSms {
    @Id
    @Field(type = FieldType.Keyword)
    private String id;

    @Field(type = FieldType.Keyword)
    private int messageId;

    @Field(type = FieldType.Keyword)
    private String telco;
    private int status = 2;
    private int mtCount;
    private String error;


    // 0 (default) not yet update status, 1 updated status
    // this property use for check static late
    private int paMessageStatusUpdated = 0;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTelco() {
        return telco;
    }

    public void setTelco(String telco) {
        this.telco = telco;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getMtCount() {
        return mtCount;
    }

    public void setMtCount(int mtCount) {
        this.mtCount = mtCount;
    }
    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
    public int getPaMessageStatusUpdated() {
        return paMessageStatusUpdated;
    }

    public void setPaMessageStatusUpdated(int paMessageStatusUpdated) {
        this.paMessageStatusUpdated = paMessageStatusUpdated;
    }

}
