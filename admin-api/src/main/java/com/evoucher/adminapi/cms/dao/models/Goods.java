package com.evoucher.adminapi.cms.dao.models;

import com.evoucher.adminapi.admin.enums.StoreQueryType;
import com.evoucher.adminapi.cms.service.models.StoreDTO;
import com.evoucher.adminapi.cms.service.models.request.GoodsRequest;
import com.evoucher.adminapi.cms.service.models.vnptEPay.response.GiftSearchWithDateDTO;
import com.evoucher.adminapi.common.enums.*;
import com.evoucher.adminapi.common.models.BaseEntity;
import com.evoucher.adminapi.common.utils.Constant;
import com.evoucher.adminapi.common.utils.DateUtils;
import com.evoucher.adminapi.good.service.GoodServiceFactory;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@SqlResultSetMapping(
        name = "vnpt_search_gift_mapping",
        classes = @ConstructorResult(
                targetClass = GiftSearchWithDateDTO.class,
                columns = {
                        @ColumnResult(name = "id", type = Long.class),
                        @ColumnResult(name = "giftTitle", type = String.class),
                        @ColumnResult(name = "brandName", type = String.class),
                        @ColumnResult(name = "price", type = Integer.class),
                        @ColumnResult(name = "quantity", type = Integer.class),
                        @ColumnResult(name = "createDate", type = Date.class)
                }
        )
)

