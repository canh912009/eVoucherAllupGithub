package com.evoucher.evoucherbe.service.request;

import lombok.*;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ExternalPinGiftPopRequest {
    private Long goodsId;
    private Integer quantity;
    private Date expireDate;
}
