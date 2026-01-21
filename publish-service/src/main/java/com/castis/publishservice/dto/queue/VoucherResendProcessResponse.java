package com.castis.publishservice.dto.queue;

import com.castis.publishservice.utils.status.CompletedStatusCode;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class VoucherResendProcessResponse {
    private Integer voucherResendHistoryId;
    private String frontEndPreviousPublishDetailStatusCode;
    private CompletedStatusCode frontEndUpdateResult;
}
