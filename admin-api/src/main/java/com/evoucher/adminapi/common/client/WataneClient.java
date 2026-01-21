package com.evoucher.adminapi.common.client;

import com.evoucher.adminapi.common.config.ClientConfiguration;
import com.evoucher.adminapi.good.service.model.watane.request.WatanePageRequestBody;
import com.evoucher.adminapi.good.service.model.watane.request.WataneProductDetailRequestBody;
import com.evoucher.adminapi.good.service.model.watane.response.WataneBaseResponse;
import com.evoucher.adminapi.good.service.model.watane.response.WataneListResponse;
import com.evoucher.adminapi.good.service.model.watane.response.WataneProduct;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = "watane",
        url = "${watane.url}",
        configuration = ClientConfiguration.class)
public interface WataneClient {
    @PostMapping("/giftstore/api/products")
    WataneBaseResponse<WataneListResponse<WataneProduct>> getGoods(@RequestBody WatanePageRequestBody body,
                                                                   @RequestHeader("appId") String appId,
                                                                   @RequestHeader("signature") String signature,
                                                                   @RequestHeader("api-key") String apiKey,
                                                                   @RequestHeader("token") String token);

    @PostMapping("/giftstore/api/products/info")
    WataneBaseResponse<WataneProduct> getGoodsDetail(@RequestBody WataneProductDetailRequestBody body,
                          @RequestHeader("appId") String appId,
                          @RequestHeader("signature") String signature,
                          @RequestHeader("api-key") String apiKey,
                          @RequestHeader("token") String token);
}
