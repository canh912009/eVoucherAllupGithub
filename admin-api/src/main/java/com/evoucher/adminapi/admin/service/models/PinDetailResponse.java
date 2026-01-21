package com.evoucher.adminapi.admin.service.models;

import com.evoucher.adminapi.common.enums.SystemType;
import com.evoucher.adminapi.common.serializers.EncryptedFieldSerializer;
import com.evoucher.adminapi.common.utils.Constant;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class PinDetailResponse {
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
    List<CsExchangeHistoryDTO> exChangeHistories;
    Set<CsTransferHistoryDTO> transferHistories;
    final String pinPassword;
    final String parentVoucherToken;
    final String voucherTypeCode;
    final String parentVoucherEv;
    final String parentSystem;
    final String publishDetailStatusCode;
    List<ChildOfChoiceVoucherResponse> childOfChoiceVoucherList;
    final String serialNo;
    final String activationUrl;
    @JsonFormat(pattern = Constant.Common.COMMON_DATETIME_FORMAT)
    final Date activationDate;
    final String system;
    final Integer requestCount;
    @JsonIgnore
    final Integer requestingCount;
    public boolean isUnderProcess() {
        return requestingCount > 0;
    }
}
