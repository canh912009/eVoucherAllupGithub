package com.castis.publishservice.dto.request;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class EvoucherRequest {
    private Long contractId;
    private Long publishId;
    private Long campaignId;
    private Long goodsId;
    private List<PublishDetailRequest> publishDetails;
}
