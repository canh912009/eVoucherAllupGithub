package com.evoucher.adminapi.cms.mapper;


import com.evoucher.adminapi.cms.dao.models.Supplier;
import com.evoucher.adminapi.cms.service.models.SupplierDTO;
import com.evoucher.adminapi.cms.service.models.request.SupplierRequest;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(builder = @Builder(disableBuilder = true))
public interface SupplierMapper {

    SupplierMapper INSTANT = Mappers.getMapper(SupplierMapper.class);

    Supplier toSupplier(SupplierDTO supplierDTO);

    Supplier toSupplier(SupplierRequest supplierRequest);

    SupplierDTO toSupplierDTO(Supplier supplier);

    List<SupplierDTO> toListSupplierDTOS(List<Supplier> suppliers);
}
