package com.evoucher.evoucherbe.service;

import com.evoucher.evoucherbe.common.enums.EnumValidYn;
import com.evoucher.evoucherbe.dto.CustomerContractDto;
import com.evoucher.evoucherbe.entity.CustomerContract;
import com.evoucher.evoucherbe.entity.SupplierContract;
import com.evoucher.evoucherbe.exception.EntityNotFoundException;
import com.evoucher.evoucherbe.mapper.CustomerContractMapper;
import com.evoucher.evoucherbe.repository.CustomerContractRepository;
import com.evoucher.evoucherbe.utils.MessageUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerContractService extends EntityService<CustomerContract, Integer, CustomerContractDto> {
    @Getter
    private final CustomerContractRepository repository;

    private final CustomerContractMapper mapper;

    @Override
    public String getEntityType() {
        return "customer contract";
    }

    @Override
    public EntityNotFoundException getNotFoundException(Integer id) {
        return new EntityNotFoundException(MessageUtils.getMessage("evoucher.customerContract.not.found"));
    }

    @Override
    public CustomerContract toEntity(CustomerContractDto dto) {
        return mapper.dtoToEntity(dto);
    }

    @Override
    public CustomerContractDto toDto(CustomerContract entity) {
        return mapper.entityToDto(entity);
    }

    public CustomerContractDto findValidById(Integer id) throws EntityNotFoundException {
        CustomerContract contract = repository.findByIdAndValidYn(id, EnumValidYn.Y)
                .orElseThrow(() -> getNotFoundException(id));
        return toDto(contract);
    }
}
