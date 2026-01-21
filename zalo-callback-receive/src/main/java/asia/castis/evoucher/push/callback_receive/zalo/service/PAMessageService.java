package asia.castis.evoucher.push.callback_receive.zalo.service;

import asia.castis.evoucher.push.callback_receive.zalo.model.PAMessage;
import asia.castis.evoucher.push.callback_receive.zalo.repositories.PARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PAMessageService {
    private final PARepository repository;
    @Autowired
    public PAMessageService(PARepository repository) {
        this.repository = repository;
    }
    public PAMessage save(final PAMessage message) {
        return repository.save(message);
    }
    public PAMessage findById(final String id) {
        return repository.findById(id).orElse(null);
    }
    public PAMessage findByMessageId(final String messageId) {
        return repository.findByMessageId(messageId).orElse(null);
    }
    public PAMessage updateMessageStatus(final PAMessage message) {
        // find message with PAMessage Id
        // if exist => save -> it will automatice update

        return null;
    }
    // get list PAMesage by publishScheduleId
    // get list PAMesage by campaignId
    // get list PAMesage by publishScheduleId and messageType...
    // sending back static of list PAMessage which sending by (1)

}
