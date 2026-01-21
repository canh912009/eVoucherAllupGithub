package com.evoucher.evoucherbe.client;

import com.evoucher.evoucherbe.config.ClientConfiguration;
import com.evoucher.evoucherbe.dto.partner_service.request.McpCardPurchaseRequest;
import com.evoucher.evoucherbe.dto.partner_service.request.McpTopUpRequest;
import com.evoucher.evoucherbe.message.BaseResponse;
import com.evoucher.evoucherbe.message.DataResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "jplaceholder",
        url = "${system.partner-service.url}",
        configuration = ClientConfiguration.class)
public interface PartnerServiceClient {
    @PostMapping("/vnpt/top-up")
    BaseResponse topUpVnpt(@RequestBody McpTopUpRequest request);

    @PostMapping("/vnpt/purchase")
    DataResponse purchaseVnpt(@RequestBody McpCardPurchaseRequest request);

    @PostMapping("/xpay/top-up")
    BaseResponse topUpXpay(@RequestBody McpTopUpRequest request);

    @PostMapping("/xpay/purchase")
    DataResponse purchaseXpay(@RequestBody McpCardPurchaseRequest request);
}
