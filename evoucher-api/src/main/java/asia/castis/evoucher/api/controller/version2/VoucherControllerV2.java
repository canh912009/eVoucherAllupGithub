package asia.castis.evoucher.api.controller.version2;

import asia.castis.evoucher.api.dto.otpservice.generate.OtpRequest;
import asia.castis.evoucher.api.dto.request.*;
import asia.castis.evoucher.api.dto.request.activate.ActivateRequestV2;
import asia.castis.evoucher.api.dto.response.PageResponse;
import asia.castis.evoucher.api.dto.response.ResponseData;
import asia.castis.evoucher.api.dto.response.VoucherResponse;
import asia.castis.evoucher.api.dto.response.VoucherResponseWrapper;
import asia.castis.evoucher.api.exception.InvalidException;
import asia.castis.evoucher.api.service.version2.VoucherServiceV2;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static asia.castis.evoucher.api.common.Constant.gson;

@Slf4j
@RestController
@RequestMapping("/v2/vouchers")
@RequiredArgsConstructor
public class VoucherControllerV2 {
    private final VoucherServiceV2 voucherService;

    @PostMapping()
    public ResponseData<VoucherResponseWrapper> getVoucherInfo(@Valid @RequestBody VoucherDetailsRequest request) {
        return ResponseData.ok(voucherService.getVoucher(request));
    }

    @PostMapping("/activate")
    public ResponseEntity<String> activateVoucher(@Valid @RequestBody ActivateRequestV2 activateReq) throws InvalidException {
        log.info("ActivateVoucher called with activateReq: {}", gson.toJson(activateReq));
        voucherService.activate(activateReq);
        return ResponseEntity.ok("");
    }

    @PostMapping("/getUserVoucherList")
    public ResponseData<PageResponse<VoucherResponse>> getUserVoucherList(@Valid @RequestBody UserVoucherListRequestV2 request) {
        return ResponseData.ok(voucherService.getVoucherListByPhoneNo(request));
    }

    @PostMapping("/createOtp")
    public ResponseEntity<String> createOtp(@Valid @RequestBody OtpRequest otpRequest) {
        log.info("CreateOtp called with otpRequest: {}", gson.toJson(otpRequest));
        voucherService.createOtp(otpRequest.getMobileNumber());
        return ResponseEntity.ok("");
    }

    @PostMapping("/checkSerialNumber")
    public ResponseData<Boolean> checkSerialNumber(@Valid @RequestBody SerialNumberRequest serialNumberCheck) {
        log.info("Check serial number: {}", gson.toJson(serialNumberCheck));
        boolean result = voucherService.checkSerialNumber(serialNumberCheck);
        if (result) {
            return ResponseData.ok(true);
        }
        return ResponseData.ok(false);
    }

}
