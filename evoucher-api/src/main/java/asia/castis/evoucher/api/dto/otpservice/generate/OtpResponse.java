package asia.castis.evoucher.api.dto.otpservice.generate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OtpResponse {
    private String error;
    private String errorType;
    private String path;
    private OtpData data;
}
