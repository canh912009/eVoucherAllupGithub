package asia.castis.evoucherservicefe.common.repository;

import asia.castis.evoucherservicefe.storerequest.model.StoreModel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends ElasticsearchRepository<StoreModel, String> {
}
