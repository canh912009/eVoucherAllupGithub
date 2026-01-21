package asia.castis.evoucher.push.callback_receive.sms.service;

import asia.castis.evoucher.push.callback_receive.sms.model.PAMessageCallBackSms;
import asia.castis.evoucher.push.callback_receive.sms.repositories.PACallBackSMSRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PAMessageCallBackSmsService {
    private PACallBackSMSRepository paCallBackSMSRepository;
    @Autowired
    public PAMessageCallBackSmsService(PACallBackSMSRepository paCallBackSMSRepository) {
        this.paCallBackSMSRepository = paCallBackSMSRepository;
    }
    public PAMessageCallBackSms save(PAMessageCallBackSms paMessageCallBackSms) {
        return this.paCallBackSMSRepository.save(paMessageCallBackSms);
    }
    public PAMessageCallBackSms findById(final String id) {
        return paCallBackSMSRepository.findById(id).orElse(null);
    }
    public PAMessageCallBackSms updateMessageStatus(final PAMessageCallBackSms message) {
        // find message with PAMessage Id
        // if exist => save -> it will automatice update

        return null;
    }
   public PAMessageCallBackSms findByMessageId(final int messageId) {
        return paCallBackSMSRepository.findByMessageId(messageId).orElse(null);
   }

}
