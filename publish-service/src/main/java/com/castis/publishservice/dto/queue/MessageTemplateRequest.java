package com.castis.publishservice.dto.queue;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageTemplateRequest {
    private String template;
    private String language;
    private List<MsgTemplateData> data;
}
