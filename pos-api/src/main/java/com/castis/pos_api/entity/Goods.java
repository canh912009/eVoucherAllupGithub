package com.castis.pos_api.entity;
import com.castis.pos_api.enum_constant.EnumValidYn;
import com.castis.pos_api.enum_constant.SystemType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.sql.Date;

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

    @JsonBackReference
    @OneToOne
    @JoinColumn(name = "brand_id", referencedColumnName = "brand_id", updatable = false, insertable = false)
    Brand brand;

    @JsonBackReference
    @OneToOne
    @JoinColumn(name = "supplier_id", referencedColumnName = "supplier_id", updatable = false, insertable = false)
    Supplier supplier;

    @Column(name = "goods_status_cd")
    private String goodsStatusCd;


    @Column(name = "list_price")
    private Double listPrice;

    @Column(name = "sell_price")
    private Double sellPrice;

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

    @Column(name = "VALID_YN")
    @Enumerated(EnumType.STRING)
    private EnumValidYn validYn;

    @Column(name = "period_type")
    private String periodType;

    @Column(name = "period_term")
    private Double periodTerm;

    @Column(name = "period_expire_date")
    private String periodExpireDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "system")
    private SystemType system;

    @Column(name = "goods_type")
    private String type;
}

