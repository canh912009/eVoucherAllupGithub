package com.evoucher.evoucherbe.service;

import com.evoucher.evoucherbe.entity.VoucherChoiceHistory;
import com.evoucher.evoucherbe.repository.VoucherChoiceHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class VoucherChoiceHistoryService {

    private final VoucherChoiceHistoryRepository repository;

    public void saveVoucherChoiceHistory(VoucherChoiceHistory voucherChoiceHistory) {
        try {
            repository.save(voucherChoiceHistory);
        } catch (Exception e) {
            log.error("Save VoucherChoiceHistory error: {}", e.getMessage(), e);
        }
    }
}
