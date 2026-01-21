package com.evoucher.evoucherbe.client;

import com.evoucher.evoucherbe.config.ClientConfiguration;
import com.evoucher.evoucherbe.service.request.ExternalPinGiftPopRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "publishService",
        url = "${servers.publishServer}",
        configuration = ClientConfiguration.class)
public interface PublishServiceClient {
    @PostMapping("/external-pins/ur-box")
    List<Long> buyUrBoxPin(@RequestBody ExternalPinGiftPopRequest request);
    @PostMapping("/external-pins/gift-pop")
    List<Long> buyGiftPopPin(@RequestBody ExternalPinGiftPopRequest request);
    @PostMapping("/external-pins/watane")
    List<Long> buyWatanePin(@RequestBody ExternalPinGiftPopRequest request);
}