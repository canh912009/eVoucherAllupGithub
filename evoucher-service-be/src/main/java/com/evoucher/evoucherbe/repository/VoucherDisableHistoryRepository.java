package com.evoucher.evoucherbe.repository;

import com.evoucher.evoucherbe.entity.VoucherDisableHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface VoucherDisableHistoryRepository extends JpaRepository<VoucherDisableHistory, Integer> {
}
