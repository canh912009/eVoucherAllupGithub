package asia.castis.evoucher.api.dto.request;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@ToString
public class SerialNumberRequest {
    @NotNull(message = "voucherId is required")
    @NotEmpty(message = "voucherId cannot be empty")
    private String voucherId;
    @NotNull(message = "serialNumber is required")
    @NotEmpty(message = "serialNumber cannot be empty")
    private String serialNumber;
}