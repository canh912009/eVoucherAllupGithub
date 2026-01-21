package asia.castis.evoucher.push.model;

import lombok.Data;
import lombok.ToString;


@Data
@ToString
public class TemplateData {
    private String customerName;
    private String sender;
    private String message;
    private String productName;
    private String expireDate;
    private String cta1;
    private String cta2;
}
