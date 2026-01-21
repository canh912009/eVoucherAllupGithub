package com.evoucher.adminapi.cms.service.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class EndUserDTO {
    private String userMobileNum;
    private String userNm;
    private String gender;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date birthday;
    private String address;
    private String email;
    private String smsStatus;
    private String externalPinNo;
    private String publishResultMessage;
    private String ev;
}
