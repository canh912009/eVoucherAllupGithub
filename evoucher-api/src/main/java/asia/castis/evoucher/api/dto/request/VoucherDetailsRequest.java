package asia.castis.evoucher.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class VoucherDetailsRequest {
    @NotNull(message = "Voucher Id is required")
    @NotEmpty(message = "Voucher Id cannot be empty")
    private String voucherId;
    private String otp;
}
