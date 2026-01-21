package asia.castis.evoucher.push.repositories;

import asia.castis.evoucher.push.model.PAMessageCallBackSms;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Optional;

public interface PACallBackSMSRepository extends ElasticsearchRepository<PAMessageCallBackSms, String> {
    Optional<PAMessageCallBackSms> findByMessageId(String messageId);
}
