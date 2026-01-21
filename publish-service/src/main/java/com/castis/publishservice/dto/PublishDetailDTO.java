package com.castis.publishservice.dto;

import com.castis.publishservice.utils.Constants;
import com.castis.publishservice.utils.status.PublishDetailStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublishDetailDTO {
    private Long publishDtlId;
    @JsonFormat(pattern = Constants.fullDateTimeFormat)
    private Date regDt;
    private long publishId;
    private String receiverMobileNo;
    private Long userId;
    private PublishDetailStatus publishStatusCd;
    private String publishResultMessage;
    @JsonFormat(pattern = Constants.fullDateTimeFormat)
    private Date updateDate;
    private String smsId;
    private String smsType;
    @JsonFormat(pattern = Constants.fullDateTimeFormat)
    private Date smsSendDt;
    @JsonFormat(pattern = Constants.fullDateTimeFormat)
    private Date smsSendResultDate;
    private Long extPinId;
    private EndUserDto user;
}
