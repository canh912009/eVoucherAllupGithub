package com.evoucher.adminapi.admin.dao;

import com.evoucher.adminapi.admin.dao.models.SupplierContractApproveHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierContractApproveHistoryRepository extends JpaRepository<SupplierContractApproveHistory, Integer> {
}
