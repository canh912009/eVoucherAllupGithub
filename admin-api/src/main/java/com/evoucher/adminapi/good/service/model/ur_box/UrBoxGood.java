package com.evoucher.adminapi.good.service.model.ur_box;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class UrBoxGood {
    private String id;
    private String brand;
    @JsonProperty("brand_id")
    private String brandId;
    @JsonProperty("cat_id")
    private String catId;
    @JsonProperty("cat_title")
    private String catTitle;
    @JsonProperty("gift_id")
    private String giftId;
    private String title;
    //1: Voucher tiền mặt
    //2: Giftset
    //3: Combo
    //4: Thẻ balance
    //5: Thẻ điện thoại
    //7: Thẻ điểm
    //8: Topup điểm
    //9: Vật lý
    //10: Item (Sản phẩm cụ thể)
    //11: Voucher khuyến mãi
    //12: Bảo hiểm
    //14: Lượt quay số
    //15: Premium Service
    //16: Deal
    //19: Link quà UrCard
    private String type;
    private String price;
    private String point;
    private String view;
    private String quantity;
//    1: Số lượng trong kho > 0
//    2: Số lượng trong kho = 0
    private int stock;
    private String image;
    private Object images;
    @JsonProperty("images_rectangle")
    private Object imageRectangle;
    @JsonProperty("expire_duration")
    private String expireDuration;
    @JsonProperty("code_display")
    private String codeDisplay;
    @JsonProperty("code_display_type")
    private Integer codeDisplayType;
    private Integer price_promo;
    private Integer start_promo;
    private Integer end_promo;
    private Integer is_promo;
    private String is_unfix;
    private List<Office> office;
    private String brandLogoLoyalty;
    private String brandImage;
    @JsonProperty("brand_name")
    private String brandName;
    // 1 offline, 2 online
    @JsonProperty("brand_online")
    private String brandOnline;
    @JsonProperty("parent_cat_id")
    private String parentCatId;
    @JsonProperty("usage_check")
    private int usageCheck;
    private String content;
    private String note;
    @JsonProperty("code_quantity")
    private String codeQuantity;
    @Data
    public static class Office {
        @JsonProperty("brand_id")
        private String brandId;
        @JsonProperty("city_id")
        private String cityId;
        @JsonProperty("district_id")
        private String districtId;
        @JsonProperty("ward_id")
        private String wardId;
        @JsonProperty("street_id")
        private String streetId;
        private String code;
        private String address;
        @JsonProperty("address_en")
        private String addressEn;
        private String number;
        private String phone;
        private String latitude;
        private String longitude;
        private String geo;
        private String isApply;
        private String id;
        @JsonProperty("brand_img_src")
        private String brandImgSrc;
        @JsonProperty("brand_title")
        private String brandTitle;
        @JsonProperty("title_city")
        private String titleCity;

        // Constructor, getters, and setters
    }
    // Constructor, getters, and setters
}



