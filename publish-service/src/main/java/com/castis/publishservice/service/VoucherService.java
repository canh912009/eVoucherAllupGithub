package com.castis.publishservice.service;

import com.castis.publishservice.dto.queue.VoucherRequest;
import com.castis.publishservice.dto.request.VoucherResendRequest;
import com.castis.publishservice.dto.request.StatusQueue;
import com.castis.publishservice.exception.defineException.ServerRuntimeException;

import java.util.HashMap;
import java.util.List;

public interface VoucherService {
    List<VoucherRequest> getVoucherRequestByVoucherId(List<String> requestIds);
    void updatePublishStatus(StatusQueue statusQueue);
    void updateSendMessageStatus(List<StatusQueue> statusQueues);
    Long getContractIdByPublishId(Long publishId) throws ServerRuntimeException;
    void resendVoucher(VoucherResendRequest voucherResendRequest);
}
