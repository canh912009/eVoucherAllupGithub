package com.evoucher.evoucherbe.service;

import com.evoucher.evoucherbe.common.enums.EnumValidYn;
import com.evoucher.evoucherbe.dto.CustomerDto;
import com.evoucher.evoucherbe.entity.Customer;
import com.evoucher.evoucherbe.exception.CustomCodeException;
import com.evoucher.evoucherbe.exception.EntityNotFoundException;
import com.evoucher.evoucherbe.mapper.CustomerMapper;
import com.evoucher.evoucherbe.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerBasicService extends EntityService<Customer, String, CustomerDto> {
    private final CustomerRepository repository;
    private static final CustomerMapper MAPPER = CustomerMapper.INSTANCE;
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
        return new EntityNotFoundException(
                "can not find customer with id: ".concat(id)
        );
    }

    @Override
    public Customer toEntity(CustomerDto dto) {
        return MAPPER.toEntity(dto);
    }

    @Override
    public CustomerDto toDto(Customer entity) {
        return MAPPER.toDto(entity);
    }

    public CustomerDto getValidCustomerById(String id) throws CustomCodeException {
        try {
            Customer customer = findById(id);
            if (!Objects.equals(customer.getValidYn(), EnumValidYn.Y.name())) {
                log.info("Customer {} is invalid", customer.getId());
                throw new CustomCodeException(
                        "Customer is invalid",
                        HttpStatus.BAD_REQUEST
                );
            }
            return toDto(customer);
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage(), e);
            throw new CustomCodeException(
                    e.getMessage(),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}
