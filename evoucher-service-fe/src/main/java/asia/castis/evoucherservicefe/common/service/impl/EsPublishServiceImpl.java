package asia.castis.evoucherservicefe.common.service.impl;

import asia.castis.evoucherservicefe.common.enums.EnumPublishDetailStatus;
import asia.castis.evoucherservicefe.common.enums.EnumPublishStatus;
import asia.castis.evoucherservicefe.common.model.publish.PublishDetailModel;
import asia.castis.evoucherservicefe.common.utils.JsonMapper;
import asia.castis.evoucherservicefe.exceptions.ElasticSearchException;
import asia.castis.evoucherservicefe.common.model.publish.PublishModel;
import asia.castis.evoucherservicefe.common.repository.PublishRepository;
import asia.castis.evoucherservicefe.common.service.EsPublishService;
import asia.castis.evoucherservicefe.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service("esPublishService")
@Slf4j
public class EsPublishServiceImpl implements EsPublishService {

    private final PublishRepository publishRepository;
    private final ElasticsearchOperations elasticsearchOperations;
    @Value("${elastic.publish.indexName}")
    private String indexName;

    @Autowired
    public EsPublishServiceImpl(PublishRepository publishRepository,
                                ElasticsearchOperations elasticsearchOperations) {
        this.publishRepository = publishRepository;
        this.elasticsearchOperations = elasticsearchOperations;
    }

    @Override
    public PublishModel save(PublishModel publish) {
        PublishModel modelFromES;
        modelFromES = publishRepository.save(publish);
        log.info("=============== Publish saved to ES ===============");
        log.info("JSON={}", JsonMapper.safeWriteValueAsString(modelFromES));
        return modelFromES;
    }

    @Override
    public PublishModel getPublish(Long publishId) throws NotFoundException {
        Optional<PublishModel> publishModelOptional;
        publishModelOptional = publishRepository.findById(publishId);
        if (publishModelOptional.isEmpty()) {
            throw new NotFoundException(String.format("Can not find publish id=%d", publishId));
        }
        return publishModelOptional.get();
    }

    @Override
    public void updatePublishStatus(PublishModel publish) throws ElasticSearchException {
        try {
            PublishModel esPublish = elasticsearchOperations.get(String.valueOf(publish.getId()), PublishModel.class);
            if (esPublish == null) {
                throw new NotFoundException(String.valueOf(publish.getId()));
            }
            esPublish.setPublishStatusCode(publish.getPublishStatusCode());
            UpdateQuery updateQuery = UpdateQuery.builder(indexName)
                    .withIndex(String.valueOf(publish.getId()))
                    .build();
            log.info("Publish update to ES {}", JsonMapper.safeWriteValueAsString(publish));
            elasticsearchOperations.update(updateQuery, IndexCoordinates.of(indexName));
        } catch (OptimisticLockingFailureException e) {
            throw new ElasticSearchException(String.format("Locking exception msg=%s", e.getMessage()), e);
        } catch (Exception e) {
            throw new ElasticSearchException(String.format("Exception msg=%s", e.getMessage()), e);
        }
    }

    @Override
    public void delete(Long publishId) {
        log.info("Deleting publish={}", publishId);
        publishRepository.deleteById(publishId);
        log.info("Deleted publish={}", publishId);
    }

    @Override
    public void rollback(Long publishId, PublishModel oldModel) {
        try {
            log.info("try to rollback publish: {}, to {}", publishId, oldModel);
            if (oldModel == null) {
                publishRepository.deleteById(publishId);
            } else {
                publishRepository.save(oldModel);
            }
            log.info("rollback is OK");
        } catch (Exception e) {
            log.error("ERROR when rollback publish with id {}, and old model : {}", publishId, oldModel == null ? "" : oldModel.toString());
            throw e;
        }
    }

    @Override
    public void updatePublishForResend(Long publishId, EnumPublishStatus publishStatus,
                                       Long detailsId, EnumPublishDetailStatus detailStatus, Integer voucherResendHistoryId)
            throws ElasticSearchException {
        try {
            PublishModel esPublish = elasticsearchOperations.get(String.valueOf(publishId), PublishModel.class);
            if (esPublish == null) {
                throw new NotFoundException(String.valueOf(publishId));
            }
            esPublish.setPublishStatusCode(publishStatus);
            for (PublishDetailModel publishDetail : esPublish.getPublishDetails()) {
                if (Objects.equals(publishDetail.getId(), detailsId)) {
                    publishDetail.setPublishStatusCode(detailStatus);
                    if (voucherResendHistoryId != null) {
                        publishDetail.setVoucherResendHistoryId(voucherResendHistoryId);
                    }
                }
            }
            publishRepository.save(esPublish);
            log.info("Publish update status {}={}, detail {}={}, historyId={}",
                    publishId, publishStatus, detailsId, detailStatus, voucherResendHistoryId);
        } catch (OptimisticLockingFailureException e) {
            throw new ElasticSearchException(String.format("Locking exception msg=%s", e.getMessage()), e);
        } catch (Exception e) {
            throw new ElasticSearchException(String.format("Exception msg=%s", e.getMessage()), e);
        }
    }

    @Override
    public PublishModel findByPublishDetailId(Long publishDetailId) {
        return publishRepository.findByPublishDetailId(publishDetailId);
    }
}
