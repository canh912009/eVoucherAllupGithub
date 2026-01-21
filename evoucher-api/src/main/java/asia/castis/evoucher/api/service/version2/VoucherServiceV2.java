package asia.castis.evoucher.api.service.version2;

import asia.castis.evoucher.api.dto.request.SerialNumberRequest;
import asia.castis.evoucher.api.dto.request.UserVoucherListRequestV2;
import asia.castis.evoucher.api.dto.request.VoucherDetailsRequest;
import asia.castis.evoucher.api.dto.request.activate.ActivateRequestV2;
import asia.castis.evoucher.api.dto.response.PageResponse;
import asia.castis.evoucher.api.dto.response.VoucherResponse;
import asia.castis.evoucher.api.dto.response.VoucherResponseWrapper;
import asia.castis.evoucher.api.exception.InvalidException;

public interface VoucherServiceV2 {

    PageResponse<VoucherResponse> getVoucherListByPhoneNo(UserVoucherListRequestV2 request);

    void createOtp(String phoneNumberEncrypt);

    void activate(ActivateRequestV2 activateReq) throws InvalidException;

    VoucherResponseWrapper getVoucher(VoucherDetailsRequest request);

    boolean checkSerialNumber(SerialNumberRequest serialNumberCheck);
}
