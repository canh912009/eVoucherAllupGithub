package asia.castis.evoucherservicefe.voucherhandler.dto;

import asia.castis.evoucherservicefe.common.dto.QueueMessage;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@ToString
@Data
public class ActivateRequest implements QueueMessage {
    @NotNull
    private String phoneNumber;
    @NotNull
    private String userName;
    @NotNull
    private String serialNumber;
    private String ev;
}
