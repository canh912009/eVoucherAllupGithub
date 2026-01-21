package asia.castis.evoucher.push.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "#{@environment.getProperty('elastic.index.message_callback_zalo')}")
public class PAMessageCallBackZalo {
    @Id
    @Field(type = FieldType.Keyword)
    private String id;

    @Field(type = FieldType.Keyword)
    private String messageId;

    private String type;
    private int status;

    private String sent_time;
    private String received_time;
    private String error;
    private String error_info;
    // SMS error
    private String error_failover;
    //start time of SMS sending
    private String failover_sent_time;
    public PAMessageCallBackZalo() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSent_time() {
        return sent_time;
    }

    public void setSent_time(String sent_time) {
        this.sent_time = sent_time;
    }

    public String getReceived_time() {
        return received_time;
    }

    public void setReceived_time(String received_time) {
        this.received_time = received_time;
    }

    public String getError_info() {
        return error_info;
    }

    public void setError_info(String error_info) {
        this.error_info = error_info;
    }

    public String getError_failover() {
        return error_failover;
    }

    public void setError_failover(String error_failover) {
        this.error_failover = error_failover;
    }

    public String getFailover_sent_time() {
        return failover_sent_time;
    }

    public void setFailover_sent_time(String failover_sent_time) {
        this.failover_sent_time = failover_sent_time;
    }

}
