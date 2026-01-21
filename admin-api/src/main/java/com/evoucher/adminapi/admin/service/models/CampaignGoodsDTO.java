package com.evoucher.adminapi.admin.service.models;

import com.evoucher.adminapi.common.models.BaseDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CampaignGoodsDTO extends BaseDTO {
    private Integer campaignId;
    private Integer goodsId;
    private Double sellDiscountRate;
    private Double sellDiscountAmount;
    private Double sellCommissionRate;
    private String vatIncludeYn;
    private String settlementMethodCode;
    private Double sendCost;
    private String validYn;
}
