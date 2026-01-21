package com.evoucher.adminapi.admin.dao;

import com.evoucher.adminapi.admin.dao.models.CustomerContractApproveHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerContractApproveHistoryRepository extends JpaRepository<CustomerContractApproveHistory, Integer> {
}
