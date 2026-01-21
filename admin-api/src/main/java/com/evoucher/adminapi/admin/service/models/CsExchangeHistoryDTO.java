package com.evoucher.adminapi.admin.service.models;

import com.evoucher.adminapi.common.serializers.EncryptedFieldSerializer;
import com.evoucher.adminapi.common.utils.Constant;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CsExchangeHistoryDTO {
    Long id;
    @JsonFormat(pattern = Constant.Common.COMMON_DATE_FORMAT)
    Date exchangeDate;
    String storeId;
    String storeName;
    @JsonSerialize(using = EncryptedFieldSerializer.class)
    String storeStaff;
    String pinStatus;
    String exchangeType;
    Double exchangeAmount;
    Integer remainingCount;
}
