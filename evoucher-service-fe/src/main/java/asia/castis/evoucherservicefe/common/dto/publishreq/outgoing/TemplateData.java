package asia.castis.evoucherservicefe.common.dto.publishreq.outgoing;

import lombok.Data;
import lombok.ToString;


@Data
@ToString
public class TemplateData {
    private String customerName;
    private String sender;
    private String cta2;
    private String message;
    private String cta1;
    private String productName;
    private String expireDate;
    private String transferMessage;

}
