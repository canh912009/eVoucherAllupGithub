package asia.castis.evoucher.api.dto.response.vnpt;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class VnptProviderResponse {
    private String providerCd;
    private String providerNm;
    private String providerType;
    private String validYn;
    private String allowedCardFaces;
    private String allowedActions;
}
