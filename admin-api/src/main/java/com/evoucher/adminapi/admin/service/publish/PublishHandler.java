package com.evoucher.adminapi.admin.service.publish;

import com.evoucher.adminapi.admin.enums.SMSType;
import com.evoucher.adminapi.admin.service.models.PublishDTO;

public interface PublishHandler {
    void setTypeSpecificMetaData(SMSType smsType, PublishDTO publishDTO);
}
