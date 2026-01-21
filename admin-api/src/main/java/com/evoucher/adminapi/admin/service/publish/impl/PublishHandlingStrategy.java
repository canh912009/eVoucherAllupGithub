package com.evoucher.adminapi.admin.service.publish.impl;

import com.evoucher.adminapi.admin.dao.models.Publish;
import com.evoucher.adminapi.admin.enums.SMSType;
import com.evoucher.adminapi.admin.service.models.PublishDTO;
import com.evoucher.adminapi.admin.service.models.PublishRequest;
import com.evoucher.adminapi.admin.service.publish.PublishHandler;
import com.evoucher.adminapi.cms.dao.models.EndUser;
import com.evoucher.adminapi.common.exception.CustomCodeException;
import org.springframework.stereotype.Service;

import java.util.List;

public interface PublishHandlingStrategy {
    void setTypeSpecificMetaData(PublishDTO publishDTO);
    SMSType getSMSType();
    void validatePublishRequest(PublishRequest publishRequest) throws CustomCodeException;
    void setUserInfoToPublishRequest(PublishRequest publishRequest);
    boolean isUsingUserInfoType(SMSType type);
    boolean isMetaDataPublishing();
}
