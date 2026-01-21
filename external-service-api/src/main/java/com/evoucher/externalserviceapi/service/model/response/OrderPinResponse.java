package com.evoucher.externalserviceapi.service.model.response;

import com.evoucher.externalserviceapi.common.utils.ConstantUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
public class OrderPinResponse {
    private Integer orderId;
    private String pin;
    private String shortLink;
    @JsonFormat(pattern = ConstantUtils.Common.COMMON_DATETIME_FORMAT)
    private Date createDate;
    @JsonFormat(pattern = ConstantUtils.Common.COMMON_DATETIME_FORMAT)
    private Date publishDate;
    private String status;
    private String goodsId;
    private String goodsName;
    private String brandId;
    private String brandName;
    private String goodsImgPath;
}
