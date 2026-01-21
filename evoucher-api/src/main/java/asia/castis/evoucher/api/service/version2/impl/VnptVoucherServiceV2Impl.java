package asia.castis.evoucher.api.service.version2.impl;

import asia.castis.evoucher.api.common.Encryption;
import asia.castis.evoucher.api.dto.request.vnpt.VnptPurchaseRequest;
import asia.castis.evoucher.api.dto.request.vnpt.VnptPurchaseRequestV2;
import asia.castis.evoucher.api.entity.EVoucher;
import asia.castis.evoucher.api.exception.ApplicationException;
import asia.castis.evoucher.api.repository.VoucherRepository;
import asia.castis.evoucher.api.service.VnptVoucherService;
import asia.castis.evoucher.api.service.base.BaseVoucherService;
import asia.castis.evoucher.api.service.version2.VnptVoucherServiceV2;
import asia.castis.evoucher.api.utils.ErrorCode;
import asia.castis.evoucher.api.utils.ResponseString;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service("vnptVoucherServiceV2")
@RequiredArgsConstructor
public class VnptVoucherServiceV2Impl implements VnptVoucherServiceV2 {

    private final VnptVoucherService vnptVoucherService;
    private final BaseVoucherService baseVoucherService;
    private final VoucherRepository voucherRepository;


    @Override
    public void purchase(VnptPurchaseRequestV2 purchaseRequest) {
        EVoucher voucher = voucherRepository
                .findById(purchaseRequest.getEv()).orElseThrow(() -> new ApplicationException(ResponseString.VOUCHER_NOT_FOUND, ErrorCode.VOUCHER_NOT_FOUND));
        baseVoucherService.validateOtp(voucher.getUserMobileNumber(), purchaseRequest.getOtp());

        vnptVoucherService.purchase(purchaseRequest);
    }
}
