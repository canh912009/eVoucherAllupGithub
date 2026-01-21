package com.castis.pos_api.dto;

import com.castis.pos_api.enum_constant.SystemType;
import com.castis.pos_api.enum_constant.TransferStatusCode;
import com.castis.pos_api.enum_constant.VoucherStatusCode;
import com.castis.pos_api.enum_constant.VoucherTypeCode;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class VoucherDto implements Serializable {
	private String id;
	private Double balance;
	private Date expirationDate;
	private GoodsDto goods;
	private String userMobileNumber;
	private String userName;
	private Double voucherPrice;
	private VoucherStatusCode voucherStatusCode;
	private VoucherTypeCode voucherTypeCd;
	private Long extPinId;
	private String extPinNo;
	private SystemType system;
	private String externalPinPassword;
	private TransferStatusCode transferStatusCode;
}
