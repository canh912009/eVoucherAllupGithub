package com.evoucher.adminapi.cms.dao.models;

import com.evoucher.adminapi.common.enums.EnumValidYn;
import com.evoucher.adminapi.common.models.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tb_bulk_goods")
@Data
public class BulkGoods extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bulk_goods_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="bulk_brand_id")
    @JsonBackReference
    private BulkBrand bulkBrand;

    @OneToOne
    @JoinColumn(name = "goods_id", referencedColumnName = "goods_id", insertable = false, updatable = false)
    @JsonManagedReference
    private Goods goods;

    @Column(name = "goods_id", nullable = false)
    private Long goodsId;

    @Column(name = "display_idx", nullable = false)
    private int displayIndex;

    @Column(name = "valid_yn", nullable = false, length = 1)
    @Enumerated(EnumType.STRING)
    private EnumValidYn validYn;
}
