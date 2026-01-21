package asia.castis.evoucher.api.service;


import asia.castis.evoucher.api.dto.request.vnpt.VnptPurchaseRequest;

public interface VnptVoucherService {
    void purchase(VnptPurchaseRequest purchaseRequest);
}
