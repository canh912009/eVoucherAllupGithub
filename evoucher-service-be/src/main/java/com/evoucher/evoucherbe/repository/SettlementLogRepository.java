package com.evoucher.evoucherbe.repository;

import com.evoucher.evoucherbe.common.enums.SettlementLogType;
import com.evoucher.evoucherbe.common.enums.SettlementMethodCode;
import com.evoucher.evoucherbe.entity.SettlementLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SettlementLogRepository extends JpaRepository<SettlementLog, Integer> {
    void deleteAllByEvIn(List<String> evs);
    List<SettlementLog> getSettlementLogByEv(String ev);
    List<SettlementLog> getSettlementLogByEvAndSettlementMethodCode(String ev, SettlementMethodCode methodCode);
}
