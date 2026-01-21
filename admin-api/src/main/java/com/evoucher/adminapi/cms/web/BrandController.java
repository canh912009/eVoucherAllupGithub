package com.evoucher.adminapi.cms.web;

import com.evoucher.adminapi.cms.service.BrandService;
import com.evoucher.adminapi.cms.service.models.BrandDTO;
import com.evoucher.adminapi.cms.service.models.request.BrandRequest;
import com.evoucher.adminapi.common.message.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/brands")
public class BrandController {

    private final BrandService brandService;

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> findById(@PathVariable String id) {
        BrandDTO searchBrandResponse = brandService.findDtoById(id);
        return new ResponseEntity<>(new BaseResponse(searchBrandResponse), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BaseResponse> createBrandDTO(@Valid @RequestBody BrandRequest brandRequest) {
        BrandDTO result = brandService.createBrand(brandRequest);
        return new ResponseEntity<>(new BaseResponse(result), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<BaseResponse> updateBrandDTO(@PathVariable String id,
                                                       @Valid @RequestBody BrandRequest brandRequest) {
        BrandDTO result = brandService.updateBrand(id, brandRequest);
        return new ResponseEntity<>(new BaseResponse(result), HttpStatus.OK);
    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<BaseResponse> deleteBrandDTO(@PathVariable String id) {
//        String result = brandService.deleteBrandById(id);
//        return new ResponseEntity<>(new BaseResponse(result), HttpStatus.OK);
//    }

}
