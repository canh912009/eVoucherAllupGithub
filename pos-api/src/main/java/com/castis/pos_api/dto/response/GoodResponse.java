package com.castis.pos_api.dto.response;

import com.castis.pos_api.enum_constant.EnumValidYn;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GoodResponse {
    Long id;
    String goodsName;
    String goodsDescription;
    String goodsImgUrl;
    BrandResponse brand;
    SupplierResponse supplier;
    EnumValidYn validYn;
    Double listPrice;
    Double salesPrice;
}
