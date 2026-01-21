package com.castis.publishservice.dto;

import com.castis.publishservice.dto.request.ChoiceChosenItem;
import com.castis.publishservice.utils.enum_template.SystemType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PurchaseChildRequest {
    String parentVoucherId;
    List<ChoiceChosenItem> products;
    SystemType type;
}
