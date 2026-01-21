package com.castis.publishservice.repository;

import com.castis.publishservice.entity.VoucherResendHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoucherResendHistoryRepository extends JpaRepository<VoucherResendHistory, Integer> {
}
