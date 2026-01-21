package com.evoucher.evoucherbe.common.models;

import com.evoucher.evoucherbe.utils.Constant;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class BaseDTO {
    private String regId;
    @JsonFormat(pattern= Constant.Common.COMMON_DATETIME_FORMAT)
    private Date regDt;
    private String updtId;
    @JsonFormat(pattern=Constant.Common.COMMON_DATETIME_FORMAT)
    private Date updtDt;
}
