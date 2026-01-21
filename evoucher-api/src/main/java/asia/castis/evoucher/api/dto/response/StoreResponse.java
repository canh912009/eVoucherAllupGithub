package asia.castis.evoucher.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class StoreResponse {
    private String storeId;
    private String storeName;
    private String storeImagePath;
    private String storeImageName;
    private String brandId;
    private String validYN;
    private String fullAddress;
}
