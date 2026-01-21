package com.evoucher.evoucherbe.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class VoucherDisableProcessRequest {
    private String ev;
    private Integer voucherDisableHistoryId;
}
