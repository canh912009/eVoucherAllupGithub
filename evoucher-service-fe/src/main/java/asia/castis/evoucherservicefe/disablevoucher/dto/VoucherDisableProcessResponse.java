package asia.castis.evoucherservicefe.disablevoucher.dto;

import asia.castis.evoucherservicefe.common.dto.QueueMessage;
import asia.castis.evoucherservicefe.common.enums.EnumDisableVoucherResult;
import asia.castis.evoucherservicefe.common.enums.EnumVoucherStatus;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class VoucherDisableProcessResponse implements QueueMessage {
    private String ev;
    private Integer voucherDisableHistoryId;
    private EnumVoucherStatus frontEndPreviousStatusCode;
    private EnumDisableVoucherResult frontEndUpdateResult;
}