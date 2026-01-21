package asia.castis.evoucherservicefe.common.dto.publishreq.imcoming;

import asia.castis.evoucherservicefe.common.dto.messagetemplate.MessageTemplate;
import lombok.Data;
import lombok.ToString;


@Data
@ToString
public class Campaign {
    private Long id;
    private String name;
    private String startDate;
    private String endDate;
    private MessageTemplate messageTemplate;
}
