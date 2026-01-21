package asia.castis.evoucher.api.service.generator;

import asia.castis.evoucher.api.dto.request.ExchangeRequest;
import asia.castis.evoucher.api.dto.request.PaymentHistory;
import asia.castis.evoucher.api.dto.request.vnpt.VnptPurchaseRequest;
import asia.castis.evoucher.api.entity.EVoucher;
import asia.castis.evoucher.api.entity.Goods;

public interface PaymentHistoryGenerator {
    PaymentHistory getVnptPaymentHistory(EVoucher sourceVoucher, Goods goods, VnptPurchaseRequest vnptPurchaseRequest);

    PaymentHistory generateExchangePaymentHistory(EVoucher sourceVoucher, Goods goods, ExchangeRequest exchangeRequest);
}
