package com.evoucher.adminapi.common.client;


import com.evoucher.adminapi.cms.service.models.vnptEPay.request.ServiceBEPurchaseRequest;
import com.evoucher.adminapi.cms.service.models.vnptEPay.response.BalanceResponse;
import com.evoucher.adminapi.cms.service.models.vnptEPay.response.ServiceBEPurchaseResponse;
import com.evoucher.adminapi.common.config.ClientConfiguration;
import com.evoucher.adminapi.common.message.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "jplaceholder",
        url = "${e-voucher.service-be.url}",
        configuration = ClientConfiguration.class)
public interface ServiceBEClient {
    @GetMapping("/vnpt-integrate/query-balance")
    BaseResponse queryBalance();
    @PostMapping("/vnpt-integrate/download-soft-pin")
    BaseResponse downloadSoftPin(@RequestBody ServiceBEPurchaseRequest request);
}
