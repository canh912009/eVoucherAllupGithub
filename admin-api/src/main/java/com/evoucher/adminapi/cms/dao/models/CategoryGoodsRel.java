package com.evoucher.adminapi.cms.dao.models;

import com.evoucher.adminapi.common.models.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "tb_category_goods_rel")
@IdClass(CategoryGoodsId.class)
public class CategoryGoodsRel extends BaseEntity {
    @Id
    @Column(name = "ctgr_cd")
    private String categoryCode;

    @Id
    @Column(name = "goods_id")
    private Integer goodsId;
}
