package com.castis.publishservice.dto.request;

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
    @NotBlank(message = "Disable reason is empty!")
    private String reason;
}
