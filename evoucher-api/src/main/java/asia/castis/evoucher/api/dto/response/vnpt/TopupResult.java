package asia.castis.evoucher.api.dto.response.vnpt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class TopupResult {
    private String providerCode;
    private String targetPhoneNumber;
    private Long faceValue;
    private String requestTime;
}
