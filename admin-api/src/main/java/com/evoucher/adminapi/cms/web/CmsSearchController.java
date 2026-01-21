package com.evoucher.adminapi.cms.web;

import com.evoucher.adminapi.cms.service.BrandService;
import com.evoucher.adminapi.cms.service.StoreService;
import com.evoucher.adminapi.cms.service.SupplierService;
import com.evoucher.adminapi.cms.service.models.*;
import com.evoucher.adminapi.common.message.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/searches")
@RequiredArgsConstructor
public class CmsSearchController {

    private final SupplierService supplierService;
    private final BrandService brandService;
    private final StoreService storeService;


    @GetMapping(value = {"/supplier/{page}/{pageSize}", "/supplier"})
    public ResponseEntity<BaseResponse> searchSupplier(FilterSearchCms filterSearchCms,
                                                     @PathVariable(name = "page", required = false) Integer page,
                                                     @PathVariable(name = "pageSize", required = false) Integer pageSize) {
        Page<SupplierDTO> result = supplierService.searchSupplierDTO(filterSearchCms);
        BaseResponse res = new BaseResponse(result.getContent());
        res.setTotalCount(result.getTotalElements());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping(value = {"/brand/{page}/{pageSize}", "/brand"})
    public ResponseEntity<BaseResponse> searchBrand(FilterSearchCms filterSearchCms,
                                                  @PathVariable(name = "page", required = false) Integer page,
                                                  @PathVariable(name = "pageSize", required = false) Integer pageSize) {
        Page<? extends SearchBrandResponse> result = brandService.searchBrandDTO(filterSearchCms);
        BaseResponse res = new BaseResponse(result.getContent());
        res.setTotalCount(result.getTotalElements());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping(value = {"/store/{page}/{pageSize}", "/store"})
    public ResponseEntity<BaseResponse> searchStore(FilterSearchCms filterSearchCms,
                                                     @PathVariable(name = "page", required = false) Integer page,
                                                     @PathVariable(name = "pageSize", required = false) Integer pageSize) {
        Page<SearchStoreResponse> result = storeService.searchStoreDTO(filterSearchCms);
        BaseResponse res = new BaseResponse(result.getContent());
        res.setTotalCount(result.getTotalElements());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
