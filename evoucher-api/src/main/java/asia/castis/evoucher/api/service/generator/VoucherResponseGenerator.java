package asia.castis.evoucher.api.service.generator;

import asia.castis.evoucher.api.dto.response.VoucherResponse;
import asia.castis.evoucher.api.dto.response.VoucherResponseWrapper;
import asia.castis.evoucher.api.entity.EVoucher;

public interface VoucherResponseGenerator {

    VoucherResponseWrapper getVoucherResponseWrapper(EVoucher voucher);

    VoucherResponse getVoucherResponse(EVoucher voucher);

    VoucherResponse getVoucherResponse(String ev);
}
