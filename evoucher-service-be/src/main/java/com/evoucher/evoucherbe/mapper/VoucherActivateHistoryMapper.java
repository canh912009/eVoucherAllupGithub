package com.evoucher.evoucherbe.mapper;

import com.evoucher.evoucherbe.dto.VoucherActivateHistoryDto;
import com.evoucher.evoucherbe.entity.VoucherActivateHistory;
import org.mapstruct.Mapper;

@Mapper
public interface VoucherActivateHistoryMapper {
    VoucherActivateHistory mapDtoToEntity(VoucherActivateHistoryDto dto);
    VoucherActivateHistoryDto mapEntityToDto(VoucherActivateHistory entity);
}
