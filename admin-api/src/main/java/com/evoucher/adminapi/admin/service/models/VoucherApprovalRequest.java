package com.evoucher.adminapi.admin.service.models;

import com.evoucher.adminapi.auth.service.models.AdminDTO;
import com.evoucher.adminapi.cms.service.models.CustomerDTO;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VoucherApprovalRequest {
    OperatorRequestDto request;
    VoucherDto voucher;
    PublishDTO publish;
    CustomerDTO customer;
    AdminDTO requester;

    List<OperatorRequestDto> approvalHistory;
}
