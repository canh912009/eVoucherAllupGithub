package com.evoucher.adminapi.cms.service.models.request;

import com.evoucher.adminapi.cms.service.models.*;
import com.evoucher.adminapi.common.enums.*;
import com.evoucher.adminapi.serializer.EndDateWithoutTimeDeserializer;
import com.evoucher.adminapi.serializer.StartDateWithoutTimeDeserializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GoodsRequest {
    @NotBlank(message = "Product name is blank!")
    private String goodsName;
//    @NotBlank(message = "Product code is empty!")
    private String supplierGoodsId;
    @NotBlank(message = "Supplier is blank!")
    private String supplierId;
    private String goodsDescription;
    @NotNull(message = "Supplier contract is null!")
    private Integer supplierContractId;
    @NotNull(message = "Goods type is null!")
    private GoodsType goodsType;
    @NotBlank(message = "Brand is blank!")
    private String brandId;
    private PeriodType periodType;
    @PositiveOrZero(message = "Period term must be zero or positive")
    private Double periodTerm;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date periodExpireDate;
    @NotNull(message = "Goods image name is empty!")
    private String goodsImgName;
    @NotNull(message = "Goods image path is empty!")
    private String goodsImgPath;
    @NotNull(message = "Active is null!")
    private EnumValidYn validYn;
//    @NotNull(message = "List Price is null!")
//    @PositiveOrZero(message = "List price must be zero or positive")
    private Double listPrice;
//    @NotNull(message = "Selling Price is null!")
//    @PositiveOrZero(message = "Selling price must be zero or positive")
    private Double sellPrice;
    @NotNull(message = "Settlement method is null!")
    private SettlementMethodCode settlementMethodCode;
    @NotNull(message = "Discount amount is null!")
    @PositiveOrZero(message = "Discount amount must be zero or positive")
    private Double supplyDiscountAmount;
    @NotNull(message = "Commission rate is null!")
    @PositiveOrZero(message = "Commission rate must be zero or positive")
    private Double supplyCommissionRate;
    @NotNull(message = "Including VAT is null!")
    private EnumValidYn vatIncludeYn;
    @NotNull(message = "Start date is empty!")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = StartDateWithoutTimeDeserializer.class)
    private Date startDate;
    @NotNull(message = "End date is empty!")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = EndDateWithoutTimeDeserializer.class)
    private Date endDate;
    @NotNull(message = "System type is empty!")
    private SystemType system;

    private Integer usageCount;
//    @NotEmpty(message = "Category is empty!")
    private List<CategoryDTO> categories;
    private List<StoreDTO> exceptStores;
    private LinkedList<GoodsDTO> listGoodsChoice;
    LinkedList<BulkCategoryDTO> bulkCategories;
    List<VnptGoodDto> vnptGoods;
    List<XpayGoodDto> xpayGoods;
}
