package com.evoucher.adminapi.admin.dao;

import com.evoucher.adminapi.admin.dao.models.Campaign;
import com.evoucher.adminapi.common.enums.EnumValidYn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Integer>, CampaignRepositoryCustom {
    Optional<Campaign> findByIdAndValidYn(Integer id, EnumValidYn validYn);
    Optional<Campaign> findFirstByCustomerIdOrderByEndDateDesc(String customerId);
    boolean existsByCustomerId(String customerId);
}
