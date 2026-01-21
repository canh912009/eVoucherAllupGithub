package com.castis.publishservice.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Accessors(chain = true)
@Data
public class UrBoxDataBuy {
    String priceId;
    String quantity;
//    Integer amount;
}
