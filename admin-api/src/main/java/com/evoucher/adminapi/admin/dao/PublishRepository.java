package com.evoucher.adminapi.admin.dao;

import com.evoucher.adminapi.admin.dao.models.Publish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PublishRepository extends JpaRepository<Publish, Integer>, PublishRepositoryCustom {
    List<Publish> findAllByCampaignId(Integer campaignId);
    List<Publish> findAllByTransactionId(String transactionId);
    Optional<Publish> findByIdAndCustomerId(Integer publishId, String customerId);
}
