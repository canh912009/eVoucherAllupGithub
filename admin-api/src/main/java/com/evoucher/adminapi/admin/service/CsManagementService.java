package com.evoucher.adminapi.admin.service;

import com.evoucher.adminapi.admin.service.models.CsManagementFilterRequest;
import com.evoucher.adminapi.admin.service.models.CsManagementResponse;
import com.evoucher.adminapi.admin.service.models.PinDetailResponse;
import com.evoucher.adminapi.admin.service.models.VoucherDisableRequest;
import com.evoucher.adminapi.admin.service.models.VoucherResendRequest;
import com.evoucher.adminapi.common.exception.CustomCodeException;
import org.springframework.data.domain.Page;

public interface CsManagementService {
    Page<CsManagementResponse> search(CsManagementFilterRequest request, Integer page, Integer pageSize) throws CustomCodeException;
    PinDetailResponse getPinDetail(String ev) throws CustomCodeException;
    PinDetailResponse getPinDetailV2(String ev) throws CustomCodeException;
    void disableVoucher(VoucherDisableRequest voucherDisableRequest);
    void resendVoucher(VoucherResendRequest voucherResendRequest);
}
