package com.evoucher.adminapi.cms.web;

import com.evoucher.adminapi.cms.service.VnptProviderService;
import com.evoucher.adminapi.cms.service.models.FilterSearchCms;
import com.evoucher.adminapi.cms.service.models.VnptProviderDto;
import com.evoucher.adminapi.common.message.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vnpt/provider")
@RequiredArgsConstructor
public class VnptProviderController {
    private final VnptProviderService service;
    @GetMapping(value = {"/search/{page}/{pageSize}", "/search"})
    BaseResponse getAll(FilterSearchCms filter, @PathVariable(required = false) Integer page, @PathVariable(required = false) Integer pageSize) {
        return service.getAll(filter, page, pageSize);
    }

    @GetMapping("/{id}")
    BaseResponse findById(@PathVariable String id) {
        return service.findById(id);
    }

//    @PostMapping
//    BaseResponse create(@RequestBody VnptProviderDto provider) {
//        return service.createNew(provider);
//    }

    @PutMapping
    BaseResponse update(@RequestBody VnptProviderDto provider) {
        return service.update(provider);
    }

}
