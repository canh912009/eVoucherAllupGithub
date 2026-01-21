package asia.castis.evoucher.api.dto.response.vnpt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class CardCodeResult {
    private String providerCode;
    private String code;
    private String serialNo;
    private Long faceValue;
    private String requestTime;
}
