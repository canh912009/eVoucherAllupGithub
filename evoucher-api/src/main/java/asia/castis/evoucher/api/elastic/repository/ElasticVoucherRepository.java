package asia.castis.evoucher.api.elastic.repository;

import asia.castis.evoucher.api.common.Encryption;
import asia.castis.evoucher.api.dto.request.UserVoucherListRequest;
import asia.castis.evoucher.api.elastic.model.VoucherModel;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class ElasticVoucherRepository {
    @Value("${elastic.voucher.indexName}")
    private String INDEX_COORDINATES;
    private final ElasticsearchOperations elasticsearchOperations;

    @Autowired
    Encryption encryption;

    @Autowired
    public ElasticVoucherRepository(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    public String createVoucher(VoucherModel voucherModel) {

        IndexQuery indexQuery = new IndexQueryBuilder().withId(voucherModel.getId()).withObject(voucherModel).build();

        String data = elasticsearchOperations.index(indexQuery, IndexCoordinates.of(INDEX_COORDINATES));
        return data;
    }

    public VoucherModel searchById(String voucherId) {
        try {
            Criteria criteria = new Criteria();

            if (voucherId != null) {
                criteria = criteria.and("id").is(voucherId);
            }

            CriteriaQuery query = new CriteriaQuery(criteria);
            SearchHits<VoucherModel> searchHists = elasticsearchOperations.search(query, VoucherModel.class, IndexCoordinates.of(INDEX_COORDINATES));
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

    public List<VoucherModel> findAllByIdIn(List<String> ids) {
        try {
            Criteria criteria = new Criteria();

            if (ids != null && !ids.isEmpty()) {
                criteria = Criteria.where("id").in(ids);
            } else {
                return null;
            }

            CriteriaQuery query = new CriteriaQuery(criteria);
            SearchHits<VoucherModel> searchHists = elasticsearchOperations.search(query, VoucherModel.class, IndexCoordinates.of(INDEX_COORDINATES));
            log.info("searchHists.getTotalHits() {}", searchHists.getTotalHits());
            if (searchHists.getTotalHits() > 0) {
                log.info("search hits: {}", searchHists.getSearchHits());
                return searchHists.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public VoucherModel searchId(String voucherId) {
        try {
            QueryBuilder queryBuilder = QueryBuilders.matchQuery("id", voucherId);

            Query searchQuery = new NativeSearchQueryBuilder().withQuery(queryBuilder).build();

            SearchHits<VoucherModel> searchHists = elasticsearchOperations.search(searchQuery, VoucherModel.class, IndexCoordinates.of(INDEX_COORDINATES));
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

    public List<VoucherModel> userVoucherList(UserVoucherListRequest request) {

        try {
            Criteria criteria = new Criteria();
            Pageable pageable = PageRequest.of(request.getPageNum(), request.getPageSize());

            if (request.getMobileNumber() != null) {
                criteria = criteria.and("userMobileNumber").is(encryption.encryptData(request.getMobileNumber()));
            }
//            if (request.getSupplierName() != null) {
//                criteria = criteria.and("publishDetail.publish.supplier.name").contains(request.getSupplierName());
//            }
//            if (request.getBrandName() != null) {
//                criteria = criteria.and("publishDetail.publish.goods.brand.name").contains(request.getBrandName());
//            }

            CriteriaQuery query = new CriteriaQuery(criteria);
            Sort sort = Sort.by(Sort.Direction.DESC, "createDate");
            query.addSort(sort);
            query.setPageable(pageable);
            SearchHits<VoucherModel> searchHists = elasticsearchOperations.search(query, VoucherModel.class, IndexCoordinates.of(INDEX_COORDINATES));
            List<VoucherModel> listVouchers = new ArrayList<>();

            searchHists.getSearchHits().forEach(searchHit -> {
                listVouchers.add(searchHit.getContent());
            });
            return listVouchers;

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    public int getCountUserVoucher(String userMobileNumber) {

        try {
            Criteria criteria = new Criteria();

            if (userMobileNumber != null) {
                criteria = criteria.and("userMobileNumber").is(encryption.encryptData(userMobileNumber));
            }

            CriteriaQuery query = new CriteriaQuery(criteria);
            long numberOfPages = elasticsearchOperations.count(query, IndexCoordinates.of(INDEX_COORDINATES));

            return (int) numberOfPages;

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return 0;
        }
    }

    public boolean updateVoucher(VoucherModel voucher) {
        UpdateQuery builder = UpdateQuery.builder(voucher.getId()).withDocument(elasticsearchOperations.getElasticsearchConverter().mapObject(voucher)).withDocAsUpsert(true).build();

        UpdateResponse updateResponse = elasticsearchOperations.update(builder, IndexCoordinates.of(INDEX_COORDINATES));
        return updateResponse.getResult() == UpdateResponse.Result.UPDATED;
    }
}
