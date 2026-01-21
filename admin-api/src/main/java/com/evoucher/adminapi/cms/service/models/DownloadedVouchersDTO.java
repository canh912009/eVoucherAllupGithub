package com.evoucher.adminapi.cms.service.models;

import com.evoucher.adminapi.common.models.BaseDTO;
import com.evoucher.adminapi.common.utils.Constant;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DownloadedVouchersDTO {
    private String ev;
    private Double price;
    private String extPin;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expirationDate;
    private String OTP;
    private String shortLink;
    private String serialNo;
    private String activationUrl;
    private String activationId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date activationDate;
}
