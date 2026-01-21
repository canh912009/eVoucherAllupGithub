package asia.castis.evoucher.api.service;

import asia.castis.evoucher.api.dto.otpservice.generate.OtpGenerateTTLRequest;
import asia.castis.evoucher.api.dto.otpservice.generate.OtpResponse;
import asia.castis.evoucher.api.dto.otpservice.restore.VoucherOtpResponse;
import asia.castis.evoucher.api.dto.otpservice.use.VoucherUuidResponse;

public interface OtpConnector {
    VoucherUuidResponse getUuidByOtp(String otp);

    OtpResponse getOtpByPhoneNo(String key);

    VoucherOtpResponse generateDefaultOtp(String ev);

    VoucherOtpResponse generateOtpWithTTL(OtpGenerateTTLRequest request);

    VoucherUuidResponse useOtp(String otp);

    VoucherUuidResponse getOtp(String otp);

    void restoreOtp(String otp);
}
