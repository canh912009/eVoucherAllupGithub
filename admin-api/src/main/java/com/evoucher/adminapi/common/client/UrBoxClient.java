package com.evoucher.adminapi.common.client;

import com.evoucher.adminapi.common.config.ClientConfiguration;
import com.evoucher.adminapi.good.service.model.ur_box.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "urBox",
        url = "${ur-box.url}",
        configuration = ClientConfiguration.class)
public interface UrBoxClient {
    @GetMapping("${ur-box.path.get-brands}")
    UrBoxListResponse<UrBoxBrand> getBrands(@SpringQueryMap UrBoxBrandListReq req);
    @GetMapping("${ur-box.path.get-goods}")
    UrBoxListResponse<UrBoxGood> getGoods(@SpringQueryMap UrBoxGoodListRequest req);
    @GetMapping("${ur-box.path.get-good}")
    UrBoxSingleResponse<UrBoxGood> getGoodById(@SpringQueryMap UrBoxGoodByIdRequest req);
}
