package com.evoucher.adminapi.admin.service.models;

import com.evoucher.adminapi.common.enums.ApproveStatus;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ApproveRequest {
    @NotNull(message = "Approve status code is empty!")
    private ApproveStatus approveStatusCode;
    @Size(max = 1000, message = "Reject reason less than 1000 characters")
    private String rejectReason;
}
