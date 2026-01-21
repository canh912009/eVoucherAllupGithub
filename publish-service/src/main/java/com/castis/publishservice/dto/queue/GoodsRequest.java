package com.castis.publishservice.dto.queue;

import com.castis.publishservice.utils.enum_template.GoodType;
import com.castis.publishservice.utils.enum_template.SystemType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;

@Data
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GoodsRequest {
    Long id;
    Set<CategoryRequest> categories;
    @JsonIgnore
    String brandId;
    BrandRequest brand;
    String name;
    Double listPrice;
    Double sellPrice;
    Double supplyDiscountRate;
    Double supplyDiscountAmount;
    Double supplyFeeRate;
    boolean isSupplyVatInclude;
    String supplyCalculateMethodCode;
    Double sellDiscountRate;
    Double sellDiscountCost;
    Double sellFeeRate;
    String sellCalculateMethod;
    Double sendCost;
    String startDate;
    String endDate;
    boolean isValid;
    String sticker;
    String imagePath;
    String imageName;
    String exceptStoreIds;
    String periodType;
    Double periodTerm;
    String periodExpireDate;
    String description;
    SystemType system;
    GoodType type;
    List<GoodsRequest> choices;
    Integer remainingCount;
}

