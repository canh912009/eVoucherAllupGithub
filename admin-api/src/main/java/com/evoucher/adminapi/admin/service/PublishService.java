package com.evoucher.adminapi.admin.service;

import com.evoucher.adminapi.admin.service.models.*;
import com.evoucher.adminapi.cms.service.models.request.EndUserRequest;
import com.evoucher.adminapi.partner.service.model.request.ExternalPublishConvert;
import com.evoucher.adminapi.partner.service.model.response.ExternalPublishOrderPinResponse;
import com.evoucher.adminapi.partner.service.model.response.ExternalPublishResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface PublishService {
    PublishDTO findById(Integer id);

    PublishDTO createPublish(PublishRequest publishRequest);

    List<EndUserRequest> generatePaperTypedEndUsers(PublishRequest request);

    PublishDTO updatePublish(Integer id, PublishRequest publishRequest);

    Integer deletePublishById(Integer id);

    Integer updateStatusPublish(Integer id, ApproveRequest approveRequest);

    Page<SearchPublishResponse> searchPublish(FilterSearchAdmin filterSearchAdmin);

    ExternalPublishResponse createListPublishForCustomerChannel(ExternalPublishConvert externalPublish);

    ExternalPublishResponse getExternalPublishWithTransactionId(String transactionId);

    ExternalPublishOrderPinResponse getExternalPublishWithOrderId(Integer orderId, String customerId);

    void cancelExternalPublishForCustomerChannel(UUID transactionId);

    void cancelExternalPublishForCustomerChannel(Integer orderId, String customerId);
    List<EndUserRequest> generateDownloadTypedEndUsers(PublishRequest request);

}
