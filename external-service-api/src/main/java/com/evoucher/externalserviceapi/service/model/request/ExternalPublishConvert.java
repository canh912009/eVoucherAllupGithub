package com.evoucher.externalserviceapi.service.model.request;

import com.evoucher.externalserviceapi.common.utils.ConstantUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ExternalPublishConvert {
    private UUID transactionId;
    private String sha;
    @JsonFormat(pattern = ConstantUtils.Common.COMMON_DATETIME_FORMAT)
    private Date requestTime;
    private String userId;
    private boolean isSendSms;
    @JsonFormat(pattern = ConstantUtils.Common.COMMON_DATETIME_FORMAT)
    private Date smsSchedule;
    private String subject;
    private String contentText;
    private String contentLink;
    private List<OrderPinRequest> orders;
}
