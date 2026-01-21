package com.evoucher.adminapi.cms.web;

import com.evoucher.adminapi.cms.service.XPAYProviderService;
import com.evoucher.adminapi.cms.service.models.FilterSearchCms;
import com.evoucher.adminapi.cms.service.models.XPAYProviderDto;
import com.evoucher.adminapi.common.message.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/xpay/provider")
@RequiredArgsConstructor
public class XPayProviderController {
    private final XPAYProviderService service;
    @GetMapping(value = {"/search/{page}/{pageSize}", "/search"})
    BaseResponse getAll(FilterSearchCms filter, @PathVariable(required = false) Integer page, @PathVariable(required = false) Integer pageSize) {
        return service.getAll(filter, page, pageSize);
    }

    @GetMapping("/{id}")
    BaseResponse findById(@PathVariable String id) {
        return service.findById(id);
    }

    @PutMapping
    BaseResponse update(@RequestBody XPAYProviderDto provider) {
        return service.update(provider);
    }

}
