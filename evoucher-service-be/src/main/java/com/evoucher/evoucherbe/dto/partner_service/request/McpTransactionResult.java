package com.evoucher.evoucherbe.dto.partner_service.request;

import com.evoucher.evoucherbe.common.enums.McpTransactionResultEnum;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class McpTransactionResult {
    @NotNull
    private String ev;
    @NotNull
    private McpTransactionResultEnum result;
}
