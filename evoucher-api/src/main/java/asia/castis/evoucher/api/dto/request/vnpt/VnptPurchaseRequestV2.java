package asia.castis.evoucher.api.dto.request.vnpt;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class VnptPurchaseRequestV2 extends VnptPurchaseRequest {
    @NotNull(message = "OTP can not be null")
    @NotEmpty(message = "OTP can not be empty")
    private String otp;
}
