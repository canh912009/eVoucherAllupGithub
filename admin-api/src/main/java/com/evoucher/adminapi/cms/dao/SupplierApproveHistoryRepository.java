package com.evoucher.adminapi.cms.dao;

import com.evoucher.adminapi.cms.dao.models.SupplierApproveHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierApproveHistoryRepository extends JpaRepository<SupplierApproveHistory, Integer> {
}
