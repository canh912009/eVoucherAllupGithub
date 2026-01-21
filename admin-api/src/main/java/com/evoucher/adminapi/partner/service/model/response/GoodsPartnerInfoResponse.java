package com.evoucher.adminapi.partner.service.model.response;


import com.evoucher.adminapi.common.enums.EnumValidYn;
import com.evoucher.adminapi.common.enums.GoodsType;
import com.evoucher.adminapi.common.enums.PeriodType;
import com.evoucher.adminapi.common.enums.SettlementMethodCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsPartnerInfoResponse {
    private Integer id;
    private String goodsName;
    private String goodsDescription;
    private GoodsType goodsType;
    private String supplierGoodsId;
    private SupplierPartnerResponse supplier;
    private BrandPartnerResponse brandPartnerResponse;
    private PeriodType periodType;
    private Double periodTerm;
    private String periodExpireDate;
    private String goodsImgName;
    private String goodsImgPath;
    private EnumValidYn validYn;
    private Double listPrice;
    private Double sellPrice;
    private SettlementMethodCode settlementMethodCode;
    private EnumValidYn vatIncludeYn;
    private Date startDate;
    private Date endDate;
    private CategoryPartnerResponse categories;
}
