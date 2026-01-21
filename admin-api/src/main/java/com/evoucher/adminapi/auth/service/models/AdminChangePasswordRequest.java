package com.evoucher.adminapi.auth.service.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AdminChangePasswordRequest {
    @NotBlank(message = "Password new is empty!")
    @Size(max = 120, min = 6)
    private String newPassword;
}
