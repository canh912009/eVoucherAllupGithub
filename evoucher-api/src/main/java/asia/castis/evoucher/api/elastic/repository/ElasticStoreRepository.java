package asia.castis.evoucher.api.elastic.repository;

import asia.castis.evoucher.api.dto.request.StoreSearchRequest;
import asia.castis.evoucher.api.elastic.model.StoreModel;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.matchPhraseQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

@Repository
@Slf4j
public class ElasticStoreRepository {

    @Value("${elastic.store.indexName}")
    private String INDEX_COORDINATES;
    private final ElasticsearchOperations elasticsearchOperations;

    private static final Gson gson = new Gson();

    @Autowired
    public ElasticStoreRepository(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }


    public StoreModel searchById(String storeId) {
        try {
            Criteria criteria = new Criteria();

            if (storeId != null) {
                criteria = criteria.and("storeId").is(storeId);
            }

            CriteriaQuery query = new CriteriaQuery(criteria);
            SearchHits<StoreModel> searchHists = elasticsearchOperations.search(query, StoreModel.class, IndexCoordinates.of(INDEX_COORDINATES));
            if (searchHists.getTotalHits() > 0) {
                return searchHists.getSearchHit(0).getContent();
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }
}
