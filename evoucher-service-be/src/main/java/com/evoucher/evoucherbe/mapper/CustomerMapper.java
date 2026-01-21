package com.evoucher.evoucherbe.mapper;

import com.evoucher.evoucherbe.dto.CustomerDto;
import com.evoucher.evoucherbe.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CustomerMapper {
    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);
    CustomerDto toDto(Customer entity);
    Customer toEntity(CustomerDto dto);
}
