package com.evoucher.adminapi.cms.service;

import com.evoucher.adminapi.cms.dao.models.Customer;
import com.evoucher.adminapi.cms.mapper.CustomerMapper;
import com.evoucher.adminapi.cms.service.models.CustomerDTO;
import com.evoucher.adminapi.common.exception.EntityNotFoundException;
import com.evoucher.adminapi.common.service.EntityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import static com.evoucher.adminapi.cms.service.CustomerServiceImpl.NOT_FOUND_ERROR_CODE;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerBasicService extends EntityService<Customer, String, CustomerDTO> {
    private final JpaRepository<Customer, String> repository;
    private final CustomerMapper mapper;
    @Override
    public JpaRepository<Customer, String> getRepository() {
        return repository;
    }

    @Override
    public String getEntityType() {
        return "customer";
    }

    @Override
    public EntityNotFoundException getNotFoundException(String id) {
        return new EntityNotFoundException(NOT_FOUND_ERROR_CODE);
    }

    @Override
    public Customer toEntity(CustomerDTO dto) {
        return mapper.toEntity(dto);
    }

    @Override
    public CustomerDTO toDto(Customer entity) {
        return mapper.toDTO(entity);
    }

    public CustomerDTO findCustomerRequestById(String id) throws EntityNotFoundException {
        return mapper.toOperatorReqInfo(findById(id));
    }
}
