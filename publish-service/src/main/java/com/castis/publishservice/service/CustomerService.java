package com.castis.publishservice.service;

import com.castis.publishservice.dto.queue.CustomerRequest;
import com.castis.publishservice.entity.Customer;
import com.castis.publishservice.exception.defineException.NotFoundException;
import com.castis.publishservice.exception.defineException.ServerRuntimeException;
import com.castis.publishservice.mapper.CustomerMapper;
import com.castis.publishservice.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository repository;
    private static final CustomerMapper mapper = CustomerMapper.INSTANCE;

    public CustomerRequest findRequestById(String customerId) {
        if (Objects.isNull(customerId) || customerId.isBlank()) {
            throw new ServerRuntimeException("Null or empty customer id");
        }
        Customer customer = repository.findById(customerId)
                .orElseThrow(() -> NotFoundException.customer(customerId));
        return mapper.toRequest(customer);
    }
}
