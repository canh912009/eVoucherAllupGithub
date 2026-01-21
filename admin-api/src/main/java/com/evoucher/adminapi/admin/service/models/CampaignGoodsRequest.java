package com.evoucher.adminapi.admin.service.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CampaignGoodsRequest {
    private Integer campaignId;
    private Integer goodsId;
    private Double sellDiscountRate;
    private Double sellDiscountAmount;
    private Double sellFeeRate;
    private String vatIncludeYn;
    private String calculateMethodCode;
    private Double sendCost;
}
