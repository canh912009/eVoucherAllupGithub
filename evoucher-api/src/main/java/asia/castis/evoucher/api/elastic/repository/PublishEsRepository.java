package asia.castis.evoucher.api.elastic.repository;

import asia.castis.evoucher.api.elastic.model.publish.PublishModel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface PublishEsRepository extends ElasticsearchRepository<PublishModel, Long> {
    PublishModel findByActivationUrlEndingWith(String activationUrl);
}
