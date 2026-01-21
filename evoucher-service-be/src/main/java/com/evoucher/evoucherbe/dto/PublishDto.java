package com.evoucher.evoucherbe.dto;

import com.evoucher.evoucherbe.common.enums.*;
import com.evoucher.evoucherbe.entity.Campaign;
import com.evoucher.evoucherbe.entity.Goods;
import com.evoucher.evoucherbe.entity.Supplier;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PublishDto {
    Integer id;
    CampaignDto campaign;
    GoodDto goods;
    String publishName;
    String messageSubject;
    String messageContent;
    String messageCallingNumber;
    EnumValidYn bookingYn;
    Date bookingDate;
    EnumValidYn testSendYn;
    EnumValidYn receiverNoDuplicateAllowYn;
    UploadDataType uploadType;
    String uploadFilePath;
    String uploadFileName;
    String uploadText;
    SMSType smsType;
    Supplier supplier;
    String customerId;
    Double sellPrice;
    Double sellListPrice;
    Double sellDiscountRate;
    Double sellDiscountAmount;
    Double sellCommissionRate;
    EnumValidYn sellVatIncludeYn;
    SettlementMethodCode sellSettlementMethodCode;
    Double sendCost;
    Date publishDate;
    Date cancelDate;
    String publishStatusCode;
    ApproveStatus approveStatusCode;
    String approveRequestId;
    Date approveRequestDate;
    String approveId;
    Date approveDate;
    String rejectId;
    Date rejectDate;
    String rejectReason;
    String transactionId;
    Date regDt;
    Date updtDt;
    String contentLink;
    String contentImagePath;
    String contentImageName;
}
