package asia.castis.evoucher.api.dto.response;

import asia.castis.evoucher.api.dto.response.vnpt.VnptResponse;
import lombok.*;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoodsResponse {
    private long id;
    private String name;
    private double listPrice;
    private double sellPrice;
    private boolean isValid;
    private String imagePath;
    private String imageName;
    private String description;
    private String system;
    private String type;
    private int remainingCount;
    private String startDate;
    private String endDate;

    private Integer usageCount;

    private List<CategoryResponse> categories;
    private BrandResponse brand;
    private List<GoodsResponse> choices;
    private List<BulkCategoryResponse> bulkCategories;
    private VnptResponse vnpt;
}
