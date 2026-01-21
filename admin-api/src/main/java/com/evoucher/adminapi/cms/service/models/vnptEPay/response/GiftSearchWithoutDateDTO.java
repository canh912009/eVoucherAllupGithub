package com.evoucher.adminapi.cms.service.models.vnptEPay.response;

import com.evoucher.adminapi.common.utils.Constant;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class GiftSearchWithoutDateDTO extends GiftSearchDTO{
    public GiftSearchWithoutDateDTO(Long id,
                                 String giftTitle,
                                 String brandName,
                                 Integer price,
                                 Integer quantity
    ) {
        super(id, giftTitle, brandName, price, quantity);
    }
}
