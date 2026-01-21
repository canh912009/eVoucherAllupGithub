package com.evoucher.evoucherbe.service.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class VoucherHandoverRequest {

    @NotBlank(message = "Voucher ID old is empty!")
    private String evOld;
    @NotNull(message = "PublishDetail ID is empty!")
    private Integer publishDetailId;
    @NotNull(message = "EndUser is empty!")
    private EndUserRequest endUser;
    private String transferMessage;
}
