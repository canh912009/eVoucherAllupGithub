package asia.castis.evoucher.push.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SmsMessageResponse {
    private String MessageId;
    private int PartnerId;
    private String BrandName;
    private String Telco;
    private String Phone;
    private String Message;
    private String TransID;
}
