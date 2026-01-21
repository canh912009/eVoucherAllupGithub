package asia.castis.evoucher.api.entity;

import asia.castis.evoucher.api.common.enums.*;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

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

    @Column(name = "include_store_ids")
    private String includeStoreIds;

    @Column(name = "store_query_type")
    @Enumerated(EnumType.STRING)
    private StoreQueryType storeQueryType;

    @Column(name = "goods_type")
    @Enumerated(EnumType.STRING)
    private VoucherTypeCode goodsType;

    @Column(name = "period_type")
    @Enumerated(EnumType.STRING)
    private PeriodType periodType;

    @Column(name = "period_term")
    private Integer periodTerm;

    @Column(name = "period_expire_date")
    private String periodExpireDate;

    @Column(name = "system")
    @Enumerated(EnumType.STRING)
    private SystemType system;

    @Column(name = "usage_count")
    private Integer usageCount;
}
