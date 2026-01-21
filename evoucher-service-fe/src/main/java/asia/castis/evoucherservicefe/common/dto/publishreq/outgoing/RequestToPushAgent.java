package asia.castis.evoucherservicefe.common.dto.publishreq.outgoing;

import asia.castis.evoucherservicefe.common.dto.QueueMessage;
import lombok.*;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestToPushAgent implements QueueMessage {
    private Long publishId;
    private Long campaignId;
    private String campaignName;
    private String smsType;
    private String brandName;
    private String templateId;
    private int totalCount;
    private List<PublishMessage> publishMessageList;
}
