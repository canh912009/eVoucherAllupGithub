package com.evoucher.adminapi.good.service.model.ur_box;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Accessors(chain = true)
public class UrBoxGoodListRequest extends UrBoxBaseRequest {
    Integer brand_id;
    Integer cat_id;
    String field = "content,note,office";
    String lang = "vi";
    Integer stock = 1;
    String title;
}
