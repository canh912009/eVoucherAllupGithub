package asia.castis.evoucher.push.repositories;

import asia.castis.evoucher.push.model.CheckPublish;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;
import java.util.Optional;

public interface CheckPublishRepository extends ElasticsearchRepository<CheckPublish, Long> {
    Optional<CheckPublish> findByPublishId(Long publishId);

    Optional<List<CheckPublish>> findByDone(boolean done);
}
