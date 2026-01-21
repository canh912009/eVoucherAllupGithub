package com.evoucher.adminapi.cms.service.models.vnptEPay.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class GiftSearchDTO {
//    public GiftSearchDTO(GiftSearchDTO o) {
//        this.id = o.id;
//        this.giftTitle = o.getGiftTitle();
//    }
    Long id;
    String giftTitle;
    String brandName;
    Integer price;
    Integer quantity;
//    Date createDate;
}
