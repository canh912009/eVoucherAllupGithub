package com.evoucher.adminapi.admin.service.models;

import com.evoucher.adminapi.common.serializers.EncryptedFieldSerializer;
import com.evoucher.adminapi.common.utils.Constant;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CsManagementResponse {
    String voucherUUID;
    String campaignName;
    String deliveryName;
    String productName;
    Long productId;
    @JsonFormat(pattern = Constant.Common.COMMON_DATETIME_FORMAT)
    Date expireDate;
    @JsonSerialize(using = EncryptedFieldSerializer.class)
    String targetNumber;
    String targetEmail;
    @JsonSerialize(using = EncryptedFieldSerializer.class)
    String targetName;
    String pin;
    String pinStatus;
    String accessLink;
    @JsonFormat(pattern= Constant.Common.COMMON_DATETIME_FORMAT)
    Date deliveryDate;
    String voucherType;

    String status;
    String otp;

    Integer remainingCount;
    Double remainingBalance;
    String password;

    String system;


    // for vnpt type
    String transactionId;
    Long faceValue;
    String cardSerial;
    String cardPin;
    String topupNumber;
    String provider;
    String requestId;
}
