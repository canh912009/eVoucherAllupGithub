package com.evoucher.evoucherbe.dto;

import com.evoucher.evoucherbe.common.enums.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VoucherDto {
    String eV;
    Integer publishId;
    Integer publishDetailId;
    Long goodsId;
    Integer campaignId;
    Date registDate;
    Long userId;
    String userMobileNumber;
    String userName;
    EnumValidYn testYn;
    Date creationDate;
    Date expirationDate;
    Date publishDate;
    Date lastExchangeDate;
    Date disuseDate;
    Date cancelDate;
    Date transferDate;
    String shortLink;
    VoucherTypeCode voucherTypeCode;
    String sticker;
    VoucherStatusCode voucherStatusCode;
    TransferStatusCode transferStatusCode;
    String subject;
    String content;
    String useInfo;
    String imageUrl;
    Double voucherPrice;
    Double discountRate;
    Double discountLimitPrice;
    Double initAmount;
    Double balance;
    String originalEv;
    String transferMessage;
    Long externalPinId;
    String externalPinNo;
    VoucherDisplayType externalPinType;
    SystemType system;
    String externalPinPassword;
    String contentLink;
    String contentImagePath;
    String contentImageName;
    String parentVoucherEv;
    String parentVoucherToken;
    String serialNo;
    String activationUrl;
    String activationId;
    Date activationDate;
    Integer usageCount;
    Integer usageRemainingCount;
}
