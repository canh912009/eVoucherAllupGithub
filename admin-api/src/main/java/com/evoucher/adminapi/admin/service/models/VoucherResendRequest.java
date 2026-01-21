package com.evoucher.adminapi.admin.service.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VoucherResendRequest {
    @NotBlank(message = "EVoucher ID is empty!")
    private String ev;
    private String reason;
}
