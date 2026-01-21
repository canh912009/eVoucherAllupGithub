package asia.castis.evoucherservicefe.common.repository;

import asia.castis.evoucherservicefe.common.model.publish.PublishModel;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


public interface PublishRepository extends ElasticsearchRepository<PublishModel, Long> {
    @Query("{\"bool\": {\"must\": [{\"match\": {\"publishDetails.id\": \"?0\"}}]}}")
    PublishModel findByPublishDetailId(Long publishDetailsId);
}
