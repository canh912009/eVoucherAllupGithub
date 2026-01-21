package asia.castis.evoucherservicefe.disablevoucher.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class VoucherDisableProcessRequest {
    private String ev;
    private Integer voucherDisableHistoryId;
}