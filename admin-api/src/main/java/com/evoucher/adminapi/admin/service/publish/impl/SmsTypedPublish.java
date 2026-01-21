package com.evoucher.adminapi.admin.service.publish.impl;

import com.evoucher.adminapi.admin.enums.SMSType;
import com.evoucher.adminapi.admin.service.PublishServiceImpl;
import com.evoucher.adminapi.admin.service.models.PublishRequest;
import com.evoucher.adminapi.common.exception.CustomCodeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("smsTypedPublish")
@Slf4j
public class SmsTypedPublish extends DeliveryAbstractService {


    public SmsTypedPublish(DeliveryBridgeService bridgeService) {
        super(bridgeService);
    }

    @Override
    public SMSType getSMSType() {
        return SMSType.SMS;
    }
    @Override
    public void validatePublishRequest(PublishRequest publishRequest) throws CustomCodeException {
        PublishServiceImpl.validateNoDuplicateYn(publishRequest.getReceiverNoDuplicateAllowYn());
        PublishServiceImpl.validateUserList(publishRequest.getEndUsers());
        PublishServiceImpl.validateBookingDate(publishRequest.getBookingYn(), publishRequest.getBookingDate());
        // validate upload text
        PublishServiceImpl.validateUploadData(publishRequest.getUploadType(), publishRequest.getUploadFileName(), publishRequest.getUploadFilePath());
    }

}
