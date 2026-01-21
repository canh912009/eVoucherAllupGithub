package com.castis.publishservice.dto.request;

import com.castis.publishservice.utils.enum_template.SystemType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateChoiceItemRequest {
    String parentId;
    Long publishId;
    List<ChoiceChosenItem> products;
    String token;
    SystemType type;
}
