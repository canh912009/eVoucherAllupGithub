package asia.castis.evoucherservicefe.publishrequest.dto;

import asia.castis.evoucherservicefe.common.dto.QueueMessage;
import lombok.Data;

@Data
public class OtpSmsQueueMsg implements QueueMessage {
    private String brandName;
    private String message;
    private String phoneNumber;
}
