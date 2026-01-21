package asia.castis.evoucher.api.dto.request.activate;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@ToString
@Data
public class ActivateRequestV2 extends ActivateRequest {
    @NotNull(message = "OTP code can not be null")
    @NotEmpty(message = "OTP code can not be empty")
    private String otp;

    @NotEmpty(message = "Voucher Id can not be empty")
    @NotNull(message = "Voucher Id can not be null")
    String voucherId;
}
