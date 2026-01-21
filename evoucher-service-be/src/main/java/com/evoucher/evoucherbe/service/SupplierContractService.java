package com.evoucher.evoucherbe.service;

import com.evoucher.evoucherbe.common.enums.EnumValidYn;
import com.evoucher.evoucherbe.dto.SupplierContractDto;
import com.evoucher.evoucherbe.entity.SupplierContract;
import com.evoucher.evoucherbe.exception.CustomCodeException;
import com.evoucher.evoucherbe.exception.EntityNotFoundException;
import com.evoucher.evoucherbe.mapper.SupplierContractMapper;
import com.evoucher.evoucherbe.repository.SupplierContractRepository;
import com.evoucher.evoucherbe.utils.MessageUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SupplierContractService extends EntityService<SupplierContract, Integer, SupplierContractDto> {
    @Getter
    private final SupplierContractRepository repository;
    private final SupplierContractMapper mapper;


    @Override
    public String getEntityType() {
        return "supplier contract";
    }

    @Override
    public EntityNotFoundException getNotFoundException(Integer id) {
        return  new EntityNotFoundException(
                MessageUtils.getMessage("evoucher.supplierContract.not.found"));
    }

    @Override
    public SupplierContract toEntity(SupplierContractDto dto) {
        return mapper.dtoToEntity(dto);
    }

    @Override
    public SupplierContractDto toDto(SupplierContract entity) {
        return mapper.entityToDto(entity);
    }

    public SupplierContractDto findValidById(Integer id) throws EntityNotFoundException {
        SupplierContract contract = repository.findByIdAndValidYn(id, EnumValidYn.Y)
                .orElseThrow(() -> getNotFoundException(id));
        return toDto(contract);
    }
}
