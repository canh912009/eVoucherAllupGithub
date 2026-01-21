package asia.castis.evoucher.push.callback_receive.zalo.repositories;

import asia.castis.evoucher.push.callback_receive.zalo.model.PAMessage;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Optional;

public interface PARepository extends ElasticsearchRepository<PAMessage, String> {
    Optional<PAMessage> findByMessageId(String messageId);
}
