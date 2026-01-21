package com.evoucher.adminapi.settlement.dao;

import com.evoucher.adminapi.settlement.dao.model.SettlementLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettlementLogRepository extends JpaRepository<SettlementLog, Integer>, SettlementLogRepositoryCustom {
}
