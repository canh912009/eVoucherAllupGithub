package com.evoucher.adminapi.admin.dao;

import com.evoucher.adminapi.admin.dao.models.CustomerContract;
import com.evoucher.adminapi.common.enums.EnumValidYn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerContractRepository extends JpaRepository<CustomerContract, Integer>, CustomerContractRepositoryCustom {
    Optional<CustomerContract> findByIdAndValidYn(Integer id, EnumValidYn validYn);
    boolean existsByCustomerId(String customerId);
}
