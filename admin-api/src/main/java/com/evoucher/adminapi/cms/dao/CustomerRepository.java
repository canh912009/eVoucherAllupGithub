package com.evoucher.adminapi.cms.dao;

import com.evoucher.adminapi.cms.dao.models.Customer;
import com.evoucher.adminapi.common.enums.ApproveStatus;
import com.evoucher.adminapi.common.enums.EnumValidYn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String>, CustomerRepositoryCustom {

    Optional<Customer> findByIdAndValidYn(String id, EnumValidYn validYn);
    Optional<Customer> findByIdAndApproveStatusCodeAndValidYn(String id, ApproveStatus approveStatus, EnumValidYn validYn);

    @Query(value = "select id from Customer where id like ?1 order by id asc")
    List<String> findAllCustomerIdByRegexIdAndIdASC(String regexId);
}
