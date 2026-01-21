package com.evoucher.adminapi.admin.service.publish.impl;

import com.evoucher.adminapi.admin.enums.SMSType;
import com.evoucher.adminapi.admin.service.models.PublishDTO;
import com.evoucher.adminapi.admin.service.models.PublishRequest;
import com.evoucher.adminapi.cms.service.models.EndUserDTO;
import com.evoucher.adminapi.common.exception.CustomCodeException;
import com.evoucher.adminapi.common.utils.Constant;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public abstract class DeliveryAbstractService implements PublishHandlingStrategy {
    protected final DeliveryBridgeService bridgeService;

    @Override
    public void setUserInfoToPublishRequest(PublishRequest publishRequest) {
    }
    @Override
    public boolean isUsingUserInfoType(SMSType type) throws CustomCodeException {
        switch (type) {
            case DOWNLOAD:
            case PAPER:
                return true;
            case ZALO:
            case SMS:
            case EMAIL:
                return false;
            default:
                log.error("{} type is not supported", type);
                throw new CustomCodeException(
                        type + " type is not supported", HttpStatus.INTERNAL_SERVER_ERROR
                );
        }
    }

    @Override
    public void setTypeSpecificMetaData(PublishDTO publishDTO) {
        List<EndUserDTO> endUsers = bridgeService.publishDetailService.publishMetaDataDetail(publishDTO.getId());
        // Set end user info
        if (CollectionUtils.isEmpty(endUsers)) {
            String endUserStringData = publishDTO.getUploadText();
            log.info("List endUser: {}", endUserStringData);
            Type listType = new TypeToken<ArrayList<EndUserDTO>>() {
            }.getType();
            endUsers = Constant.gson.fromJson(endUserStringData, listType);
        } else {
            endUsers.forEach(o -> {
                if (StringUtils.isNotBlank(o.getUserMobileNum())) { // decrypt user mobile number
                    o.setUserMobileNum(bridgeService.propertyConverter.convertToEntityAttribute(o.getUserMobileNum()));
                }
                if (StringUtils.isNotBlank(o.getUserNm())) { // decrypt user mobile number
                    o.setUserNm(bridgeService.propertyConverter.convertToEntityAttribute(o.getUserNm()));
                }
            });
        }
        publishDTO.setEndUsers(endUsers);
    }

    @Override
    public boolean isMetaDataPublishing() {
        return false;
    }
}
