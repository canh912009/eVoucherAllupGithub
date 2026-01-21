package asia.castis.evoucher.push.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class IncomingPublish {
    private Long publishId;
    private Long campaignId;
    private String campaignName;
    private String smsType;
    private String brandName;
    private String templateId;
    private int totalCount;
    private List<IncomingPublishMessage> publishMessageList;
}
