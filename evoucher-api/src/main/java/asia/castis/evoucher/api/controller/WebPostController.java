package asia.castis.evoucher.api.controller;

import asia.castis.evoucher.api.dto.request.CancelPaymentRequest;
import asia.castis.evoucher.api.dto.request.ExchangeRequest;
import asia.castis.evoucher.api.dto.response.ExchangeResponse;
import asia.castis.evoucher.api.dto.response.PaymentHistoryResponse;
import asia.castis.evoucher.api.dto.response.ResponseData;
import asia.castis.evoucher.api.dto.response.VoucherResponse;
import asia.castis.evoucher.api.service.WebPosService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/webpos/vouchers")
@RequiredArgsConstructor
public class WebPostController {

    private final WebPosService webPostService;

    @GetMapping("/otp/{otp}")
    public ResponseData<VoucherResponse> getVoucherByOtp(@PathVariable(value = "otp") String otp) {
        return ResponseData.ok(webPostService.voucherDetailByOtp(otp));
    }

    @GetMapping("/payments/{paymentHistoryId}")
    public ResponseData<PaymentHistoryResponse> getPaymentDetail(@PathVariable(value = "paymentHistoryId") String paymentHistoryId) {
        return ResponseData.ok(webPostService.paymentDetail(paymentHistoryId));
    }

    @GetMapping("/payments")
    public ResponseData<List<PaymentHistoryResponse>> getListPayment(@RequestParam String storeId) {
        return ResponseData.ok(webPostService.getListPaymentHistory(storeId));
    }

    @PostMapping("/exchange")
    public ResponseData<ExchangeResponse> exchange(@RequestBody ExchangeRequest request) {
        return ResponseData.ok(webPostService.exchangeVoucher(request));
    }

    @PostMapping("/cancelPayment")
    public ResponseData<ExchangeResponse> cancelPayment(@RequestBody CancelPaymentRequest request) {
        return ResponseData.ok(webPostService.cancelPayment(request));
    }
}
