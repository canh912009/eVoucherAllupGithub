package com.evoucher.adminapi.good.service.model.gift_pop;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class GiftPopGoodsListRequest extends GiftPopAuthenticationRequest {
    private String brand;
}
