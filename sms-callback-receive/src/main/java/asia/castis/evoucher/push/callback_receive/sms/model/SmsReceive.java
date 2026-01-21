package asia.castis.evoucher.push.callback_receive.sms.model;

import lombok.Data;

@Data
public class SmsReceive {
    private int smsid;
    private int Status;
    private String Telco;
    private String Error;
    private int mt_count;

}
