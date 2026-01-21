package com.castis.publishservice.dto.queue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PublishDetailQueueRequest {
    private Long id;
    private Long publishId;
    private String smsId;
    private String smsType;
    private VoucherRequest voucher;
}
