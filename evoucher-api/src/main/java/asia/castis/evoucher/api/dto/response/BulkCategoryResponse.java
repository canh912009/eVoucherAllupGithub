package asia.castis.evoucher.api.dto.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class BulkCategoryResponse {
    private Long bulkCtgrId;
    private Integer goodsId;
    private CategoryResponse category;
    private int displayIdx;
    private String validYn;
}
