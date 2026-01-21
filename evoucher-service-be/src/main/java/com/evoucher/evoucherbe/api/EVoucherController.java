package com.evoucher.evoucherbe.api;

import com.evoucher.evoucherbe.common.enums.EnumAssetType;
import com.evoucher.evoucherbe.dto.*;
import com.evoucher.evoucherbe.message.BaseResponse;
import com.evoucher.evoucherbe.message.DataResponse;
import com.evoucher.evoucherbe.service.EVoucherProcessService;
import com.evoucher.evoucherbe.service.EVoucherService;
import com.evoucher.evoucherbe.service.request.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.ZoneOffset;
import java.util.List;

import static com.evoucher.evoucherbe.utils.Constant.gson;

@RestController
@RequiredArgsConstructor
@RequestMapping("/evouchers")
@Slf4j
public class EVoucherController {

    private final EVoucherService eVoucherService;
    private final EVoucherProcessService processService;
    private final ObjectMapper objectMapper;

    @PostMapping("/create")
    public ResponseEntity<BaseResponse> createVouchers(@Valid @RequestBody PublishRequest publishRequest) {
        log.info("Generate each voucher with publish={}", gson.toJson(publishRequest));
        List<String> result = eVoucherService.generateEachVoucher(publishRequest);
        return new ResponseEntity<>(new DataResponse(result), HttpStatus.OK);
    }

    @PostMapping(value = "/revert")
    public ResponseEntity<String> revertVouchers(@RequestBody List<String> evs) {
        log.info("Revert list voucher with evs/={}", evs);
        eVoucherService.revertListVoucher(evs);
        return ResponseEntity.ok("");
    }

    @PostMapping("/create/handover")
    public ResponseEntity<BaseResponse> handoverVoucher(
            @Valid @RequestBody VoucherHandoverRequest handoverRequest) {
        log.info("Handover voucher for VoucherHandoverRequest={}", handoverRequest.toString());
        String result = eVoucherService.voucherHandover(handoverRequest);
        return new ResponseEntity<>(new DataResponse(result), HttpStatus.OK);
    }

    @PostMapping("/create/choice-voucher")
    public ResponseEntity<BaseResponse> createChildVouchers(
            @Valid @RequestBody VoucherChoiceRequest voucherChoiceRequest) {
        log.info("Create choice voucher for VoucherChoiceRequest={}", gson.toJson(voucherChoiceRequest));
        List<String> result = eVoucherService.processCreateChoiceVoucher(voucherChoiceRequest);
        return new ResponseEntity<>(new DataResponse(result), HttpStatus.OK);
    }

    @PostMapping("/create/choice-voucher/v2")
    public ResponseEntity<BaseResponse> createChildVouchersV2(
            @Valid @RequestBody ChildVoucherRequest request) {
        log.info("Create choice voucher v2 for VoucherChoiceRequest={}", gson.toJson(request));
        List<String> result = eVoucherService.processCreateChildVouchersV2(request);
        return new ResponseEntity<>(new DataResponse(result), HttpStatus.OK);
    }

    @PostMapping("/activate-v2")
    public BaseResponse activateVoucher(@RequestBody ActivationRequest request) {
        log.info("Processing activate voucher v2={}", gson.toJson(request));
        processService.processActivateVoucher(request);
        return new BaseResponse();
    }

    @PostMapping("/disable")
    public ResponseEntity<String> disableVouchers(
            @Valid @RequestBody VoucherDisableRequest request) {
        log.info("Disable voucher for ev={}", request.getEv());
        eVoucherService.disableVoucher(request);
        return ResponseEntity.ok("");
    }

    @GetMapping("latest")
    public ResponseEntity<BaseResponse> getLatestData(@RequestParam EnumAssetType type, @RequestParam String id) {
        return ResponseEntity.ok(new DataResponse(eVoucherService.getLatestData(type, id)));
    }

    @PostMapping(value = "/use-voucher")
    public BaseResponse usingVoucher(@RequestBody VoucherExchangeReq request) {
        log.info("Processing use voucher={}", gson.toJson(request));
        return processService.processExchangeVoucher(request);
    }
    @PostMapping(value = "/use-vouchers")
    public BaseResponse usingVouchers(@RequestBody List<VoucherExchangeReq> requests) {
        log.info("Processing use vouchers={}", gson.toJson(requests));
        return processService.processExchangeVouchers(requests);
    }

    @PostMapping(value = "/cancel-exchange")
    public BaseResponse cancelExchange(@RequestBody VoucherExchangeReq request) {
        log.info("Processing cancel exchange={}", gson.toJson(request));
        return processService.processCancelingExchange(request);
    }

    @PostMapping(value = "/update-status")
    BaseResponse updateStatus(@RequestBody List<UpdatingVoucherReq> request) {
        log.info("Processing update voucher status={}", gson.toJson(request));
        return processService.updateVoucherStatus(request);
    }

    @PostMapping(value = "/receive")
    BaseResponse transferVoucher(@RequestBody EVoucherTransferProcess voucherTransferProcess) {
        log.info("Processing receive or not receipt voucher={}", gson.toJson(voucherTransferProcess));
        return processService.receiveVoucher(voucherTransferProcess);
    }

    @PostMapping(value = "/activate")
    BaseResponse activateVoucher(@RequestBody EVoucherActivateProcess activateProcess) {
        log.info("Processing activate voucher={}", activateProcess.getEv());
        return processService.processActivateVoucher(activateProcess);
    }

    @GetMapping(value = "get-jackson-timezone")
    BaseResponse getJacksonTimezone() {
        return BaseResponse.builder()
                .message(objectMapper.getDeserializationConfig().getTimeZone().getID().concat(ZoneOffset.systemDefault().toString())).build();
    }
}
