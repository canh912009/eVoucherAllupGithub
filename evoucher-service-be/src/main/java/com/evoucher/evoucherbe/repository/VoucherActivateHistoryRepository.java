package com.evoucher.evoucherbe.repository;

import com.evoucher.evoucherbe.entity.VoucherActivateHistory;
import com.evoucher.evoucherbe.entity.VoucherTransferHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoucherActivateHistoryRepository extends JpaRepository<VoucherActivateHistory, Long> {
}
