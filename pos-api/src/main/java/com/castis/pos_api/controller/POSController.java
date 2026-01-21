package com.castis.pos_api.controller;

import com.castis.pos_api.dto.request.FinalizeSingleRequest;
import com.castis.pos_api.dto.request.FinalizeListRequest;
import com.castis.pos_api.dto.request.TransactionIdOnlyResponse;
import com.castis.pos_api.dto.request.ValidatingRequest;
import com.castis.pos_api.dto.response.ResponseData;
import com.castis.pos_api.service.PosService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/pos/v1")
@RequiredArgsConstructor
@Slf4j
public class POSController {
    private final PosService posService;

    @PostMapping("/validate")
    public ResponseData posVoucher(@RequestBody @Valid ValidatingRequest validatingRequest,
                                   @RequestHeader String appId) {
        log.info("validate: appId: {}, body: {}", appId, validatingRequest);

        return posService.getVoucherDetail(appId, validatingRequest);
    }

    @PostMapping("/finalize-pre-paid")
    public ResponseData<TransactionIdOnlyResponse> finalizePrePaid(@RequestHeader String appId, @RequestBody @Valid FinalizeSingleRequest request) {
        log.info("finalize prepaid: {}", request);
        return posService.finalizePrePaid(appId, request);
    }

    @PostMapping("/finalize-single-items")
    public ResponseData<TransactionIdOnlyResponse> finalizeSingleItem(@RequestHeader String appId, @RequestBody @Valid FinalizeListRequest request) {
        log.info("finalize single item: {}", request);
        return posService.finalizeListOfItems(appId, request);
    }

//    @PostMapping("/cancel")
//    public ResponseData<TransactionIdOnlyResponse> cancelExchange(@RequestHeader(value = "authCd") String authCode,@RequestBody @Valid CancelRequest request) {
//        log.info("finalize using : {}, body: {}", authCode, request);
//        return posService.cancelExchange(authCode, request);
//    }

}