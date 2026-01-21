package com.evoucher.adminapi.cms.service.models.vnptEPay.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class BrandSearchDTO {
    String id;
    String brandTitle;
    Integer numberOfGifts;
}
