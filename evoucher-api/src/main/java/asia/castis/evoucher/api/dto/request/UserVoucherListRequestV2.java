package asia.castis.evoucher.api.dto.request;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@ToString
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserVoucherListRequestV2 extends UserVoucherListRequest {
    @NotEmpty(message = "otp is required")
    private String otp;
}
