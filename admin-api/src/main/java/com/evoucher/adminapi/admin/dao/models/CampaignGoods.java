package com.evoucher.adminapi.admin.dao.models;

import com.evoucher.adminapi.common.enums.SettlementMethodCode;
import com.evoucher.adminapi.common.models.BaseEntity;
import javax.persistence.*;

import com.evoucher.adminapi.common.enums.EnumValidYn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_CAMPAIGN_GOODS_REL")
@IdClass(CampaignGoodsId.class)
public class CampaignGoods extends BaseEntity {

    @Id
    @Column(name = "CAMPAIGN_ID")
    private Integer campaignId;

    @Id
    @Column(name = "GOODS_ID")
    private Integer goodsId;

    @Column(name = "SUPPLY_DC_RATE")
    private Double supplyDiscountRate;

    @Column(name = "SUPPLY_DC_AMOUNT")
    private Double supplyDiscountAmount;

    @Column(name = "SUPPLY_COMMISSION_RATE")
    private Double supplyCommissionRate;

    @Column(name = "VAT_INC_YN")
    @Enumerated(EnumType.STRING)
    private EnumValidYn vatIncludeYn;

    @Column(name = "SETTLEMENT_METHOD_CD")
    @Enumerated(EnumType.STRING)
    private SettlementMethodCode settlementMethodCode;

    @Column(name = "SEND_COST")
    private Double sendCost;

    @Column(name = "VALID_YN")
    @Enumerated(EnumType.STRING)
    private EnumValidYn validYn;
}
