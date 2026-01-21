package asia.castis.evoucher.api.elastic.repository;

import asia.castis.evoucher.api.elastic.model.publish.PublishModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.stereotype.Repository;

@Configuration
@Repository
@Slf4j
public class ElasticPublishRepository {
    @Value("${elastic.publish.indexName}")
    private String INDEX_COORDINATES;
    private final ElasticsearchOperations elasticsearchOperations;

    @Autowired
    public ElasticPublishRepository(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    public PublishModel searchByPublishDetailId(Long id) {
        try {
            Criteria criteria = new Criteria();


            if (id != null) {
                criteria = criteria.and("publishDetails.id").is(id);
            }

            CriteriaQuery query = new CriteriaQuery(criteria);
            SearchHits<PublishModel> searchHists = elasticsearchOperations.search(query, PublishModel.class, IndexCoordinates.of(INDEX_COORDINATES));
            if (searchHists.getTotalHits() > 0) {
                return searchHists.getSearchHit(0).getContent();
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();
            return null;
        }
    }

    public String createPublish(PublishModel publishModel) {

        IndexQuery indexQuery = new IndexQueryBuilder().withId(publishModel.getId().toString()).withObject(publishModel).build();

        return elasticsearchOperations.index(indexQuery, IndexCoordinates.of(INDEX_COORDINATES));
    }
}
