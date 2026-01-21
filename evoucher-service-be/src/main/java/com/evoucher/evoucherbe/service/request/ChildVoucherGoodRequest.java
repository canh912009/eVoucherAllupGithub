package com.evoucher.evoucherbe.service.request;

import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ChildVoucherGoodRequest {
    @NotNull(message = "Goods ID is empty")
    private Long goodsId;
    @NotNull(message = "Quantity is empty")
    private Integer quantity;
}
