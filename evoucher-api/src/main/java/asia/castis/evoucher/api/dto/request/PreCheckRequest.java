package asia.castis.evoucher.api.dto.request;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@ToString
@Data
public class PreCheckRequest {
    @NotNull(message = "voucherId is required")
    @NotEmpty(message = "voucherId cannot be empty")
    private String voucherId;
}
