package com.evoucher.adminapi.admin.service.models;

import com.evoucher.adminapi.admin.enums.OperatorRequestStatus;
import com.evoucher.adminapi.admin.validator_group.ApprovingGroup;
import com.evoucher.adminapi.admin.validator_group.CreatingGroup;
import com.evoucher.adminapi.auth.service.models.AdminDTO;
import com.evoucher.adminapi.cms.service.models.CustomerDTO;
import com.evoucher.adminapi.common.utils.Constant;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class OperatorRequestDto {
    @NotNull(groups = {ApprovingGroup.class})
    Long reqId;
    @NotBlank(groups = {CreatingGroup.class})
    String ev;
    Long publishId;
    Long goodsId;
    @NotNull(groups = {ApprovingGroup.class})
    OperatorRequestStatus reqStatus;
    String requester;
    @JsonFormat(pattern = Constant.Common.COMMON_DATETIME_FORMAT)
    Date reqDt;
    @NotBlank(groups = {CreatingGroup.class})
    String memo;
    @JsonFormat(pattern = Constant.Common.COMMON_DATETIME_FORMAT)
    Date approveDate;
    @NotNull(groups = {ApprovingGroup.class})
    String approveMemo;
    String approver;

    // for detail
    AdminDTO requestedAdmin;
    AdminDTO approvedAdmin;
}
