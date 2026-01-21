package com.castis.publishservice.mapper;

import com.castis.publishservice.dto.queue.CustomerRequest;
import com.castis.publishservice.entity.Customer;
import com.castis.publishservice.utils.Utils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(imports = {Utils.class})
public interface CustomerMapper {
    public static final CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);
    @Mapping(target = "name", source = "customerName")
    CustomerRequest toRequest(Customer entity);
}
