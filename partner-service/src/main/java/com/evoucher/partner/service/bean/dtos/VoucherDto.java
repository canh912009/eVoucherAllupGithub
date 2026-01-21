package com.evoucher.partner.service.bean.dtos;

import com.evoucher.partner.service.bean.enum_type.SystemType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class VoucherDto implements Serializable {
	private String id;
	private Double balance;
	private long campaignId;
	private Date cancelDate;
	private String content;
	private Date creationDate;
	private Double discountLimitPrice;
	private Double discountRate;
	private Date disuseDate;
	private Date expirationDate;
	private GoodsDTO goods;
	private String imgUrl;
	private Double initialAmount;
	private Date lastExchangeDate;
	private Date publishDate;
	private long publishDtlId;
	private long publishId;
	private Date regDt;
	private String shortLink;
	private String sticker;
	private String subject;
	private String testYn;
	private Date transferDate;
	private String transferStatusCode;
	private String useInfo;
	private String userMobileNumber;
	private String userName;
	private Double voucherPrice;
	private String voucherStatusCode;
	private String voucherTypeCd;
	private String origEv;
	private String transferMsg;
	private Long extPinId;
	private String extPinNo;
	private String externalPinType;
	private SystemType system;
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
}
