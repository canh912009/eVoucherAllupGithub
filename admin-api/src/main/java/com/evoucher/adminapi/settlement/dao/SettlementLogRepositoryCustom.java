package com.evoucher.adminapi.settlement.dao;

import com.evoucher.adminapi.settlement.service.model.FilterSearchSettlement;
import com.evoucher.adminapi.settlement.service.model.SettlementLogAllDataDto;
import com.evoucher.adminapi.settlement.service.model.SettlementLogDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SettlementLogRepositoryCustom {
    List<SettlementLogDTO> searchSettlementLog(FilterSearchSettlement filterSearchSettlement, Pageable pageable);
    long countSettlementLog(FilterSearchSettlement filterSearchSettlement);
    List<SettlementLogAllDataDto> getSettlementLogData(FilterSearchSettlement filterSearchSettlement);
}
