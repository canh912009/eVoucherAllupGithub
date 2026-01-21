package asia.castis.evoucher.api.service.base;

import asia.castis.evoucher.api.dto.otpservice.generate.OtpData;
import asia.castis.evoucher.api.dto.response.VoucherResponseWrapper;

public interface BaseVoucherService {
    VoucherResponseWrapper getVoucherDetails(String ev);

    OtpData getOtpByPhoneNumber(String phoneNumberEncrypt);

    void validateOtp(String encryptedPhoneNumber, String givenOtp);

    void createOtp(String encryptedPhoneNo);
}
