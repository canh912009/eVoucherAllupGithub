package com.evoucher.adminapi.cms.service.models;

import com.evoucher.adminapi.common.enums.ExternalPinStatus;
import com.evoucher.adminapi.common.models.BaseDTO;
import com.evoucher.adminapi.common.utils.Constant;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ExternalPinDTO extends BaseDTO {

    private Integer id;

    private String externalPinNo;

    private Integer goodsId;

    private ExternalPinUploadDTO externalPinUpload;

    private ExternalPinStatus status;

    @JsonFormat(pattern = Constant.Common.COMMON_DATE_FORMAT)
    private Date expireTime;

    private String password;
}
