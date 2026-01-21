package com.evoucher.adminapi.partner.service.model.response;

import com.evoucher.adminapi.common.utils.Constant;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderPinResponse {
    private Integer orderId;
    private String pin;
    private String shortLink;
    @JsonFormat(pattern = Constant.Common.COMMON_DATETIME_FORMAT)
    private Date createDate;
    @JsonFormat(pattern = Constant.Common.COMMON_DATETIME_FORMAT)
    private Date publishDate;
    private String status;
    private Integer goodsId;
    private String goodsName;
    private String brandId;
    private String brandName;
    private String goodsImgPath;
}
