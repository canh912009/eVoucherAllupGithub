package com.evoucher.evoucherbe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublishDetailDto {
    private Integer id;
    private Integer publishId;
    private String receiverMobileNo;
    private Long userId;
    private String publishStatusCd;
    private String publishResultMessage;
    private String smsId;
    private String smsType;
    private Date smsSendDt;
    private Date smsSendResultDate;
    private Long externalPinId;
    private Date regDt;
    private Date updateDate;
}
