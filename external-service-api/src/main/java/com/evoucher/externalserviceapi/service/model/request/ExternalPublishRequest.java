package com.evoucher.externalserviceapi.service.model.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ExternalPublishRequest {
    private UUID transactionId;
    private String sha;
    private String requestTime;
    private String userId;
    private Boolean isSendSms;
    private String smsSchedule;
    private String subject;
    private String contentText;
    private MultipartFile contentImage;
    private String contentLink;
    private List<OrderPinRequest> orders;
}
