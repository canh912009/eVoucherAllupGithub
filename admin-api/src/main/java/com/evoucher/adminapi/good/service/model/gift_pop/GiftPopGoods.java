package com.evoucher.adminapi.good.service.model.gift_pop;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GiftPopGoods {
    private String goodsId;
    private String goodsName;
    private String enGoodsName;
    private String krGoodsName;
    private String brandCode;
    private String brandName;
    private String originImg;
    private Double listPrice;
    private Double salePrice;
    private String goodsType;
    private String saleFeeType;
    private String useYN;
    private String modDate;
    private String commtGuide;
    @JsonProperty("commtGuide_en")
    private String commtGuideEn;
    @JsonProperty("commtGuide_kr")
    private String commtGuideKr;
    private String commtProduct;
    @JsonProperty("commtProduct_en")
    private String commtProductEn;
    @JsonProperty("commtProduct_kr")
    private String commtProductKr;
    private String expiryDate;
    @JsonProperty("expiryDate_en")
    private String expiryDateEn;
    @JsonProperty("expiryDate_kr")
    private String expiryDateKr;
    private String cateCode;
    private Double stock;
    private String email;
    private String hotline;
}
