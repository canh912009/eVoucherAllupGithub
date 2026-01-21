package com.evoucher.evoucherbe.api;

import com.evoucher.evoucherbe.dto.partner_service.request.McpTransactionResult;
import com.evoucher.evoucherbe.message.BaseResponse;
import com.evoucher.evoucherbe.service.EVoucherProcessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/partner")
@Slf4j
public class PartnerController {

    private final EVoucherProcessService processService;

    @PostMapping(value = "/vnpt/transaction-result")
    BaseResponse handleVnptTransactionResultReceived(@RequestBody McpTransactionResult vnptTransactionResult) {
        log.info("Received VNPT transaction result={}", vnptTransactionResult.getEv());
        processService.handleMcpTransactionResultReceived(vnptTransactionResult);
        return new BaseResponse();
    }

    @PostMapping(value = "/xpay/transaction-result")
    BaseResponse handleXpayTransactionResultReceived(@RequestBody McpTransactionResult xpayTransactionResult) {
        log.info("Received XPAY transaction result={}", xpayTransactionResult.getEv());
        processService.handleMcpTransactionResultReceived(xpayTransactionResult);
        return new BaseResponse();
    }
}
