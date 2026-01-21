package com.evoucher.adminapi.auth.service.models;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
public class AdminLoginRequest {

    @NotEmpty(message = "Phone number is empty!")
    private String phoneNumber;

    @NotEmpty(message = "Password is empty!")
    private String password;
}
