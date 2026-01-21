package com.evoucher.adminapi.cms.dao.models;

import com.evoucher.adminapi.common.models.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "tb_stock_management_history")
public class Stock /*extends BaseEntity*/ {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "supplier_id")
    private String supplierId;

    @Column(name = "supplier_nm")
    private String supplierName;

    @Column(name = "brand_id")
    private String brandId;

    @Column(name = "brand_nm")
    private String brandName;

    @Column(name = "goods_id")
    private Integer goodsId;

    @Column(name = "goods_nm")
    private String goodsName;

    @Column(name = "expire_time")
    private Date expireTime;

    @Column(name = "remain_days")
    private Integer remainDays;

    @Column(name = "total_quantity")
    private Integer totalQuantity;

    @Column(name = "total_amount")
    private Integer totalAmount;

    @Column(name = "inserted_at")
    private Date insertedAt;

    @Column(name = "diff_1d")
    private Integer diff1d;

    @Column(name = "diff_7d")
    private Integer diff7d;

    @Column(name = "diff_30d")
    private Integer diff30d;
}