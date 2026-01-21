package asia.castis.evoucher.api.service.version2.impl;

import asia.castis.evoucher.api.common.Encryption;
import asia.castis.evoucher.api.common.VoucherUtils;
import asia.castis.evoucher.api.common.enums.VoucherStatusCode;
import asia.castis.evoucher.api.dto.request.SerialNumberRequest;
import asia.castis.evoucher.api.dto.request.UserVoucherListRequestV2;
import asia.castis.evoucher.api.dto.request.activate.ActivateRequestV2;
import asia.castis.evoucher.api.dto.request.VoucherDetailsRequest;
import asia.castis.evoucher.api.dto.response.PageResponse;
import asia.castis.evoucher.api.dto.response.ResponseData;
import asia.castis.evoucher.api.dto.response.VoucherResponse;
import asia.castis.evoucher.api.dto.response.VoucherResponseWrapper;
import asia.castis.evoucher.api.entity.EVoucher;
import asia.castis.evoucher.api.exception.ApplicationException;
import asia.castis.evoucher.api.repository.VoucherRepository;
import asia.castis.evoucher.api.service.BeConnector;
import asia.castis.evoucher.api.service.WebViewerService;
import asia.castis.evoucher.api.service.base.BaseVoucherService;
import asia.castis.evoucher.api.service.version1.VoucherService;
import asia.castis.evoucher.api.service.version2.VoucherServiceV2;
import asia.castis.evoucher.api.utils.ErrorCode;
import asia.castis.evoucher.api.utils.ResponseString;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class VoucherServiceV2Impl implements VoucherServiceV2 {
    public static final int BE_SUCCESS_CODE = 0;
    private final VoucherRepository voucherRepository;
    private final BaseVoucherService baseVoucherService;
    private final BeConnector beConnector;
    private final VoucherService voucherService;
    private final Encryption encryption;
    private final WebViewerService webViewerService;

    @Override
    public VoucherResponseWrapper getVoucher(VoucherDetailsRequest request) {
        String ev = webViewerService.getEvFromShortLink(request.getVoucherId());
        EVoucher voucher = voucherRepository
                .findById(ev).orElseThrow(() -> new ApplicationException(ResponseString.VOUCHER_NOT_FOUND, ErrorCode.VOUCHER_NOT_FOUND));

        // if required OTP, validate it
        if (VoucherUtils.isOtpRequired(voucher)) {
            if (Objects.isNull(request.getOtp()) || request.getOtp().isEmpty()) {
                throw new ApplicationException(ResponseString.OTP_IS_REQUIRED, ErrorCode.OTP_IS_REQUIRED);
            }
            baseVoucherService.validateOtp(voucher.getUserMobileNumber(), request.getOtp());
        }

        return baseVoucherService.getVoucherDetails(ev);
    }

    @Override
    public boolean checkSerialNumber(SerialNumberRequest serialNumberCheck) {
        Optional<EVoucher> voucherOptional = voucherRepository.findBySerialNoAndShortLinkEndingWith(serialNumberCheck.getSerialNumber(), serialNumberCheck.getVoucherId());
        return voucherOptional.isPresent();
    }

    @Override
    public PageResponse<VoucherResponse> getVoucherListByPhoneNo(UserVoucherListRequestV2 request) {
        baseVoucherService.validateOtp(request.getMobileNumber(), request.getOtp());
        return voucherService.getVoucherListByPhoneNo(request);
    }

    @Override
    public void createOtp(String phoneNumberEncrypt) {
        baseVoucherService.createOtp(encryption.encryptData(phoneNumberEncrypt));
    }

    @Override
    public void activate(ActivateRequestV2 activateReq) {
        try {

            String ev = webViewerService.getEvFromShortLink(activateReq.getVoucherId());
            EVoucher voucher = voucherRepository
                    .findById(ev).orElseThrow(() -> new ApplicationException(ResponseString.VOUCHER_NOT_FOUND, ErrorCode.VOUCHER_NOT_FOUND));

            validateActivateRequest(activateReq, voucher);
            // Version 2. Activation process doesn't go through evoucher-service-fe,
            // evoucher-api sends request directly to evoucher-service-be
            activateReq.setEv(voucher.getEV());
            ResponseData<?> responseData = beConnector.activate(activateReq);

            if (responseData.getCode() != BE_SUCCESS_CODE) {
                throw new ApplicationException(responseData.getMessage(), responseData.getCode());
            }
        } catch (ApplicationException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error calling API", e);
            throw new ApplicationException(
                    "Error calling API",
                    ErrorCode.CAN_NOT_ACTIVATE_VOUCHER);
        }
    }

    private void validateActivateRequest(ActivateRequestV2 activateReq, EVoucher voucher) {
        if (VoucherUtils.isActivated(voucher)) {
            throw new ApplicationException(ResponseString.VOUCHER_ALREADY_ACTIVATED, ErrorCode.VOUCHER_ALREADY_ACTIVATED);
        }
        baseVoucherService.validateOtp(encryption.encryptData(activateReq.getPhoneNumber()), activateReq.getOtp());

        if (Objects.isNull(voucher.getSerialNo())) {
            throw new ApplicationException("Serial number not found", ErrorCode.SERIAL_NO_MISMATCH);
        }else if (!activateReq.getSerialNumber().equals(voucher.getSerialNo())) {
            throw new ApplicationException(ResponseString.SERIAL_NO_MISMATCH, ErrorCode.SERIAL_NO_MISMATCH);
        }

        if (VoucherStatusCode.NORMAL != voucher.getVoucherStatusCode()) {
            throw new ApplicationException("Voucher has invalid status " + voucher.getVoucherStatusCode(), ErrorCode.VOUCHER_ALREADY_ACTIVATED);
        }
    }
}
