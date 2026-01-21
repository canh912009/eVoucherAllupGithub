package com.evoucher.adminapi.cms.dao;

import com.evoucher.adminapi.cms.dao.models.Supplier;
import com.evoucher.adminapi.common.enums.ApproveStatus;
import com.evoucher.adminapi.common.enums.EnumValidYn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, String>, SupplierRepositoryCustom {

    Optional<Supplier> findByIdAndValidYn(String id, EnumValidYn validYn);
    Optional<Supplier> findByIdAndApproveStatusCodeAndValidYn(String id, ApproveStatus approveStatus, EnumValidYn validYn);
    @Query(value = "select id from Supplier where id like ?1 order by id asc")
    List<String> findAllSupplierIdByRegexIdAndIdASC(String regexId);
    boolean existsByIdAndValidYn(String id, EnumValidYn validYn);
}
