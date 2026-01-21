package com.evoucher.adminapi.admin.service.publish.impl;

import com.evoucher.adminapi.admin.enums.SMSType;
import com.evoucher.adminapi.admin.service.models.PublishDTO;
import com.evoucher.adminapi.admin.service.publish.PublishHandler;
import com.evoucher.adminapi.common.config.PublishHandlingStrategyFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PublishHandlerImpl implements PublishHandler {
    PublishHandlingStrategyFactory factory;

    public void setTypeSpecificMetaData(SMSType smsType, PublishDTO publishDTO) {
        factory.getStrategy(smsType).setTypeSpecificMetaData(publishDTO);
    }
}
