package com.evoucher.externalserviceapi.api;

import com.evoucher.externalserviceapi.common.exception.ClientNotLoggedInException;
import com.evoucher.externalserviceapi.common.message.BaseResponse;
import com.evoucher.externalserviceapi.service.remote.GoodsService;
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
@RequestMapping("/v1/goods")
public class GoodsController {

    public final GoodsService goodsService;

    @GetMapping("")
    public ResponseEntity<BaseResponse> getAllGoods()
            throws IOException, ClientNotLoggedInException {
        log.info("Get all Goods");
        return new ResponseEntity<>(goodsService.getAllGoods(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> getGoodsById(
            @PathVariable(value = "id") String brandId)
            throws IOException, ClientNotLoggedInException {
        log.info("Get Goods by Id: {}", brandId);
        return new ResponseEntity<>(goodsService.getGoodsById(brandId), HttpStatus.OK);
    }
}
