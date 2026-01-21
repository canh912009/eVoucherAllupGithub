package com.evoucher.adminapi.admin.service.models;

import com.evoucher.adminapi.common.enums.EnumValidYn;
import com.evoucher.adminapi.common.enums.GoodsType;
import com.evoucher.adminapi.common.enums.PinDisplayType;
import com.evoucher.adminapi.common.enums.SystemType;
import com.evoucher.adminapi.common.utils.Constant;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VoucherDto {
    String eV;
    Integer publishId;
    Integer publishDetailId;
    Integer goodsId;
    Integer campaignId;
    Date registDate;
    String userMobileNumber;

    Long userId;

    String userName;
    EnumValidYn testYn;
    Date creationDate;
    @DateTimeFormat(pattern = Constant.Common.COMMON_DATE_FORMAT)
    Date expirationDate;
    Date publishDate;
    Date lastExchangeDate;
    Date disuseDate;
    Date cancelDate;
    Date transferDate;
    String shortLink;
    GoodsType voucherTypeCode;
    String sticker;
    String voucherStatusCode;
    String transferStatusCode;
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
    Integer externalPinId;
    String externalPinNo;
    PinDisplayType externalPinType;
    SystemType systemType;
    String parentVoucherEv;
    String parentVoucherToken;
    String serialNo;
    String activationUrl;
    Date activationDate;
    String activationId;

    Integer usageCount;
    Integer usageRemainingCount;
}
