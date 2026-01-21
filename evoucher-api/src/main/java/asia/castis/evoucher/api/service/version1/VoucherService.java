package asia.castis.evoucher.api.service.version1;

import asia.castis.evoucher.api.common.enums.SystemType;
import asia.castis.evoucher.api.dto.otpservice.generate.OtpData;
import asia.castis.evoucher.api.dto.request.ReceiptRequest;
import asia.castis.evoucher.api.dto.request.TransferVoucherRequest;
import asia.castis.evoucher.api.dto.request.UserVoucherListRequest;
import asia.castis.evoucher.api.dto.request.activate.ActivateRequestV1;
import asia.castis.evoucher.api.dto.response.PageResponse;
import asia.castis.evoucher.api.dto.response.VoucherResponse;
import asia.castis.evoucher.api.dto.response.VoucherResponseWrapper;
import asia.castis.evoucher.api.elastic.model.VoucherModel;
import asia.castis.evoucher.api.exception.InvalidException;

import java.util.List;

public interface VoucherService {

    VoucherResponse confirmReceipt(ReceiptRequest request);

    PageResponse<VoucherResponse> getVoucherListByPhoneNo(UserVoucherListRequest request);

    VoucherResponseWrapper getVoucherDetails(String request);

    VoucherResponse transferVoucher(TransferVoucherRequest request);

    Boolean checkVoucherExistForPhoneNumber(String phoneNumberEncrypt);

    OtpData getOtpByPhoneNumber(String phoneNumberEncrypt);

    void createOtpToGetVoucherList(String phoneNumberEncrypt);

    void activate(ActivateRequestV1 activateReq) throws InvalidException;

    VoucherModel voucherBySerialNo(String serialNumber);

    List<VoucherResponse> getVoucherListByEv(String ev, SystemType systemType);
}
