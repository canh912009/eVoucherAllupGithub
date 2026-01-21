package asia.castis.evoucher.push.callback_receive.zalo.service;

import asia.castis.evoucher.push.callback_receive.zalo.model.PAMessageCallBackZalo;
import asia.castis.evoucher.push.callback_receive.zalo.repositories.PACallBackZaloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PAMessageCallBackZaloService {
    private PACallBackZaloRepository paCallBackZaloRepository;
    @Autowired
    public PAMessageCallBackZaloService(PACallBackZaloRepository paCallBackZaloRepository) {
        this.paCallBackZaloRepository = paCallBackZaloRepository;
    }
    public PAMessageCallBackZalo save(PAMessageCallBackZalo paMessageCallBackZalo) {
        return paCallBackZaloRepository.save(paMessageCallBackZalo);
    }
    public PAMessageCallBackZalo findById(String id) {
        return paCallBackZaloRepository.findById(id).orElse(null);
    }
    public PAMessageCallBackZalo updateMessageStatus(PAMessageCallBackZalo message) {
        // find message with PAMessage Id
        // if exist => save -> it will automatically update

        return null;
    }
   public PAMessageCallBackZalo findByMessageId(final String messageId) {
        return paCallBackZaloRepository.findByMessageId(messageId).orElse(null);
   }

}
