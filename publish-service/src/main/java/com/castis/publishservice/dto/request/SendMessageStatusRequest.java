package com.castis.publishservice.dto.request;

import com.castis.publishservice.dto.queue.VoucherResendProcessResponse;
import com.castis.publishservice.utils.status.PublishDetailStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class SendMessageStatusRequest {
    private String messageId;
    private long publishDetailId;
    private String smsType;
    private String phoneNumber;
    private PublishDetailStatus result;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date receivedTime;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date sendDatetime;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date sendResultDatetime;
    private VoucherResendProcessResponse resend;

    private String email;
    private String message;
}
