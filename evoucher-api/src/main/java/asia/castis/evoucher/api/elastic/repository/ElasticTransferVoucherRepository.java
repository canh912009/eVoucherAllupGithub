package asia.castis.evoucher.api.elastic.repository;

import asia.castis.evoucher.api.common.Constant;
import asia.castis.evoucher.api.elastic.model.TransferHistory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
@Slf4j
public class ElasticTransferVoucherRepository {
    @Value("${elastic.transfer.history.indexName}")
    private String indexCoordinates;
    @Value("${elastic.url}")
    private String elasticUrl;
    private final ElasticsearchOperations elasticsearchOperations;

    @Autowired private RestTemplate restTemplate;

    @Autowired
    public ElasticTransferVoucherRepository(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    public String createTransferVoucher(TransferHistory transferHistory) {

        IndexQuery indexQuery = new IndexQueryBuilder()
                .withId(transferHistory.getId())
                .withObject(transferHistory).build();

        return elasticsearchOperations.index(indexQuery, IndexCoordinates.of(indexCoordinates));
    }

    public String createTransferWithRefresh(TransferHistory transferHistory) {
        try {
            String url = "http://" + elasticUrl + "/" + indexCoordinates + "/_doc/" + transferHistory.getId() + "?refresh=true";

            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            String payload = Constant.gson.toJson(transferHistory);
            HttpEntity<String> requestEntity = new HttpEntity<>(payload, headers);

            log.info("Call elastic search create TransferHistory with payload: {}", payload);
            ResponseEntity<String> response =
                    restTemplate.exchange(url, HttpMethod.PUT, requestEntity, String.class);

            return response.getBody();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public boolean updateTransferVoucher(TransferHistory transferHistory) {
        try {
            UpdateQuery builder = UpdateQuery.builder(transferHistory.getId())
                    .withDocument(elasticsearchOperations.getElasticsearchConverter().mapObject(transferHistory))
                    .withDocAsUpsert(true)
                    .build();

            UpdateResponse updateResponse = elasticsearchOperations.update(builder, IndexCoordinates.of(indexCoordinates));
            return updateResponse.getResult() == UpdateResponse.Result.UPDATED;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    public TransferHistory searchByToVoucherId(String id) {
        try {
            Criteria criteria = new Criteria();


            if (id != null) {
                criteria = criteria.and("toVoucherId").is(id);
            }

            CriteriaQuery query = new CriteriaQuery(criteria);
            SearchHits<TransferHistory> searchHists = elasticsearchOperations.search(query, TransferHistory.class, IndexCoordinates.of(indexCoordinates));
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
