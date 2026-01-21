package asia.castis.evoucher.api.dto.response;
import lombok.*;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BrandResponse {
    private String id;
    private String name;
    private String description;
    private String imgUrl;
    private String logoUrl;
    private String logoNm;
    private boolean isPosLink;
    private String system;

    private SupplierResponse supplier;
}
