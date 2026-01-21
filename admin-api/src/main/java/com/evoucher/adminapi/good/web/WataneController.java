package com.evoucher.adminapi.good.web;

import com.evoucher.adminapi.common.message.BaseResponse;
import com.evoucher.adminapi.good.service.model.ur_box.UrBoxGood;
import com.evoucher.adminapi.good.service.model.watane.response.WataneBaseResponse;
import com.evoucher.adminapi.good.service.model.watane.response.WataneProduct;
import com.evoucher.adminapi.good.service.typed_service.system.watane.WataneService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/watane")
@Validated
public class WataneController {

    public static final int MAX_WATANE_GOODS = 899;
    public static final int FIRST_PAGE = 1;
    private final WataneService service;


    @GetMapping("/goods")
    public ResponseEntity<BaseResponse> getListGoods(@RequestParam @Valid @NotNull(message = "Start can not be null") @Positive(message = "Start must be positive") Integer start,
                                                     @RequestParam @NotNull(message = "Length can not be null") @Positive(message = "Length must be positive") Integer length) throws JsonProcessingException {
        List<WataneProduct> goods = service.getGoodList(start, length);
        return new ResponseEntity<>(BaseResponse.newList(goods), HttpStatus.OK);
    }
    @GetMapping("/goods-all")
    public ResponseEntity<BaseResponse> getAllListGoods() throws JsonProcessingException {
        List<WataneProduct> goods = service.getGoodList(FIRST_PAGE, MAX_WATANE_GOODS);
        return new ResponseEntity<>(BaseResponse.newList(goods), HttpStatus.OK);
    }

    @GetMapping("/goods/{id}")
    public ResponseEntity<BaseResponse> getGoodsDetail(@PathVariable(name = "id") @NotBlank(message = "Id can not be blank") String id) throws JsonProcessingException {
        WataneProduct good = service.getGoodById(id);
        return ResponseEntity.ok(new BaseResponse(good));
    }
}
