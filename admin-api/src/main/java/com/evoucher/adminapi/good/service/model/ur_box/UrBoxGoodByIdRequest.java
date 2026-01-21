package com.evoucher.adminapi.good.service.model.ur_box;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@ToString(callSuper = true)
@Accessors(chain = true)
public class UrBoxGoodByIdRequest extends UrBoxBaseRequest{
    String id;
}
