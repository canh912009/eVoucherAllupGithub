package com.evoucher.adminapi.auth.service.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Data
@NoArgsConstructor
public class AdminRequest {
    @Email(message = "Email is valid!", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    @Size(max = 50, min = 6)
    private String email;
    @NotBlank(message = "Password is empty!")
    @Size(max = 120, min = 6)
    private String password;
    @NotBlank(message = "Admin name is empty!")
    private String adminName;
    @NotBlank(message = "Mobile phone number is empty!")
    private String mobileNumber;
    private String telephone;
    private String adminCorporationId;
    @NotBlank(message = "Role admin is empty!")
    private String roleCode;
}