package com.castis.publishservice.entity;
import com.castis.publishservice.utils.enum_template.GoodType;
import com.castis.publishservice.utils.enum_template.SystemType;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "tb_goods")
@FieldDefaults(level= AccessLevel.PRIVATE)
@Builder
public class Goods {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "goods_id")
    private Long id;

    @Column(name = "goods_nm")
    private String goodsName;

    @Column(name = "supplier_id")
    private String supplierId;

    @Column(name = "brand_id")
    private String brandId;

    @Column(name = "goods_status_cd")
    private String goodsStatusCd;

    @Column(name = "supplier_goods_id")
    private String supplierGoodsId;

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
    private String vatIncludeYn;

    @Column(name = "settlement_method_cd")
    private String settlementMethodCode;

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
    private String validYn;

    @Column(name = "except_store_ids")
    private String exceptStoreIds;
    @CreatedBy
    @Column(name = "REG_ID", updatable = false)
    private String regId;

    @CreatedDate
    @Column(name = "REG_DT", updatable = false)
    private java.util.Date regDt;

    @LastModifiedBy
    @Column(name = "UPDT_ID")
    private String updtId;

    @LastModifiedDate
    @Column(name = "UPDT_DT")
    private java.util.Date updtDt;
    @ManyToMany
    @JoinTable(
            name = "tb_category_goods_rel",
            joinColumns = @JoinColumn(name = "goods_id"),
            inverseJoinColumns = @JoinColumn(name = "ctgr_cd"))
    private Set<Category> categories;

    @Column(name = "period_type")
    private String periodType;

    @Column(name = "period_term")
    private Double periodTerm;

    @Column(name = "period_expire_date")
    private String periodExpireDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "system")
    private SystemType system;

    @Enumerated(EnumType.STRING)
    @Column(name = "goods_type")
    private GoodType type;
}

