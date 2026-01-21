package asia.castis.evoucher.api.elastic.repository;

import asia.castis.evoucher.api.dto.request.PaymentHistory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
@Slf4j
public class ElasticPaymentRepository {
    @Value("${elastic.payment.history.indexName}")
    private String INDEX_COORDINATES;
    private final ElasticsearchOperations elasticsearchOperations;

    @Autowired
    public ElasticPaymentRepository(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    public String createPaymentVoucher(PaymentHistory transferHistory) {

        IndexQuery indexQuery = new IndexQueryBuilder()
                .withId(transferHistory.getId())
                .withObject(transferHistory).build();

        return elasticsearchOperations.index(indexQuery, IndexCoordinates.of(INDEX_COORDINATES));
    }

    public boolean updatePaymentVoucher(PaymentHistory transferHistory) {
        UpdateQuery builder = UpdateQuery.builder(transferHistory.getId())
                .withDocument(elasticsearchOperations.getElasticsearchConverter().mapObject(transferHistory))
                .withDocAsUpsert(true)
                .build();

        UpdateResponse updateResponse = elasticsearchOperations.update(builder, IndexCoordinates.of(INDEX_COORDINATES));
        return updateResponse.getResult() == UpdateResponse.Result.UPDATED;
    }

    public List<PaymentHistory> getListPaymentByVoucherId(String voucherId) {
        try {
            Criteria criteria = new Criteria();

            if (voucherId != null) {
                criteria = criteria.and("voucherId").is(voucherId);
            }

            CriteriaQuery query = new CriteriaQuery(criteria);
            SearchHits<PaymentHistory> searchHists = elasticsearchOperations.search(query, PaymentHistory.class, IndexCoordinates.of(INDEX_COORDINATES));
            List<PaymentHistory> listVouchers = new ArrayList<>();

            searchHists.getSearchHits().forEach(searchHit -> listVouchers.add(searchHit.getContent()));
            return listVouchers;

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    public PaymentHistory searchById(String id) {
        Criteria criteria = new Criteria();

        if (id != null) {
            criteria = criteria.and("id").is(id);
        }

        CriteriaQuery query = new CriteriaQuery(criteria);
        SearchHits<PaymentHistory> searchHists = elasticsearchOperations.search(query, PaymentHistory.class, IndexCoordinates.of(INDEX_COORDINATES));
        if (searchHists.getTotalHits() > 0) {
            return searchHists.getSearchHit(0).getContent();
        } else {
            return null;
        }
    }

    public String delete(String paymentHistoryId) {
        return elasticsearchOperations.delete(paymentHistoryId, IndexCoordinates.of(INDEX_COORDINATES));
    }
}
