package com.castis.pos_api.service.common;

import com.castis.pos_api.entity.Voucher;
import com.castis.pos_api.enum_constant.VoucherProcessingStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class LockingService {
    private final RLock posProcessingLock;
    private final RMap<String, VoucherProcessingStatus> processingStatusMap;


    public VoucherProcessingStatus getCurrentStatusByVoucherId(String voucherId) {
        try {
            //lock
            posProcessingLock.lock();

            log.info("get pos processing status by voucher id: {}", voucherId);
            return processingStatusMap.get(voucherId);
        } finally {
            posProcessingLock.unlock();
        }
    }



}
