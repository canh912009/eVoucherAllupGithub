package asia.castis.evoucherservicefe.common.dto.messagetemplate;

import lombok.Data;

@Data
public class MsgTemplateData {
    private EnumTemplateKey key;
    private EnumTemplateType type;
    private String defaultValue;
    private int maxLength;
    private boolean encrypted;
    private String value;
}
