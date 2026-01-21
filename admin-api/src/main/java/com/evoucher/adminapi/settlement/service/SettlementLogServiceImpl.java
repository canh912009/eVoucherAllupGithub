package com.evoucher.adminapi.settlement.service;

import com.evoucher.adminapi.settlement.dao.SettlementLogRepository;
import com.evoucher.adminapi.settlement.service.model.FilterSearchSettlement;
import com.evoucher.adminapi.settlement.service.model.SettlementLogAllDataDto;
import com.evoucher.adminapi.settlement.service.model.SettlementLogDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SettlementLogServiceImpl implements SettlementLogService {

    private final SettlementLogRepository settlementLogRepository;


    @Override
    public Page<SettlementLogDTO> searchSettlementLog(FilterSearchSettlement filterSearchSettlement) {
        int page = ObjectUtils.isEmpty(filterSearchSettlement.getPage()) ? 0 : filterSearchSettlement.getPage() - 1; //pageable count from 0
        int pageSize = ObjectUtils.isEmpty(filterSearchSettlement.getPageSize()) ? 10 : filterSearchSettlement.getPageSize();
        Pageable pageable = PageRequest.of(page, pageSize);

        List<SettlementLogDTO> settlementLogDTOS = settlementLogRepository.searchSettlementLog(filterSearchSettlement, pageable);
        long settlementNumber = 0;
        if (!CollectionUtils.isEmpty(settlementLogDTOS)) {
            settlementNumber = settlementLogRepository.countSettlementLog(filterSearchSettlement);
        }

        return new PageImpl<>(settlementLogDTOS, pageable, settlementNumber);
    }

    @Override
    public List<SettlementLogAllDataDto> getDataSettlementLog(FilterSearchSettlement filterSearchSettlement) {
        log.info("Get settlement log data");
        return settlementLogRepository.getSettlementLogData(filterSearchSettlement);
    }
}
