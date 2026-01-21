package com.evoucher.adminapi.admin.service.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublishServiceResponseException {
    private int code;
    private String message;
}
