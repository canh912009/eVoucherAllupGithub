package asia.castis.evoucherservicefe.common.service;

import asia.castis.evoucherservicefe.common.enums.EnumPublishDetailStatus;
import asia.castis.evoucherservicefe.common.enums.EnumPublishStatus;
import asia.castis.evoucherservicefe.exceptions.ElasticSearchException;
import asia.castis.evoucherservicefe.common.model.publish.PublishModel;
import asia.castis.evoucherservicefe.exceptions.NotFoundException;

public interface EsPublishService {
    PublishModel save(PublishModel publishModelFromQueue);

    PublishModel getPublish(Long publishId) throws NotFoundException;

    void updatePublishStatus(PublishModel publish) throws ElasticSearchException;

    void delete(Long publishId);
    void rollback(Long publishId, PublishModel oldModel);

    void updatePublishForResend(Long publishId, EnumPublishStatus publishStatus,
                                Long detailsId, EnumPublishDetailStatus detailStatus, Integer voucherResendHistoryId)
            throws ElasticSearchException;

    PublishModel findByPublishDetailId(Long publishDetailId);
}
