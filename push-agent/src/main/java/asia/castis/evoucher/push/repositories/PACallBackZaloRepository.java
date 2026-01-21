package asia.castis.evoucher.push.repositories;

import asia.castis.evoucher.push.model.PAMessageCallBackZalo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Optional;

public interface PACallBackZaloRepository extends ElasticsearchRepository<PAMessageCallBackZalo, String> {
    Optional<PAMessageCallBackZalo> findByMessageId(String messageId);
}
