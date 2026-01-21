package com.castis.pos_api.service;

import com.castis.pos_api.enum_constant.VoucherProcessingStatus;
import com.castis.pos_api.exception.ApplicationException;
import com.castis.pos_api.service.common.LockingService;
import com.castis.pos_api.utils.CustomResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class SyncProcessingService {
    private static final HashMap<VoucherProcessingStatus, VoucherProcessingStatus> ALLOWED_PRE_STATUS = new HashMap<>();

    static {
        // allow all process status to be validated, null also get allowed
        ALLOWED_PRE_STATUS.put(VoucherProcessingStatus.VALIDATING, null);

        // only validated voucher could be finalized
        ALLOWED_PRE_STATUS.put(VoucherProcessingStatus.FINALIZING, VoucherProcessingStatus.VALIDATING);
        // only finalized voucher could be canceled
        ALLOWED_PRE_STATUS.put(VoucherProcessingStatus.CANCELING, VoucherProcessingStatus.FINALIZING);
    }

    private final LockingService lockingService;

    public void validateUsingVoucherStatus(String voucherId, VoucherProcessingStatus processingStep) {
        VoucherProcessingStatus currentStatus = lockingService.getCurrentStatusByVoucherId(voucherId);

        VoucherProcessingStatus allowedPreStatus = ALLOWED_PRE_STATUS.get(processingStep);

        if (allowedPreStatus != currentStatus) {
            if (currentStatus != null) {
                throw new ApplicationException(CustomResponse.E6001_VOUCHER_IS_PROCESSING.getCode(), String.format("current status %s is different with %s, not allowed to get next step %s", currentStatus, allowedPreStatus, processingStep));
            } else {
                throw new ApplicationException(CustomResponse.E4401_INVALID_VOUCHER_STATE);
            }
        }
    }
}
