package com.evoucher.evoucherbe.mapper;

import com.evoucher.evoucherbe.dto.CustomerContractDto;
import com.evoucher.evoucherbe.entity.CustomerContract;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerContractMapper {
    CustomerContractDto entityToDto(CustomerContract entity);
    CustomerContract dtoToEntity(CustomerContractDto dto);
}
