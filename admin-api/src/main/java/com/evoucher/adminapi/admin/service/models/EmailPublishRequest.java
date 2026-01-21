package com.evoucher.adminapi.admin.service.models;

import com.evoucher.adminapi.cms.service.models.request.EndUserRequest;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class EmailPublishRequest {
    Long publishId;
    List<EndUserRequest> users;
}
