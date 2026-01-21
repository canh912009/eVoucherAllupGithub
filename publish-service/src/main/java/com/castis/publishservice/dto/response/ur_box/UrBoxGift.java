package com.castis.publishservice.dto.response.ur_box;

import lombok.Data;
import lombok.Getter;

import java.util.List;

// equals to aqua voucher
@Data
public class UrBoxGift {
    private int pay;
    private String transaction_id;
    private String cart_created;
    private String linkCart;
    private String linkCombo;
    private String linkShippingInfo;
    private Cart cart;

    // Getters and setters
    @Data
    @Getter
    public static class Cart {
        private String id;
        private String cartNo;
        private String money_total;
        private String money_ship;
        private List<String> link_gift;
        private List<CartDetail> code_link_gift;

        // Getters and setters
        @Data
        public static class CartDetail {
            private String cart_detail_id;
            private String code_display;
            private int code_display_type;
            private String link;
            private String code;
            private int card_id;
            private String pin;
            private String serial;
            private String priceId;
            private String gift_id;
            private String token;
            private String expired;
            private long expired_time;
            private String code_image;
            private String estimateDelivery;
            private String ttemail;
            private String ttphone;
            private String receive_code;
            private int city_id;
            private int district_id;
            private int ward_id;
            private String delivery_note;
            private String ttaddress;
            private int deliveryCode;
            private int type;
            private int urcard_id;
            private int price;

            // Getters and setters
        }
    }

}




