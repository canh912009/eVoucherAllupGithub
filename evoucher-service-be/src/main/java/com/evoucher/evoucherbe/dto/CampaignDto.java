package com.evoucher.evoucherbe.dto;

import com.evoucher.evoucherbe.common.enums.ApproveStatus;
import com.evoucher.evoucherbe.common.enums.EnumValidYn;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CampaignDto {
    Integer id;
    String campaignName;
    String customerId;
    Integer customerContractId;
    Date startDate;
    Date endDate;
    String messageSubject;
    String messageContent;
    String messageCallingNumber;
    EnumValidYn validYn;
    ApproveStatus approveStatusCode;
    String approveRequestId;
    Date approveRequestDate;
    String approveId;
    Date approveDate;
}
