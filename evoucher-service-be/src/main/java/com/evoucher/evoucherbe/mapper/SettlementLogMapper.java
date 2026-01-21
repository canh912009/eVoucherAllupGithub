package com.evoucher.evoucherbe.mapper;

import com.evoucher.evoucherbe.dto.SettlementLogDto;
import com.evoucher.evoucherbe.entity.SettlementLog;
import org.mapstruct.Mapper;

@Mapper
public interface SettlementLogMapper {
    SettlementLog toEntity(SettlementLogDto dto);
    SettlementLogDto toDto(SettlementLog entity);
}
