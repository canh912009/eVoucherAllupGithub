package asia.castis.evoucher.push.service;

import asia.castis.evoucher.push.model.PAMessageCallBackSms;
import asia.castis.evoucher.push.repositories.PACallBackSMSRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PAMessageCallBackSmsService {
    private final PACallBackSMSRepository paCallBackSMSRepository;
    @Autowired
    public PAMessageCallBackSmsService(PACallBackSMSRepository paCallBackSMSRepository) {
        this.paCallBackSMSRepository = paCallBackSMSRepository;
    }
    public PAMessageCallBackSms save(final PAMessageCallBackSms paMessageCallBackSms) {
        return paCallBackSMSRepository.save(paMessageCallBackSms);
    }
    public PAMessageCallBackSms findById(final String id) {
        return paCallBackSMSRepository.findById(id).orElse(null);
    }
    public PAMessageCallBackSms updateMessageStatus(final PAMessageCallBackSms message) {
        // find message with PAMessage Id
        // if exist => save -> it will automatice update

        return null;
    }
    // get list PAMesage by publishScheduleId
    // get list PAMesage by campaignId
    // get list PAMesage by publishScheduleId and messageType...
    // sending back static of list PAMessage which sending by (1)
    public PAMessageCallBackSms findByMessageId(final String messageId) {
        return paCallBackSMSRepository.findByMessageId(messageId).orElse(null);
    }
}
