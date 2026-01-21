package asia.castis.evoucherservicefe.common.dto.publishreq.outgoing;

import lombok.Data;

@Data
public class PublishMessage {
    private Long publishDetailId;
    private String receiverMobileNumber;
    private String templateId;
    private TemplateData templateData;
    private String message;
    private String imageUrl;
}
