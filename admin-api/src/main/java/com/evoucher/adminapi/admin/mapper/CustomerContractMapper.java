package com.evoucher.adminapi.admin.mapper;

import com.evoucher.adminapi.admin.dao.models.CustomerContract;
import com.evoucher.adminapi.admin.service.models.CustomerContractDTO;
import com.evoucher.adminapi.admin.service.models.CustomerContractRequest;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(builder = @Builder(disableBuilder = true))
public interface CustomerContractMapper {

    CustomerContractMapper INSTANT = Mappers.getMapper(CustomerContractMapper.class);

    CustomerContractDTO toContractDTO(CustomerContract customerContract);

    CustomerContract toContract(CustomerContractDTO customerContractDTO);

    CustomerContract toContract(CustomerContractRequest customerContractRequest);
}
