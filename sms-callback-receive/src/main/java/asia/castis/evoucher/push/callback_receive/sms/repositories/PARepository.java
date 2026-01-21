package asia.castis.evoucher.push.callback_receive.sms.repositories;

import asia.castis.evoucher.push.callback_receive.sms.model.PAMessage;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Optional;

public interface PARepository extends ElasticsearchRepository<PAMessage, String> {
    Optional<PAMessage> findByMessageId(int messageId);
    Optional<PAMessage> findByPublishScheduleId(int publishScheduleId);
}
