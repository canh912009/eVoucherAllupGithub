package com.evoucher.adminapi.good.web;

import com.evoucher.adminapi.common.message.BaseResponse;
import com.evoucher.adminapi.good.service.typed_service.system.UrBoxService;
import com.evoucher.adminapi.good.service.model.ur_box.UrBoxGood;
import com.evoucher.adminapi.good.service.model.ur_box.UrBoxBrand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/ur-box")
public class UrBoxController {

    private final UrBoxService service;

    @GetMapping("/brands")
    public ResponseEntity<BaseResponse> getListGiftPopBrand() {
        log.info("Get Gift pop brand");
        List<UrBoxBrand> brands = service.getBrandList();
        return new ResponseEntity<>(BaseResponse.newList(brands), HttpStatus.OK);
    }

    @GetMapping("/{brandCode}/goods")
    public ResponseEntity<BaseResponse> getListGiftPopGoods(
            @PathVariable(name = "brandCode") String brandCode) {
        log.info("Get Gift pop goods with brandCode: {}", brandCode);
        List<UrBoxGood> goods = service.getGoodList(brandCode);

        return new ResponseEntity<>(BaseResponse.newList(goods), HttpStatus.OK);
    }

    @GetMapping("/goods/{id}")
    public ResponseEntity<BaseResponse> getGood(
            @PathVariable(name = "id") String id
    ) {
        UrBoxGood good = service.getGoodById(id);
        return ResponseEntity.ok(new BaseResponse(good));
    }
}
