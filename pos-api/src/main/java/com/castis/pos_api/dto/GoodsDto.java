package com.castis.pos_api.dto;

import com.castis.pos_api.enum_constant.SystemType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsDto {
    private Long id;

    private String goodsName;

    private BrandDto brand;
    private SupplierDto supplier;

    private String goodsStatusCd;

    private Double listPrice;

    private Double sellPrice;

    private String vatIncludeYn;

    private String settlementMethodCode;

    private String goodsDescription;

    private String useInfo;

    private String goodsImgPath;

    private String goodsImgName;

    private Date startDate;

    private Date endDate;

    private String validYn;
    private String periodType;

    private Double periodTerm;

    private String periodExpireDate;
    private SystemType system;
    private String type;
}
