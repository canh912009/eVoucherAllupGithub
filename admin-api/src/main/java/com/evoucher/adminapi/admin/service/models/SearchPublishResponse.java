package com.evoucher.adminapi.admin.service.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchPublishResponse {
    private Integer id;
    private String publishName;
    private Integer campaignId;
    private String campaignName;
    private String bookingYn;
    private String goodsName;
    private String statusCode;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date approveDate;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date regDt;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updtDt;
    private String smsType;
}
