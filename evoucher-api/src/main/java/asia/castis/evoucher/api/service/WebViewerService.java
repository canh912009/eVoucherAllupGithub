package asia.castis.evoucher.api.service;


import asia.castis.evoucher.api.dto.response.VoucherResponseWrapper;

public interface WebViewerService {

    VoucherResponseWrapper getVoucher(String id);

    String getEvFromShortLink(String shortLink);
}
