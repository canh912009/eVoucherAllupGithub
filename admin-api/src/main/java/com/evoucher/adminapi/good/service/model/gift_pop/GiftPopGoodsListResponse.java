package com.evoucher.adminapi.good.service.model.gift_pop;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class GiftPopGoodsListResponse extends GiftPopListResponse {
    private List<GiftPopGoods> goodsList;
}
