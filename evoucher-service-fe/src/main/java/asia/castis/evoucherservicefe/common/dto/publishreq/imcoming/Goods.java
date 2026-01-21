package asia.castis.evoucherservicefe.common.dto.publishreq.imcoming;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class Goods {
    private Long id;
    private List<Category> categories;
    private Brand brand;
    private String name;
    private Double listPrice;
    private Double sellPrice;
    private Double supplyDiscountAmount;
    private Double supplyDiscountRate;
    private Double supplyFeeRate;
    private boolean isSupplyVatInclude;
    private String supplyCalculateMethodCode;
    private Double sellDiscountRate;
    private Double sellDiscountCost;
    private Double sellFeeRate;
    private String sellCalculateMethod;
    private Double sendCost;
    private String startDate;
    private String endDate;
    private boolean isValid;
    private String sticker;
    private String exceptStoreIds;
    private String imagePath;
    private String imageName;
    private String periodType;
    private Double periodTerm;
    private String periodExpireDate;
    private String description;
    private String system;
    private Integer remainingCount;
    String type;
    List<Goods> choices;
}
