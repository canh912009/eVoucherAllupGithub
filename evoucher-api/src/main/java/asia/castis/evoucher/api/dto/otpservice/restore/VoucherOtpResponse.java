package asia.castis.evoucher.api.dto.otpservice.restore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VoucherOtpResponse {
    private String error;
    private String errorType;
    private String path;
    private Data data;
}

