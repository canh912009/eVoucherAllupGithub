package com.castis.publishservice.dto.queue;

import com.castis.publishservice.utils.enum_template.SystemType;
import com.castis.publishservice.utils.enum_template.VoucherDisplayType;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class VoucherRequest {
    private String id;
    private String shortLink;
    private String voucherType;
    private GoodsRequest goods;
    private String userName; // encrypted
    private String userMobileNumber; // encrypted
    private String userEmail;
    private String subject;
    private String content;
    private String useInfo;
    private String sticker;
    private String voucherStatus;
    private String transferStatus;
    private Double voucherPrice;
    private Double discountRate;
    private Double discountLimitPrice;
    private Double initialAmount;
    private Double balance;
    private String createDate;
    private String expireDate;
    private String publishDate;
    private String lastExchangeDate;
    private String disuseDate;
    private String cancelDate;
    private String transferDate;
    private String transferMessage;
    private String originalVoucherId;
    private Long publishDetailId;
    private SystemType system;
    private Long extPinId;
    private String extPinNo;
    private String contentLink;
    private String contentImagePath;
    private String contentImageName;
    private String externalPinPassword;
    private String parentVoucherEv;
    private String parentVoucherToken;
    private String serialNo;
    private String activationUrl;
    private Date activationDate;
    private String activationId;
    private VoucherDisplayType extPinType;
}
