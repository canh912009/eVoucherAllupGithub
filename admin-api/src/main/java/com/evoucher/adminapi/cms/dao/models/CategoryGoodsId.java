package com.evoucher.adminapi.cms.dao.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryGoodsId implements Serializable {

    @Column(name = "CTGR_CD")
    private String categoryCode;

    @Column(name = "GOODS_ID")
    private Integer goodsId;
}
