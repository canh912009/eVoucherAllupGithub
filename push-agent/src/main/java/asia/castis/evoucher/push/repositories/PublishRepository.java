package asia.castis.evoucher.push.repositories;

import asia.castis.evoucher.push.model.PublishModel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface PublishRepository extends ElasticsearchRepository<PublishModel, Long> {
}
