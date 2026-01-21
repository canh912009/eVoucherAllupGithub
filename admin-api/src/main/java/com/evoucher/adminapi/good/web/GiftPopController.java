package com.evoucher.adminapi.good.web;

import com.evoucher.adminapi.common.message.BaseResponse;
import com.evoucher.adminapi.good.service.typed_service.system.GiftPopService;
import com.evoucher.adminapi.good.service.model.gift_pop.GiftPopBrand;
import com.evoucher.adminapi.good.service.model.gift_pop.GiftPopGoods;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/gift-pop")
public class GiftPopController {

    private final GiftPopService giftPopService;

    @GetMapping("/brand")
    public ResponseEntity<BaseResponse> getListGiftPopBrand() {
        log.info("Get Gift pop brand");
        List<GiftPopBrand> brands = giftPopService.getListGiftPopBrand();
        return new ResponseEntity<>(new BaseResponse(brands), HttpStatus.OK);
    }

    @GetMapping("/{brandCode}/goods")
    public ResponseEntity<BaseResponse> getListGiftPopGoods(
            @PathVariable(name = "brandCode") String brandCode) {
        log.info("Get Gift pop goods with brandCode: {}", brandCode);
        List<GiftPopGoods> goods = giftPopService.getListGiftPopGoods(brandCode);
        return new ResponseEntity<>(new BaseResponse(goods), HttpStatus.OK);
    }
}
