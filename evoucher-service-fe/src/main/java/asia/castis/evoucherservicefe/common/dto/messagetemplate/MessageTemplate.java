package asia.castis.evoucherservicefe.common.dto.messagetemplate;

import lombok.Data;

import java.util.List;

@Data
public class MessageTemplate {
    private String template;
    private String language;
    private List<MsgTemplateData> data;
}
