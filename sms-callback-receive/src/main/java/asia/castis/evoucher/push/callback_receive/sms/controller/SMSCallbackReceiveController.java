package asia.castis.evoucher.push.callback_receive.sms.controller;

import asia.castis.evoucher.push.callback_receive.sms.constant.Constant;
import asia.castis.evoucher.push.callback_receive.sms.model.PAMessage;
import asia.castis.evoucher.push.callback_receive.sms.model.PAMessageCallBackSms;
import asia.castis.evoucher.push.callback_receive.sms.model.SmsReceive;
import asia.castis.evoucher.push.callback_receive.sms.model.SmsResponse;
import asia.castis.evoucher.push.callback_receive.sms.service.PAMessageCallBackSmsService;
import asia.castis.evoucher.push.callback_receive.sms.service.PAMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class SMSCallbackReceiveController {
    private PAMessageCallBackSmsService service;

    public SMSCallbackReceiveController(PAMessageCallBackSmsService service/*, PAMessageService paMessageService*/) {
        this.service = service;
    }

    @PostMapping("/callback_receive")
    public SmsResponse callbackReceive(@RequestBody final SmsReceive smsReceive) {
        log.info("callbackReceive SmsReceive:{}", smsReceive);
        SmsResponse smsResponse = new SmsResponse();
        smsResponse.setStatus(1);
        if(smsReceive == null) {
            log.info("Oop! request body null");
            smsResponse.setStatus(0);
            return smsResponse;
        }else {
            if(smsReceive.getSmsid() != 0) {
                //log.info("SmsReceive: sms id: {}, status: {}, message trans count:{}", smsReceive.getSmsid(), smsReceive.getStatus(), smsReceive.getMt_count());
                //PAMessageCallBackSms existPAMessageCallBackSms = service.findById(String.valueOf(smsReceive.getSmsid()));
                PAMessageCallBackSms existPAMessageCallBackSms = service.findByMessageId(smsReceive.getSmsid());
                // first time dlr of this message
                if(existPAMessageCallBackSms == null) {
                    // save new
                    PAMessageCallBackSms newPAMessageCallbackSms = new PAMessageCallBackSms();
                    newPAMessageCallbackSms.setMessageId(smsReceive.getSmsid());
                    newPAMessageCallbackSms.setStatus(smsReceive.getStatus());
                    newPAMessageCallbackSms.setTelco(smsReceive.getTelco());
                    newPAMessageCallbackSms.setMtCount(smsReceive.getMt_count());
                    /*
                    if(smsReceive.getStatus() == Constant.MSG_SENDING_STATUS.SUCCESS) {
                        newPAMessageCallbackSms.setPaMessageStatusUpdated(1);
                    }
                     */
                    PAMessageCallBackSms savedNewPAMessageCallBackSms = service.save(newPAMessageCallbackSms);
                    if(savedNewPAMessageCallBackSms != null) {
                        log.info("Save new Callback Message {} success.", smsReceive.getSmsid());
                    }else {
                        log.info("Save new Callback Message {} fail.", smsReceive.getSmsid());
                    }
                } else {
                    // second time of dlr
                    // Update PAMessageCallBackSMS
                    existPAMessageCallBackSms.setStatus(smsReceive.getStatus());
                    existPAMessageCallBackSms.setError(smsReceive.getError());
                    PAMessageCallBackSms savedExistPAMessageCallBackSms = service.save(existPAMessageCallBackSms);
                    if(savedExistPAMessageCallBackSms != null) {
                        log.info("Update Sms Message callback for sms {} success.", smsReceive.getSmsid());
                    }else {
                        log.info("Update Sms Message callback for sms {} fail.", smsReceive.getSmsid());
                    }
                }
            } else {
                // invalid smsReceive
                log.info("callbackReceive: Oop! invalid msgid can't be 0");
            }
        }

        // Need to return status code
        return smsResponse;
    }
}
