package com.castis.publishservice.controller;

import com.castis.publishservice.dto.PurchaseChildRequest;
import com.castis.publishservice.dto.request.ChoiceChosenRequest;
import com.castis.publishservice.dto.request.HandOverQueueMessage;
import com.castis.publishservice.dto.request.VoucherResendRequest;
import com.castis.publishservice.dto.response.BaseResponse;
import com.castis.publishservice.service.ProcessService;
import com.castis.publishservice.service.VoucherService;
import com.castis.publishservice.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/voucher")
public class VoucherController {
    private final ProcessService service;
    private final ProcessService processService;
    private final VoucherService voucherService;

    @PostMapping("/choose-choice")
    BaseResponse chooseChoiceItem(@RequestBody ChoiceChosenRequest choiceRequest) {
        log.info("Start create choice voucher item: {}", Utils.toJson(choiceRequest));
        return service.chooseChoiceItem(choiceRequest);
    }

    @PostMapping("/choose-choice-v2")
    BaseResponse chooseChoiceItem2(@RequestBody PurchaseChildRequest choiceRequest) {
        log.info("V2 Start create choice voucher item: {}", Utils.toJson(choiceRequest));
        return service.chooseChoiceItemV2(choiceRequest);
    }

    @PostMapping("/resend")
    public BaseResponse resendVoucher(@Valid @RequestBody VoucherResendRequest voucherResendRequest) {
        log.info("Start resent voucher: {}", Utils.toJson(voucherResendRequest));
        voucherService.resendVoucher(voucherResendRequest);
        return new BaseResponse();
    }

    @PostMapping("/transfer")
    public BaseResponse transferVoucher(@RequestBody HandOverQueueMessage request) {
        log.info("Start handover voucher: {}", Utils.toJson(request));
        return processService.voucherHandOver(request);
    }

}
