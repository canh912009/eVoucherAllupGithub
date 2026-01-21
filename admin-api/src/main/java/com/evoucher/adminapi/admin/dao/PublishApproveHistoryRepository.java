package com.evoucher.adminapi.admin.dao;

import com.evoucher.adminapi.admin.dao.models.PublishApproveHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublishApproveHistoryRepository extends JpaRepository<PublishApproveHistory, Integer> {
}
