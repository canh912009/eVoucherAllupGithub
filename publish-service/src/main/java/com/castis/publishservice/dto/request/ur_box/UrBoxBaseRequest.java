package com.castis.publishservice.dto.request.ur_box;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PUBLIC)
@Accessors(chain = true)
public class UrBoxBaseRequest {
    String app_secret;
    Integer app_id;
    Integer page_no = 1;
    Integer per_page = 500;
}
