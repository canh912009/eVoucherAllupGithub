package com.evoucher.adminapi.common.client;

import com.evoucher.adminapi.common.config.ClientConfiguration;
import com.evoucher.adminapi.common.message.ApiBaseResponse;
import com.evoucher.adminapi.common.message.ApiDataResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "partnerServiceClient",
        url = "${e-voucher.partner-service.url}",
        configuration = ClientConfiguration.class)
public interface PartnerServiceClient {
    @GetMapping("/vnpt/balance")
    ApiDataResponse<Long> getAvailableBalance();

    @GetMapping("/xpay/balance")
    ApiDataResponse<Long> getAvailableXpayBalance();
}
