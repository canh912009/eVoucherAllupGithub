package com.evoucher.evoucherbe.repository;

import com.evoucher.evoucherbe.entity.VoucherChoiceHistory;
import com.evoucher.evoucherbe.entity.VoucherExchangeHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoucherChoiceHistoryRepository extends JpaRepository<VoucherChoiceHistory, String> {
}
