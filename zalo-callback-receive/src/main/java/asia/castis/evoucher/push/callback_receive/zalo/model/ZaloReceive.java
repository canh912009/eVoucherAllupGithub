package asia.castis.evoucher.push.callback_receive.zalo.model;

import lombok.Data;

@Data
public class ZaloReceive {
    private String msg_id;
    private String type;
    private int status;
    private String sent_time;
    private String received_time;
    private String error;
    private String error_info;
    private String error_failover;
    private String failover_sent_time;
}
