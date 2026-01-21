package asia.castis.evoucher.push.callback_receive.zalo.repositories;


import asia.castis.evoucher.push.callback_receive.zalo.model.PAMessageCallBackZalo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Optional;

public interface PACallBackZaloRepository extends ElasticsearchRepository<PAMessageCallBackZalo, String> {
    Optional<PAMessageCallBackZalo> findByMessageId(String messageId);
}
