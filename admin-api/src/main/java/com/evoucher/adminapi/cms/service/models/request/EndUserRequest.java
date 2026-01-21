package com.evoucher.adminapi.cms.service.models.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EndUserRequest {

    @NotBlank(message = "End user mobile number is empty!")
    private String userMobileNum;
    @NotBlank(message = "End user name is empty!")
    private String userNm;
    private String gender;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date birthday;
    private String address;
    private String email;

}
