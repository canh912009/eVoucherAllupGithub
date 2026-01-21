package asia.castis.evoucher.push.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SendingPublishDetail {
    private String messageId;
    private Long publishDetailId;
    private String smsType;
    //private String phoneNumber;
    private String message;
    private String result;
    // 'yyyy-MM-dd hh:mm:ss'
    // time receive response of delive message
    //@JsonDeserialize(using = CustomDateTimeDeserializer.class)
    private String receivedTime;
    // 'yyyy-MM-dd hh:mm:ss'
    // time sending message
    //@JsonDeserialize(using = CustomDateTimeDeserializer.class)
    private String sendDatetime;
    // 'yyyy-MM-dd hh:mm:ss'
    // time receive response when sending message

    //@JsonDeserialize(using = CustomDateTimeDeserializer.class)
    private String sendResultDatetime;
}
