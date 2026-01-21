package asia.castis.evoucher.api.service;

import asia.castis.evoucher.api.dto.request.CancelPaymentRequest;
import asia.castis.evoucher.api.dto.request.ExchangeRequest;
import asia.castis.evoucher.api.dto.response.ExchangeResponse;
import asia.castis.evoucher.api.dto.response.PaymentHistoryResponse;
import asia.castis.evoucher.api.dto.response.VoucherResponse;
import asia.castis.evoucher.api.elastic.response.ElasticVoucherResponse;

import java.util.List;

public interface WebPosService {

    VoucherResponse voucherDetailByOtp(String otp);

    PaymentHistoryResponse paymentDetail(String paymentHistoryId);

    ExchangeResponse cancelPayment(CancelPaymentRequest cancelPaymentRequest);

    ExchangeResponse exchangeVoucher(ExchangeRequest request);

    List<PaymentHistoryResponse> getListPaymentHistory(String request);

}
