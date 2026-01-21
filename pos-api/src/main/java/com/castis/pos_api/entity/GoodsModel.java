package com.castis.pos_api.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class GoodsModel {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("category")
    @Expose
    private CategoryModel category;
    @SerializedName("brand")
    @Expose
    private BrandModel brand;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("listPrice")
    @Expose
    private Double listPrice;
    @SerializedName("sellPrice")
    @Expose
    private Double sellPrice;
    @SerializedName("supplyDiscountCost")
    @Expose
    private Double supplyDiscountCost;
    @SerializedName("supplyFeeRate")
    @Expose
    private Double supplyFeeRate;
    @SerializedName("supplyCalculateMethodCode")
    @Expose
    private String supplyCalculateMethodCode;
    @SerializedName("sellDiscountRate")
    @Expose
    private Double sellDiscountRate;
    @SerializedName("sellDiscountCost")
    @Expose
    private Double sellDiscountCost;
    @SerializedName("sellFeeRate")
    @Expose
    private Double sellFeeRate;
    @SerializedName("sellCalculateMethod")
    @Expose
    private String sellCalculateMethod;
    @SerializedName("sendCost")
    @Expose
    private Double sendCost;
    @SerializedName("sellStartDate")
    @Expose
    private String sellStartDate;
    @SerializedName("sellEndDate")
    @Expose
    private String sellEndDate;
    @SerializedName("sticker")
    @Expose
    private String sticker;
    @SerializedName("exceptStoreIds")
    @Expose
    private String exceptStoreIds;
    @SerializedName("imagePath")
    @Expose
    private String imagePath;
    @SerializedName("imageName")
    @Expose
    private String imageName;
}
