package com.evoucher.externalserviceapi.api;

import com.evoucher.externalserviceapi.common.exception.ClientNotLoggedInException;
import com.evoucher.externalserviceapi.common.message.BaseResponse;
import com.evoucher.externalserviceapi.service.model.request.ExternalPublishRequest;
import com.evoucher.externalserviceapi.service.remote.ExternalPublishService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/orders")
public class OrderPinController {

    public final ExternalPublishService externalPublishService;

    @PostMapping
    public ResponseEntity<BaseResponse> orderPin(
            ExternalPublishRequest externalPublishRequest)
            throws ClientNotLoggedInException, IOException {
        log.info("Order Pin with request: {}", externalPublishRequest);
        return new ResponseEntity<>(
                externalPublishService.uploadExternalPublish(externalPublishRequest),
                HttpStatus.OK);
    }

    @GetMapping(params = "transactionId")
    public ResponseEntity<BaseResponse> getOrderPinByTransactionId(
            @RequestParam(value = "transactionId") UUID transactionId)
            throws ClientNotLoggedInException, IOException {
        log.info("Get Order Pin with transactionId: {}", transactionId);
        return new ResponseEntity<>(
                externalPublishService.checkOrderProgressByTransaction(transactionId),
                HttpStatus.OK);
    }

    @GetMapping(params = "orderId")
    public ResponseEntity<BaseResponse> getOrderPinByOrderId(
            @RequestParam(value = "orderId") Integer orderId)
            throws ClientNotLoggedInException, IOException {
        log.info("Get Order Pin with orderId: {}", orderId);
        return new ResponseEntity<>(
                externalPublishService.checkOrderProgressByOrderId(orderId),
                HttpStatus.OK);
    }

    @DeleteMapping(params = "transactionId")
    public ResponseEntity<BaseResponse> cancelExternalPublishByTransactionId(
            @RequestParam(value = "transactionId") UUID transactionId)
            throws ClientNotLoggedInException, IOException {
        log.info("Cancel External publish with transactionId: {}", transactionId);
        return new ResponseEntity<>(
                externalPublishService.cancelExternalPublishByTransactionId(transactionId),
                HttpStatus.OK);
    }

    @DeleteMapping(params = "orderId")
    public ResponseEntity<BaseResponse> cancelOrderPinByOrderId(
            @RequestParam(value = "orderId") Integer orderId)
            throws ClientNotLoggedInException, IOException {
        log.info("Cancel Order Pin with orderId: {}", orderId);
        return new ResponseEntity<>(
                externalPublishService.cancelOrderPinByOrderId(orderId),
                HttpStatus.OK);
    }
}
