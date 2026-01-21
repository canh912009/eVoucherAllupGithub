package asia.castis.evoucherservicefe.publishrequest.service;

import asia.castis.evoucherservicefe.exceptions.SendMessageToQueueException;
import asia.castis.evoucherservicefe.publishrequest.dto.OtpSmsRequest;

public interface SmsService {
    void generateAndSendOTP(OtpSmsRequest otpSmsRequest) throws SendMessageToQueueException;
}
