package com.evoucher.adminapi.admin.dao;

import com.evoucher.adminapi.admin.dao.models.CampaignApproveHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CampaignApproveHistoryRepository extends JpaRepository<CampaignApproveHistory, Integer> {
}
