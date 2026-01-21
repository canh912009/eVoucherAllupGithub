package com.castis.publishservice.client;


import com.castis.publishservice.config.ClientConfiguration;
import com.castis.publishservice.dto.request.ur_box.UrBoxGiftReq;
import com.castis.publishservice.dto.response.ur_box.UrBoxGift;
import com.castis.publishservice.dto.response.ur_box.UrBoxResponse;
import com.castis.publishservice.dto.response.ur_box.UrBoxSingleResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = "urBox",
        url = "${ur-box.url}",
        configuration = ClientConfiguration.class)
public interface UrBoxClient {
    @PostMapping("${ur-box.path.buy-gift}")
    UrBoxSingleResponse<UrBoxGift> buyGift(@RequestHeader("Signature") String signature, @RequestBody UrBoxGiftReq body);
}
