package com.evoucher.evoucherbe.mapper;

import com.evoucher.evoucherbe.dto.SupplierContractDto;
import com.evoucher.evoucherbe.entity.SupplierContract;
import org.mapstruct.Mapper;

@Mapper
public interface SupplierContractMapper {
    SupplierContractDto entityToDto(SupplierContract entity);
    SupplierContract dtoToEntity(SupplierContractDto dto);

}
