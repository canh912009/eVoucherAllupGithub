package asia.castis.evoucher.push.callback_receive.zalo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "#{@environment.getProperty('elastic.index')}")
public class PAMessageCallBackZalo {
    @Id
    @Field(type = FieldType.Keyword)
    private String id;
    @Field(type = FieldType.Keyword)
    private String messageId;
    @Field(type= FieldType.Keyword)
    private String type;
    private int status = 2;

    private String sentTime;
    private String receiveTime;
    private String error;
    private String errorInfo;
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
   public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getSentTime() {
        return this.sentTime;
    }

    public void setSentTime(String sentTime) {
        this.sentTime = sentTime;
        /*
        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:MM:SS");
        this.sentTime = dtf.parseDateTime(sentTime);
         */
    }

    public String getReceiveTime() {
        return this.receiveTime;
    }

    public void setReceiveTime(String receiveTime) {
        this.receiveTime = receiveTime;
        /*
        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:MM:SS");
        this.receiveTime = dtf.parseDateTime(receiveTime);
         */
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }
}
