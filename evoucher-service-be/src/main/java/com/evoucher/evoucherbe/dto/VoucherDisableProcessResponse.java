package com.evoucher.evoucherbe.dto;

import com.evoucher.evoucherbe.common.enums.CompletedStatusCode;
import com.evoucher.evoucherbe.common.enums.VoucherStatusCode;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class VoucherDisableProcessResponse {
    private String ev;
    private Integer voucherDisableHistoryId;
    private VoucherStatusCode frontEndPreviousStatusCode;
    private CompletedStatusCode frontEndUpdateResult;
}
