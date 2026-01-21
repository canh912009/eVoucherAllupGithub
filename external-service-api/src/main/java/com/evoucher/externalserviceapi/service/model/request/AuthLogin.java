package com.evoucher.externalserviceapi.service.model.request;

import com.evoucher.externalserviceapi.common.utils.ConstantUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Getter
@NoArgsConstructor
@ToString
public class AuthLogin {
    @NotEmpty(message = "App id is empty!")
    @JsonProperty("app_id")
    private String phoneNumber;

    @NotEmpty(message = "App secret is empty!")
    @JsonProperty("app_secret")
    private String password;

    @JsonProperty("request_time")
    @JsonFormat(pattern= ConstantUtils.Common.COMMON_DATETIME_FORMAT)
    private Date requestTime;
}
