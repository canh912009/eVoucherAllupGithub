package com.evoucher.evoucherbe.entity;

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
public class CampaignGoodsId implements Serializable {

    @Column(name = "CAMPAIGN_ID")
    private Integer campaignId;

    @Column(name = "GOODS_ID")
    private Integer goodsId;
}
