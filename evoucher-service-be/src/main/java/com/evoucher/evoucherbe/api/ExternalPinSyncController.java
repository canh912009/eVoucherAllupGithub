package com.evoucher.evoucherbe.api;

import com.evoucher.evoucherbe.message.BaseResponse;
import com.evoucher.evoucherbe.message.DataResponse;
import com.evoucher.evoucherbe.service.ExternalPinService;
import com.evoucher.evoucherbe.utils.Constant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pins")
@Slf4j
public class ExternalPinSyncController {
    private final ExternalPinService service;
    @GetMapping("/quantity")
    public ResponseEntity<BaseResponse> queryBalance(
            @RequestParam List<Integer> ids) {
        log.info("Generate each voucher with publish: {}", Constant.gson.toJson(ids));
        Map<Integer, Integer> result = service.getRemainingQuantity(ids);
        return new ResponseEntity<>(new DataResponse(result), HttpStatus.OK);
    }
}