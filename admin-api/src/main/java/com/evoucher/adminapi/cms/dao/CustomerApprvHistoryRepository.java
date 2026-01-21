package com.evoucher.adminapi.cms.dao;

import com.evoucher.adminapi.cms.dao.models.CustomerApprvHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerApprvHistoryRepository extends JpaRepository<CustomerApprvHistory, Integer> {

}
