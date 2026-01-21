package com.evoucher.evoucherbe.service.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PublishDetailRequest {

    @NotNull(message = "PublishDetail ID is empty!")
    private Integer publishDetailId;

    @NotBlank(message = "Mobile phone is empty!")
    private String mobileNumber;
}
