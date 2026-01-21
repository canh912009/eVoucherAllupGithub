package com.evoucher.evoucherbe.repository;


import com.evoucher.evoucherbe.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String>{
    Optional<Customer> findByIdAndValidYn(String id, String validYn);
}
