package com.evoucher.adminapi.partner.service.model.request;

import com.evoucher.adminapi.common.utils.Constant;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ExternalPublishConvert {
    private String transactionId;
    private String sha;
    @JsonFormat(pattern = Constant.Common.COMMON_DATETIME_FORMAT)
    private Date requestTime;
    private String userId;
    private Boolean isSendSms;
    @JsonFormat(pattern = Constant.Common.COMMON_DATETIME_FORMAT)
    private Date smsSchedule;
    private String subject;
    private String contentText;
    private String contentLink;
    private List<OrderPinRequest> orders;
}
