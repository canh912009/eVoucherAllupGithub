package asia.castis.evoucher.api.dto.otpservice.generate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OtpGenerateTTLRequest {
    private String key;
    private Long ttl;
    private Integer length;
}
