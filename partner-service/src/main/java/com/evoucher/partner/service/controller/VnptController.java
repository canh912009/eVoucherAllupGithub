package com.evoucher.partner.service.controller;

import com.evoucher.partner.service.bean.request.VnptPurchaseRequest;
import com.evoucher.partner.service.bean.request.VnptTopUpRequest;
import com.evoucher.partner.service.bean.response.BaseResponse;
import com.evoucher.partner.service.bean.response.DataResponse;
import com.evoucher.partner.service.vnpt.service.VnptService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/vnpt")
public class VnptController {
    private final VnptService vnptService;
    @GetMapping("/balance")
    BaseResponse getAvailableBalance() {
        return vnptService.getAvailableBalance();
    }

    @PostMapping("/top-up")
    BaseResponse topUp(@RequestBody VnptTopUpRequest request) {
        return vnptService.topup(request);
    }

    @PostMapping("/purchase")
    DataResponse purchase(@RequestBody VnptPurchaseRequest request) {
        return vnptService.purchase(request);
    }
}
