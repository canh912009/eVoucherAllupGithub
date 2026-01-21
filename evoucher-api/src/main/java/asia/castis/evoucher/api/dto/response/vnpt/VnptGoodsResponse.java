package asia.castis.evoucher.api.dto.response.vnpt;

import asia.castis.evoucher.api.common.enums.EnumValidYn;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class VnptGoodsResponse {
    private Long id;
    private Integer parentGoodsId;
    private String providerCode;
    private VnptProviderResponse vnptProvider;
    private Double faceValue;
    private String validYn;
    private String description;
}
