package asia.castis.evoucher.api.dto.response;

import lombok.*;

import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class BulkBrandResponse {
    private Long bulkBrandId;
    private Long bulkCtgrId;
    private BrandResponse brand;
    private int displayIdx;
    private String validYn;
}
