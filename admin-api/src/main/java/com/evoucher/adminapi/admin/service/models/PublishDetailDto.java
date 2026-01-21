package com.evoucher.adminapi.admin.service.models;

import lombok.Data;

@Data
public class PublishDetailDto {
    Integer id;
    Integer publishId;
    String receiverMobileNo;
    Long userId;
    String publishDetailStatusCode;
    Integer externalPinId;
    String publishResultMessage;
}
