package com.evoucher.evoucherbe.mapper;

import com.evoucher.evoucherbe.dto.EVoucherHistoryProcess;
import com.evoucher.evoucherbe.dto.VoucherExchangeReq;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface RequestMapper {

    @Mapping(target = "transactionDate", ignore = true)
    @Mapping(target = "voucherId", source = "ev")
    VoucherExchangeReq toExchangeRequest(EVoucherHistoryProcess process);
}
