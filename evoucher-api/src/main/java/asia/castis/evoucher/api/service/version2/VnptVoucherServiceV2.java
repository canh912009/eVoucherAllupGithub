package asia.castis.evoucher.api.service.version2;


import asia.castis.evoucher.api.dto.request.vnpt.VnptPurchaseRequestV2;

public interface VnptVoucherServiceV2 {
    void purchase(VnptPurchaseRequestV2 purchaseRequest);
}
