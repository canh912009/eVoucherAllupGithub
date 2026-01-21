package com.evoucher.evoucherbe.repository;

import com.evoucher.evoucherbe.common.enums.EnumValidYn;
import com.evoucher.evoucherbe.entity.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Integer> {
    Optional<Campaign> findByIdAndApproveStatusCodeAndValidYn(Integer id, String approveStatusCode, EnumValidYn validYn);
}
