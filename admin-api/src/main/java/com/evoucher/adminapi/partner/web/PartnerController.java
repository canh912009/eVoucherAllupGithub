package com.evoucher.adminapi.partner.web;

import com.evoucher.adminapi.partner.service.model.request.ExternalPublishConvert;
import com.evoucher.adminapi.partner.service.PartnerService;
import com.evoucher.adminapi.partner.service.model.response.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/partner")
public class PartnerController {

    private final PartnerService partnerService;

    @GetMapping("/brand")
    public ResponseEntity<List> findAllBrand() {
        List<BrandPartnerResponse> brands = partnerService.findAllBrand();
        return new ResponseEntity<>(brands, HttpStatus.OK);
    }

    @GetMapping("/brand/{brandId}")
    public ResponseEntity<BrandPartnerResponse> findBrandByBrandId(
            @PathVariable String brandId) {
        BrandPartnerResponse brand = partnerService.findBrandByBrandId(brandId);
        return new ResponseEntity<>(brand, HttpStatus.OK);
    }

    @GetMapping("/goods")
    public ResponseEntity<List> findAllGoods() {
        List<GoodsPartnerResponse> brands = partnerService.findAllGoods();
        return new ResponseEntity<>(brands, HttpStatus.OK);
    }

    @GetMapping("/goods/{goodsId}")
    public ResponseEntity<GoodsPartnerInfoResponse> findGoodsByGoodsId(
            @PathVariable Integer goodsId) {
        GoodsPartnerInfoResponse brand = partnerService.findGoodsByGoodsId(goodsId);
        return new ResponseEntity<>(brand, HttpStatus.OK);
    }

    @PostMapping("/external-publish")
    public ResponseEntity<ExternalPublishResponse> createExternalPublish(
            @RequestBody ExternalPublishConvert externalPublish) {
        ExternalPublishResponse response =
                partnerService.createExternalPublish(externalPublish);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(
            value = "/external-publish",
            params = "transactionId")
    public ResponseEntity<ExternalPublishResponse> getOrderPinByTransactionId(
            @RequestParam(value = "transactionId") UUID transactionId) {
        log.info("Check order progess with transactionId: {}", transactionId);
        return new ResponseEntity<>(
                partnerService.checkOrderProgressWithTransactionId(transactionId),
                HttpStatus.OK);
    }

    @GetMapping(
            value = "/external-publish",
            params = "orderId")
    public ResponseEntity<ExternalPublishOrderPinResponse> getOrderPinByOrderId(
            @RequestParam(value = "orderId") Integer orderId) {
        log.info("Check order progess with orderId: {}", orderId);
        return new ResponseEntity<>(
                partnerService.checkOrderProgressWithOrderId(orderId),
                HttpStatus.OK);
    }

    @DeleteMapping(
            value = "/external-publish",
            params = "transactionId")
    public ResponseEntity<String> cancelExternalPublishByTransactionId(
            @RequestParam(value = "transactionId") UUID transactionId) {
        log.info("Cancel External publish with transactionId: {}", transactionId);
        partnerService.cancelExternalPublishByTransactionId(transactionId);
        return ResponseEntity.ok("");
    }

    @DeleteMapping(
            value = "/external-publish",
            params = "orderId")
    public ResponseEntity<String> cancelOrderPinByOrderId(
            @RequestParam(value = "orderId") Integer orderId) {
        log.info("Cancel Order Pin with orderId: {}", orderId);
        partnerService.cancelOrderPinByOrderId(orderId);
        return ResponseEntity.ok("");
    }
}
