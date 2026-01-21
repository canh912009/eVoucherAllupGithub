package asia.castis.evoucher.api.dto.otpservice.generate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OtpData {
    private String otp;
    private Long regDt;
    private String expireDt;
    private String expireDtStr;
}