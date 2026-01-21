package asia.castis.evoucher.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BulkGoodsResponse {
    private Long bulkGoodsId;
    private GoodsResponse goods;
    private int displayIdx;
    private String validYn;
}
