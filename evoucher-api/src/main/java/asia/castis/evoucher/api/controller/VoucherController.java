package asia.castis.evoucher.api.controller;

import asia.castis.evoucher.api.common.enums.SystemType;
import asia.castis.evoucher.api.dto.otpservice.generate.OtpData;
import asia.castis.evoucher.api.dto.otpservice.generate.OtpResponse;
import asia.castis.evoucher.api.dto.request.*;
import asia.castis.evoucher.api.dto.request.activate.ActivateRequestV1;
import asia.castis.evoucher.api.dto.request.choose.ChoiceChosenRequestV1;
import asia.castis.evoucher.api.dto.response.*;
import asia.castis.evoucher.api.exception.InvalidException;
import asia.castis.evoucher.api.elastic.model.VoucherModel;
import asia.castis.evoucher.api.service.ChoiceVoucherService;
import asia.castis.evoucher.api.service.LimitedCountVoucherService;
import asia.castis.evoucher.api.service.version1.VoucherService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/vouchers")
@RequiredArgsConstructor
public class VoucherController {


    private final VoucherService voucherService;
    private final LimitedCountVoucherService lcVoucherService;
    private final ChoiceVoucherService<ChoiceChosenRequestV1> choiceVoucherService;
    private final Gson gson;

    @PostMapping("/choice/choose")
    public ResponseData<String> chooseChoiceVoucher(@RequestBody ChoiceChosenRequestV1 choiceRequest) {
        choiceVoucherService.chooseChoiceVoucher(choiceRequest);
        return ResponseData.ok();
    }

    @GetMapping("/choice/children/{ev}")
    public ResponseData<List<VoucherResponse>> getChoiceChildren(@PathVariable String ev) {
        return ResponseData.ok(voucherService.getVoucherListByEv(ev, SystemType.CHOICE));
    }
    @GetMapping("/bulk/children/{ev}")
    public ResponseData<List<VoucherResponse>> getBulkChildren(@PathVariable String ev) {
        return ResponseData.ok(voucherService.getVoucherListByEv(ev, SystemType.BULK));
    }

    @GetMapping("/lc/history/{ev}")
    public ResponseData<List<LimitedCountHistoryResponse>> getLimitedCountsHistory(@PathVariable String ev) {
        return ResponseData.ok(lcVoucherService.getHistory(ev));
    }

    @GetMapping("/getUserVoucherList")
    public ResponseData<PageResponse<VoucherResponse>> getUserVoucherList(UserVoucherListRequest request) {
        return ResponseData.ok(voucherService.getVoucherListByPhoneNo(request));
    }

    @GetMapping("/{ev}")
    public ResponseData<VoucherResponseWrapper> getVoucherDetails(@PathVariable(value = "ev") String ev) {
        return ResponseData.ok(voucherService.getVoucherDetails(ev));
    }

    @GetMapping("/publishDetails")
    public ResponseData<VoucherModel> publishDetails(@RequestParam(value = "activationId") String activationId) {
        VoucherModel response = voucherService.voucherBySerialNo(activationId);
        return ResponseData.ok(response);
    }

    @PostMapping("/transfer")
    public ResponseData<VoucherResponse> transferVoucher(@RequestBody TransferVoucherRequest request) {
        log.info("Transfer voucher request: {}", gson.toJson(request));
        return ResponseData.ok(voucherService.transferVoucher(request));
    }

    @PostMapping("/receipt")
    public ResponseData<VoucherResponse> receiptConfirmation(@RequestBody ReceiptRequest request) {
        log.info("Receipt voucher request: {}", gson.toJson(request));
        return ResponseData.ok(voucherService.confirmReceipt(request));
    }

    @GetMapping("/check-voucher-exist")
    public ResponseData<Boolean> checkVoucherExistForPhoneNumber(
            @RequestParam String mobileNumber) {
        return ResponseData.ok(voucherService.checkVoucherExistForPhoneNumber(mobileNumber));
    }

    @GetMapping("/get-otp-voucher")
    public ResponseData<OtpData> getOtpVoucherByPhoneNumber(
            @RequestParam String mobileNumber) {
        return ResponseData.ok(voucherService.getOtpByPhoneNumber(mobileNumber));
    }

    @PostMapping("/create-otp-voucher")
    public ResponseEntity<String> createOtpToGetVoucherList(
            @RequestParam String mobileNumber) {
        voucherService.createOtpToGetVoucherList(mobileNumber);
        return ResponseEntity.ok("");
    }

    @PostMapping("/activate")
    public ResponseEntity<Object> activateVoucher(@RequestBody ActivateRequestV1 activateReq) throws InvalidException {
        voucherService.activate(activateReq);
        return ResponseEntity.ok("");
    }
}
