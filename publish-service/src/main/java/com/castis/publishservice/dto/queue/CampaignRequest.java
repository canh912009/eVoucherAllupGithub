package com.castis.publishservice.dto.queue;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CampaignRequest {
    private Long id;
    private String name;
    private String startDate;
    private String endDate;
    private MessageTemplateRequest messageTemplate;
}