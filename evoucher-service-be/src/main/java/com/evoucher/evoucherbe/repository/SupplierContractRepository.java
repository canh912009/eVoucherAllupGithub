package com.evoucher.evoucherbe.repository;

import com.evoucher.evoucherbe.common.enums.EnumValidYn;
import com.evoucher.evoucherbe.entity.SupplierContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SupplierContractRepository extends JpaRepository<SupplierContract, Integer> {

    Optional<SupplierContract> findByIdAndValidYn(Integer id, EnumValidYn validYn);
}
