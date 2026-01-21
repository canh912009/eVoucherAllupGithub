package asia.castis.evoucher.push.service;

import asia.castis.evoucher.push.model.PAMessageCallBackZalo;
import asia.castis.evoucher.push.repositories.PACallBackZaloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PAMessageCallBackZaloService {
    private final PACallBackZaloRepository paCallBackZaloRepository;
    @Autowired
    public PAMessageCallBackZaloService(PACallBackZaloRepository paCallBackZaloRepository) {
        this.paCallBackZaloRepository = paCallBackZaloRepository;
    }
    public PAMessageCallBackZalo save(final PAMessageCallBackZalo paMessageCallBackZalo) {
        return paCallBackZaloRepository.save(paMessageCallBackZalo);
    }
    public PAMessageCallBackZalo findById(final String id) {
        return paCallBackZaloRepository.findById(id).orElse(null);
    }
    public PAMessageCallBackZalo updateMessageStatus(final PAMessageCallBackZalo message) {
        // find message with PAMessage Id
        // if exist => save -> it will automatice update

        return null;
    }
    // get list PAMesage by publishScheduleId
    // get list PAMesage by campaignId
    // get list PAMesage by publishScheduleId and messageType...
    // sending back static of list PAMessage which sending by (1)
    public PAMessageCallBackZalo findByMessageId(final String messageId) {
        return paCallBackZaloRepository.findByMessageId(messageId).orElse(null);
    }
}
