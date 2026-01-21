package com.evoucher.adminapi.admin.mapper;

import com.evoucher.adminapi.admin.dao.models.SupplierContract;
import com.evoucher.adminapi.admin.service.models.SupplierContractDTO;
import com.evoucher.adminapi.admin.service.models.SupplierContractRequest;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(builder = @Builder(disableBuilder = true))
public interface SupplierContractMapper {

    SupplierContractMapper INSTANT = Mappers.getMapper(SupplierContractMapper.class);

    SupplierContractDTO toContractDTO(SupplierContract supplierContract);

    SupplierContract toContract(SupplierContractDTO supplierContractDTO);

    SupplierContract toContract(SupplierContractRequest supplierContractRequest);
}
