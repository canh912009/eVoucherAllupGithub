package asia.castis.evoucher.push.model;

import lombok.Data;

@Data
public class IncomingPublishMessage {
    private Long publishDetailId;
    private String receiverMobileNumber;
    private String templateId;
    private ZaloTemplateDataOld templateData;
    private String message;
    private String imageUrl;
}
