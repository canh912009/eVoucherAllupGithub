package com.evoucher.adminapi.cms.web;

import com.evoucher.adminapi.cms.service.VnptEpayService;
import com.evoucher.adminapi.common.message.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/vnpt/epay")
public class VnptEPayController {
    private final VnptEpayService service;

    @GetMapping("/get-balance")
    BaseResponse searchGift() {
        return service.getProviderBalance(false);
    }

    @GetMapping("/get-xpay-balance")
    BaseResponse searchXpayGift() {
        return service.getProviderBalance(true);
    }
//    @PostMapping("/purchase")
//    BaseResponse purchaseGift(@RequestBody PurchaseRequest request) {
//        service.purchaseVnptGift(request);
//        return new BaseResponse();
//    }
}
