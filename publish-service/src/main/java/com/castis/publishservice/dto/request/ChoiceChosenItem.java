package com.castis.publishservice.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChoiceChosenItem {
    Long goodsId;
    Integer quantity;
}

