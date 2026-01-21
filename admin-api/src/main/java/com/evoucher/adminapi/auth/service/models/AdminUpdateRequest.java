package com.evoucher.adminapi.auth.service.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;


@Data
@NoArgsConstructor
public class AdminUpdateRequest {
    @Email(message = "Email is valid!", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    @Size(max = 50, min = 6)
    private String email;
    @NotBlank(message = "Admin name is empty!")
    private String adminName;
    @NotBlank(message = "Mobile phone number is empty!")
    private String mobileNumber;
    private String telephone;
    private String adminCorporationId;
    @NotBlank(message = "Role admin is empty!")
    private String roleCode;
}