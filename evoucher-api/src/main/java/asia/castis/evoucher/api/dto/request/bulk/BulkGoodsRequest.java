package asia.castis.evoucher.api.dto.request.bulk;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@Builder
public class BulkGoodsRequest {
    private long bulkBrandId;
    private String name;
    private int pageSize;
    private int pageNum;
}
