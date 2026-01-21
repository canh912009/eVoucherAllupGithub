package com.evoucher.adminapi.admin.web;

import com.evoucher.adminapi.admin.service.CsManagementService;
import com.evoucher.adminapi.admin.service.models.CsManagementFilterRequest;
import com.evoucher.adminapi.admin.service.models.CsManagementResponse;
import com.evoucher.adminapi.admin.service.models.VoucherDisableRequest;
import com.evoucher.adminapi.admin.service.models.VoucherResendRequest;
import com.evoucher.adminapi.common.message.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/cs-management")
@RequiredArgsConstructor
@Slf4j
public class CsManagementController {
    private final CsManagementService service;
    @GetMapping(value = {"/search/{page}/{pageSize}", "/search"})
    public ResponseEntity<BaseResponse> searchPublish(@Valid CsManagementFilterRequest filterRequest,
                                                      @PathVariable(name = "page", required = false) Integer page,
                                                      @PathVariable(name = "pageSize", required = false) Integer pageSize) {
        Page<CsManagementResponse> result = service.search(filterRequest, page, pageSize);
        BaseResponse res = new BaseResponse(result.getContent());
        res.setTotalCount(result.getTotalElements());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
    @GetMapping(value = "/pin-detail/{ev}")
    public BaseResponse getVoucherDetail(@PathVariable("ev") String ev) {
        return new BaseResponse(service.getPinDetailV2(ev));
    }

    @PostMapping(value = "/disable-voucher")
    public ResponseEntity<BaseResponse> disableVoucher(
            @Valid @RequestBody VoucherDisableRequest request) {
        log.info("Start disable voucher with request: {}", request);
        service.disableVoucher(request);
        return new ResponseEntity<>(new BaseResponse(), HttpStatus.OK);
    }

    @PostMapping(value = "/resend-voucher")
    public ResponseEntity<BaseResponse> resendVoucher(
            @Valid @RequestBody VoucherResendRequest request) {
        log.info("Start resent voucher with request: {}", request);
        service.resendVoucher(request);
        return new ResponseEntity<>(new BaseResponse(), HttpStatus.OK);
    }
}
