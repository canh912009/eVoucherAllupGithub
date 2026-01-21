package com.evoucher.adminapi.admin.dao;

import com.evoucher.adminapi.admin.dao.models.SupplierContract;
import com.evoucher.adminapi.common.enums.EnumValidYn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SupplierContractRepository extends JpaRepository<SupplierContract, Integer>, SupplierContractRepositoryCustom {

    Optional<SupplierContract> findByIdAndValidYn(Integer id, EnumValidYn validYn);
}
