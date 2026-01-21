package com.evoucher.externalserviceapi.api;

import com.evoucher.externalserviceapi.common.exception.ClientNotLoggedInException;
import com.evoucher.externalserviceapi.common.message.BaseResponse;
import com.evoucher.externalserviceapi.service.remote.BrandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/brands")
public class BrandController {

    public final BrandService brandService;

    @GetMapping("")
    public ResponseEntity<BaseResponse> getAllBrand()
            throws IOException, ClientNotLoggedInException {
        log.info("Get all Brand");
        return new ResponseEntity<>(brandService.getAllBrand(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> getBrandById(
            @PathVariable(value = "id") String brandId)
            throws IOException, ClientNotLoggedInException {
        log.info("Get Brand by Id: {}", brandId);
        return new ResponseEntity<>(brandService.getBrandById(brandId), HttpStatus.OK);
    }
}
