package asia.castis.evoucherservicefe.publishrequest.service.impl;

import asia.castis.evoucherservicefe.exceptions.SendMessageToQueueException;
import asia.castis.evoucherservicefe.publishrequest.dto.OtpSmsQueueMsg;
import asia.castis.evoucherservicefe.publishrequest.dto.OtpSmsRequest;
import asia.castis.evoucherservicefe.publishrequest.sender.PublishRequestSender;
import asia.castis.evoucherservicefe.publishrequest.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SmsServiceImpl implements SmsService {

    @Value("${push.brandName}")
    public String pushBrandName;
    @Value("${message.otp.template}")
    public String otpTemplate;

    private final PublishRequestSender publishRequestSender;

    public SmsServiceImpl(PublishRequestSender publishRequestSender) {
        this.publishRequestSender = publishRequestSender;
    }

    @Override
    public void generateAndSendOTP(OtpSmsRequest otpSmsRequest) throws SendMessageToQueueException {
        try {
            log.info("Receive request sending otp={}, to={}", otpSmsRequest.getOtp(), otpSmsRequest.getPhoneNumber());
            String message = String.format(otpTemplate, otpSmsRequest.getOtp());
            String phoneNumber = otpSmsRequest.getPhoneNumber();
            OtpSmsQueueMsg queueMsg = new OtpSmsQueueMsg();
            queueMsg.setMessage(message);
            queueMsg.setPhoneNumber(phoneNumber);
            queueMsg.setBrandName(pushBrandName);
            sendOutgoingPublish(queueMsg);
        } catch (Exception e) {
            log.error("Send otp exception", e);
            throw e;
        }
    }

    private void sendOutgoingPublish(OtpSmsQueueMsg message) throws SendMessageToQueueException {
        log.info("Start sending otp message={}, to={}", message.getMessage(), message.getPhoneNumber());
        publishRequestSender.sendOtpSMS(message);
        log.info("Finish sending otp message");
    }
}
