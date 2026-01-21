package com.evoucher.adminapi.cms.service.models.request;

import com.evoucher.adminapi.common.utils.Constant;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ExternalPinRequest {

    @NotBlank(message = "External PIN NO is empty!")
    @Size(max = 100, message = "External PIN NO less than 100 characters!")
    private String externalPinNo;

    @NotNull(message = "External PIN expire time is empty!")
    @JsonFormat(pattern = Constant.Common.COMMON_DATE_FORMAT)
    private Date expireTime;

    private String password;
}
