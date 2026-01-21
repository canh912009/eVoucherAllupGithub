package asia.castis.evoucherservicefe.common.dto.createmessageresult;

import asia.castis.evoucherservicefe.common.enums.EnumPublishStatus;
import asia.castis.evoucherservicefe.common.dto.QueueMessage;
import lombok.*;

import java.util.List;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OutgoingPublishResult implements QueueMessage {
    private Long publishId;
    private EnumPublishStatus publishStatus;
    List<DetailResult> detailStatus;
    private ResendResult resend;
}
