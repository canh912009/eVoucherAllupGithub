package com.evoucher.adminapi.cms.service.models.vnptEPay.response;

import com.evoucher.adminapi.common.utils.Constant;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class GiftSearchWithDateDTO extends GiftSearchDTO {
    public GiftSearchWithDateDTO(Long id,
                                 String giftTitle,
                                 String brandName,
                                 Integer price,
                                 Integer quantity,
                                 Date createDate) {
        super(id, giftTitle, brandName, price, quantity);
        this.createDate = createDate;
    }

    @JsonFormat(pattern=Constant.Common.COMMON_DATETIME_FORMAT)
    private Date createDate;
}
