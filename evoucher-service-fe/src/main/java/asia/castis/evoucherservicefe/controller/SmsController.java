package asia.castis.evoucherservicefe.controller;

import asia.castis.evoucherservicefe.common.dto.response.ResponseData;
import asia.castis.evoucherservicefe.common.utils.ErrorCode;
import asia.castis.evoucherservicefe.publishrequest.dto.OtpSmsRequest;
import asia.castis.evoucherservicefe.publishrequest.service.SmsService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
public class SmsController {
    private static final Logger log = LoggerFactory.getLogger(SmsController.class);
    private final SmsService service;
    @PostMapping("/otp-sms")
    public ResponseEntity<ResponseData<String>> generateAndSendOTP(@RequestBody OtpSmsRequest otpSmsRequest, HttpServletRequest request) {
        try {
            if (Objects.isNull(otpSmsRequest.getPhoneNumber()) || otpSmsRequest.getPhoneNumber().isEmpty()) {
                return ResponseEntity.badRequest().body(new ResponseData<>(ErrorCode.INVALID_REQUEST, "Null or empty phone number"));
            }
            if (Objects.isNull(otpSmsRequest.getOtp()) || otpSmsRequest.getOtp().isEmpty()) {
                return ResponseEntity.badRequest().body(new ResponseData<>(ErrorCode.INVALID_REQUEST, "Null or empty OTP"));
            }
            service.generateAndSendOTP(otpSmsRequest);
            return ResponseEntity.ok(ResponseData.ok());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.badRequest().body(new ResponseData<>(ErrorCode.UNKNOWN_ERROR, e.getMessage()));
        }
    }
}
