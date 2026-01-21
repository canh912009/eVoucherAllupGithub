package asia.castis.evoucherservicefe.common.service.impl;

import asia.castis.evoucherservicefe.common.service.EsStoreService;
import asia.castis.evoucherservicefe.common.utils.JsonMapper;
import asia.castis.evoucherservicefe.storerequest.model.StoreModel;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexedObjectInformation;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class EsStoreServiceImpl implements EsStoreService {
    @Value("${elastic.store.indexName}")
    private String indexName;
    private ElasticsearchRestTemplate elasticsearchTemplate;
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    public EsStoreServiceImpl(ElasticsearchRestTemplate elasticsearchTemplate, RestHighLevelClient restHighLevelClient) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.restHighLevelClient = restHighLevelClient;
    }

    @Override
    public void insertAll(List<StoreModel> storeModels) {
        List<IndexQuery> indexQueries = new ArrayList<>();
        for (StoreModel store : storeModels) {
            IndexQuery indexQuery = new IndexQueryBuilder()
                    .withIndex(indexName)
                    .withId(store.getStoreId())
                    .withObject(store)
                    .build();
            indexQueries.add(indexQuery);
        }
        List<IndexedObjectInformation> results = elasticsearchTemplate.bulkIndex(indexQueries, StoreModel.class);
        log.info("=============== All stores inserted, size={}/{} ===============", results.size(), storeModels.size());
    }

    @Override
    public int updateAll(List<StoreModel> storeModels) throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        for (StoreModel store : storeModels) {
            String storeJson = JsonMapper.getInstance().writeValueAsString(store);
            UpdateRequest updateRequest = new UpdateRequest(indexName, store.getStoreId()).doc(storeJson, XContentType.JSON);
            bulkRequest.add(updateRequest);
        }
        BulkResponse bulkResponse = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        int updatedCount = bulkResponse.getItems().length;
        if (bulkResponse.hasFailures()) {
            log.warn("Some stores can not be updated, errorMsg={}", bulkResponse.buildFailureMessage());
        } else {
            log.info("=============== All stores updated, {}/{} ===============", updatedCount, storeModels.size());
        }
        return updatedCount;
    }

    @Override
    public int deleteAll(List<String> storeIds) throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        for (String storeId : storeIds) {
            DeleteRequest deleteRequest = new DeleteRequest(indexName, storeId);
            bulkRequest.add(deleteRequest);
        }
        BulkResponse bulkResponse = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        int deletedCount = bulkResponse.getItems().length;
        if (bulkResponse.hasFailures()) {
            log.warn("Some stores can not be deleted {}/{}, errorMsg={}", deletedCount, storeIds.size(), bulkResponse.buildFailureMessage());
        } else {
            log.info("=============== All stores deleted, {}/{} ===============", deletedCount, storeIds.size());
        }
        return deletedCount;
    }
}
