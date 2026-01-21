package asia.castis.evoucher.push.callback_receive.sms.repositories;

import asia.castis.evoucher.push.callback_receive.sms.model.PAMessageCallBackSms;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Optional;

public interface PACallBackSMSRepository extends ElasticsearchRepository<PAMessageCallBackSms, String> {
    Optional<PAMessageCallBackSms> findByMessageId(int messageId);
}