@SqlResultSetMapping(
        name = "GoodCategoryMapping",
        entities = {
                @EntityResult(entityClass = Goods.class, fields = {
                        @FieldResult(name = "id", column = "goods_id"),
                        @FieldResult(name = "goodsName", column = "goods_nm"),
                        @FieldResult(name = "supplierId", column = "supplier_id"),
                        @FieldResult(name = "brandId", column = "brand_id"),
                        @FieldResult(name = "goodsStatusCode", column = "goods_status_cd"),
                        @FieldResult(name = "supplierGoodsId", column = "supplier_goods_id"),
                        @FieldResult(name = "supplierContractId", column = "supplier_contract_id"),
                        @FieldResult(name = "listPrice", column = "list_price"),
                        @FieldResult(name = "sellPrice", column = "sell_price"),
                        @FieldResult(name = "supplyDiscountRate", column = "supply_dc_rate"),
                        @FieldResult(name = "supplyDiscountAmount", column = "supply_dc_amount"),
                        @FieldResult(name = "supplyCommissionRate", column = "supply_commission_rate"),
                        @FieldResult(name = "vatIncludeYn", column = "vat_inc_yn"),
                        @FieldResult(name = "settlementMethodCode", column = "settlement_method_cd"),
                        @FieldResult(name = "goodsDescription", column = "goods_desc"),
                        @FieldResult(name = "useInfo", column = "use_info"),
                        @FieldResult(name = "goodsImgPath", column = "goods_img_path"),
                        @FieldResult(name = "goodsImgName", column = "goods_img_nm"),
                        @FieldResult(name = "startDate", column = "strt_dt"),
                        @FieldResult(name = "endDate", column = "end_dt"),
                        @FieldResult(name = "validYn", column = "valid_yn"),
                        @FieldResult(name = "exceptStoreIds", column = "except_store_ids"),
                        @FieldResult(name = "goodsType", column = "goods_type"),
                        @FieldResult(name = "periodType", column = "period_type"),
                        @FieldResult(name = "periodTerm", column = "period_term"),
                        @FieldResult(name = "periodExpireDate", column = "period_expire_date"),
                        @FieldResult(name = "system", column = "system"),
                        @FieldResult(name = "usageCount", column = "usage_count"),
                        @FieldResult(name = "regId", column = "REG_ID"),
                        @FieldResult(name = "regDt", column = "REG_DT"),
                        @FieldResult(name = "updtId", column = "UPDT_ID"),
                        @FieldResult(name = "updtDt", column = "UPDT_DT"),

                        @FieldResult(name = "includeStoreIds", column = "include_store_ids"),
                        @FieldResult(name = "storeQueryType", column = "store_query_type")
                })
        },
        columns = {
                @ColumnResult(name = "categoryCodes", type = String.class),
                @ColumnResult(name = "brandIds", type = String.class)
        }
)

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "tb_goods")
public class Goods extends BaseEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "goods_id")
    private Integer id;

    @Column(name = "goods_nm")
    private String goodsName;

    @Column(name = "supplier_id")
    private String supplierId;

    @Column(name = "brand_id")
    private String brandId;

    @Column(name = "goods_status_cd")
    private String goodsStatusCode;

    @Column(name = "supplier_goods_id")
    private String supplierGoodsId;

    @Column(name = "supplier_contract_id")
    private Integer supplierContractId;

    @Column(name = "list_price")
    private Double listPrice;

    @Column(name = "sell_price")
    private Double sellPrice;

    @Column(name = "supply_dc_rate")
    private Double supplyDiscountRate;

    @Column(name = "supply_dc_amount")
    private Double supplyDiscountAmount;

    @Column(name = "supply_commission_rate")
    private Double supplyCommissionRate;

    @Column(name = "vat_inc_yn")
    @Enumerated(EnumType.STRING)
    private EnumValidYn vatIncludeYn;

    @Column(name = "settlement_method_cd")
    @Enumerated(EnumType.STRING)
    private SettlementMethodCode settlementMethodCode;

    @Column(name = "goods_desc")
    private String goodsDescription;

    @Column(name = "use_info")
    private String useInfo;

    @Column(name = "goods_img_path")
    private String goodsImgPath;

    @Column(name = "goods_img_nm")
    private String goodsImgName;

    @Column(name = "strt_dt")
    private Date startDate;

    @Column(name = "end_dt")
    private Date endDate;

    @Column(name = "valid_yn")
    @Enumerated(EnumType.STRING)
    private EnumValidYn validYn;

    @Column(name = "except_store_ids")
    private String exceptStoreIds;

    @Column(name = "goods_type")
    @Enumerated(EnumType.STRING)
    private GoodsType goodsType;

    @Column(name = "period_type")
    @Enumerated(EnumType.STRING)
    private PeriodType periodType;

    @Column(name = "period_term")
    private Double periodTerm;

    @Column(name = "period_expire_date")
    private String periodExpireDate;

    @Column(name = "system")
    @Enumerated(EnumType.STRING)
    private SystemType system;

    @Column(name = "usage_count")
    private Integer usageCount;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "parentGoods", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonManagedReference
    private List<BulkCategory> bulkCategories;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "vnptParentGood", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonManagedReference
    private List<VnptGood> vnptGoods;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "xpayParentGood", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonManagedReference
    private List<XpayGood> xpayGoods;

    @Column(name = "include_store_ids")
    private String includeStoreIds;

    @Column(name = "store_query_type")
    @Enumerated(EnumType.STRING)
    private StoreQueryType storeQueryType;

    public Goods(GoodsRequest goodsRequest) {
        this.supplierGoodsId = goodsRequest.getSupplierGoodsId();
        this.supplierContractId = goodsRequest.getSupplierContractId();
        this.supplierId = goodsRequest.getSupplierId();
        this.brandId = goodsRequest.getBrandId();
        this.goodsName = goodsRequest.getGoodsName();
        //for internal, choice, bulk
        if (GoodServiceFactory.DIRECT_TYPE_HAS_PERIOD.contains(goodsRequest.getSystem())
                || GoodServiceFactory.PARENT_GOOD_SYSTEM.contains(goodsRequest.getSystem())) {
            this.periodType = goodsRequest.getPeriodType();
            this.periodTerm = goodsRequest.getPeriodTerm();
            this.periodExpireDate = DateUtils.formatDateToString(goodsRequest.getPeriodExpireDate(), Constant.Common.COMMON_DATE_FORMAT);
        }
        this.startDate = goodsRequest.getStartDate();
        this.endDate = DateUtils.atEndOfDay(goodsRequest.getEndDate());
        this.goodsDescription = goodsRequest.getGoodsDescription();
        this.goodsType = goodsRequest.getGoodsType();
        this.goodsImgName = goodsRequest.getGoodsImgName();
        this.goodsImgPath = goodsRequest.getGoodsImgPath();
        this.validYn = goodsRequest.getValidYn();
        this.listPrice = goodsRequest.getListPrice();
        this.sellPrice = goodsRequest.getSellPrice();
        this.settlementMethodCode = goodsRequest.getSettlementMethodCode();
        this.supplyDiscountAmount = goodsRequest.getSupplyDiscountAmount();
        this.supplyCommissionRate = goodsRequest.getSupplyCommissionRate();
        this.vatIncludeYn = goodsRequest.getVatIncludeYn();
        this.system = goodsRequest.getSystem();

        List<StoreDTO> exceptStoreDTOS = goodsRequest.getExceptStores();
        List<String> listExceptStoreId = exceptStoreDTOS.stream().map(StoreDTO::getId).collect(Collectors.toList());
        this.exceptStoreIds = Constant.gson.toJson(listExceptStoreId);
        this.usageCount = goodsRequest.getUsageCount();
    }
}
