package com.evoucher.adminapi.admin.service.models.search_response;

import com.evoucher.adminapi.common.serializers.EncryptedFieldSerializer;
import com.evoucher.adminapi.common.utils.Common;
import com.evoucher.adminapi.common.utils.Constant;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class OperatorSearchRes {
    Long requestId;
    Long publishId;
    String publishName;
    String customerId;
    String customerName;
    String ev;
    @JsonSerialize(using = EncryptedFieldSerializer.class)
    String targetName;
    @JsonSerialize(using = EncryptedFieldSerializer.class)
    String targetNumber;
    String requestStatus;
    @JsonFormat(pattern = Constant.Common.COMMON_DATETIME_FORMAT)
    Date requestDate;
}
