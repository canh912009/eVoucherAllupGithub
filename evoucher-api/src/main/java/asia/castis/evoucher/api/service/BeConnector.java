package asia.castis.evoucher.api.service;

import asia.castis.evoucher.api.dto.request.PaymentHistory;
import asia.castis.evoucher.api.dto.request.activate.ActivateRequestV2;
import asia.castis.evoucher.api.dto.response.ResponseData;
import asia.castis.evoucher.api.publishrequest.PublishReceiptVoucher;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

public interface BeConnector {
    ResponseData<?> purchase(PaymentHistory exchangeRequest);

    ResponseData<?> cancelExchange(PaymentHistory cancelRequest);

    ResponseData<?> confirmReceive(PublishReceiptVoucher confirmReceive);

    ResponseData<Map<Long, Integer>> getRemainingCount(List<Long> goodsId) throws URISyntaxException;

    ResponseData<?> activate(ActivateRequestV2 activateRequest);
}
