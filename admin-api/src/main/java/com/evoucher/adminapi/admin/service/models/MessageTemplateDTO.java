package com.evoucher.adminapi.admin.service.models;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageTemplateDTO {
    int id;
    String name;
    String messageString;
    String system;
}
