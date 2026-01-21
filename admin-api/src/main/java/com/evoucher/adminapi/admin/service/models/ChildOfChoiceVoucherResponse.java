package com.evoucher.adminapi.admin.service.models;

import com.evoucher.adminapi.common.serializers.EncryptedFieldSerializer;
import com.evoucher.adminapi.common.utils.Constant;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class ChildOfChoiceVoucherResponse {
    final String voucherUUID;
    final String campaignName;
    @JsonSerialize(using = EncryptedFieldSerializer.class)
    final String targetName;
    @JsonSerialize(using = EncryptedFieldSerializer.class)
    final String targetNumber;
    final String targetEmail;
    final Long campaignId;
    final String accessLink;
    final String deliveryName;
    final String pin;
    final Long deliveryId;
    final String pinStatus;
    final String productName;
    @JsonFormat(pattern = Constant.Common.COMMON_DATETIME_FORMAT)
    final Date deliveryDate;
    @JsonFormat(pattern = Constant.Common.COMMON_DATE_FORMAT)
    final Date startDate;
    @JsonFormat(pattern = Constant.Common.COMMON_DATE_FORMAT)
    final Date endDate;
    @JsonFormat(pattern = Constant.Common.COMMON_DATE_FORMAT)
    final Date exchangeDate;
    final String messageType;
    final String result;
    final String pinPassword;
    final String voucherTypeCode;
}
