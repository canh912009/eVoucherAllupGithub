package com.castis.publishservice.dto.queue;

import com.castis.publishservice.utils.enum_template.EnumTemplateKey;
import com.castis.publishservice.utils.enum_template.EnumTemplateType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MsgTemplateData {
    private String key;
    private String type;
    private String defaultValue;
    private int maxLength;
    private boolean encrypted;
    private String value;
}
