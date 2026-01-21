package com.castis.publishservice.mapper;

import com.castis.publishservice.dto.queue.SupplierRequest;
import com.castis.publishservice.entity.Supplier;
import com.castis.publishservice.utils.Utils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(imports = {Utils.class})
public interface SupplierMapper {
    public static final SupplierMapper INSTANCE = Mappers.getMapper(SupplierMapper.class);
    @Mapping(target = "name", source = "supplierName")
    SupplierRequest toRequest(Supplier entity);
}
