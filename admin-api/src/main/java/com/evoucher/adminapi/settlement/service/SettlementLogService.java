package com.evoucher.adminapi.settlement.service;

import com.evoucher.adminapi.settlement.service.model.FilterSearchSettlement;
import com.evoucher.adminapi.settlement.service.model.SettlementLogAllDataDto;
import com.evoucher.adminapi.settlement.service.model.SettlementLogDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SettlementLogService {
    Page<SettlementLogDTO> searchSettlementLog(FilterSearchSettlement filterSearchSettlement);
    List<SettlementLogAllDataDto> getDataSettlementLog(FilterSearchSettlement filterSearchSettlement);
}
