package asia.castis.evoucher.push.model;

import java.util.List;
import lombok.Data;
import lombok.ToString;
/*
* Use as data to Rabbit MQ, Publish Service listen this queue and pull this data
*/
@Data
@ToString
public class SendingPublish {
    Long publishId;
    String publishStatus;
    List<SendingPublishDetail> detailStatus;
}
