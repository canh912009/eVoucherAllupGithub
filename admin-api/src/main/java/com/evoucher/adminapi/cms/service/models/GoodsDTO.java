package com.evoucher.adminapi.cms.service.models;

import com.evoucher.adminapi.admin.enums.StoreQueryType;
import com.evoucher.adminapi.cms.dao.models.BulkCategory;
import com.evoucher.adminapi.common.enums.EnumValidYn;
import com.evoucher.adminapi.common.enums.PeriodType;
import com.evoucher.adminapi.common.enums.SystemType;
import com.evoucher.adminapi.common.models.BaseDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class GoodsDTO extends BaseDTO {
    private Integer id;
    private String goodsName;
    private SupplierDTO supplier;
    private BrandDTO brand;
    private String goodsStatusCode;
    private String supplierGoodsId;
    private Integer supplierContractId;
    private Double listPrice;
    private Double sellPrice;
    private Double supplyDiscountRate;
    private Double supplyDiscountAmount;
    private Double supplyCommissionRate;
    private String vatIncludeYn;
    private String settlementMethodCode;
    private String goodsDescription;
    private String useInfo;
    private String goodsImgPath;
    private String goodsImgName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;
    private EnumValidYn validYn;
    private List<StoreDTO> exceptStores;
    private String goodsType;
    private PeriodType periodType;
    private Double periodTerm;
    private String periodExpireDate;
    private SystemType system;

    private Integer usageCount;
    private List<CategoryDTO> categories;
    private LinkedList<GoodsDTO> listGoodsChoice;
    private List<BulkCategoryDTO> bulkCategories;
    private List<VnptGoodDto> vnptGoods;
    private List<XpayGoodDto> xpayGoods;
    private String includeStoreIds;
    private StoreQueryType storeQueryType;

    List<String> includedStoresIds = new ArrayList<>();
}
