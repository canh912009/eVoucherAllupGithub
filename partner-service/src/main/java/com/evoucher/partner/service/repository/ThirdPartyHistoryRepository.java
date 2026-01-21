package com.evoucher.partner.service.repository;

import com.evoucher.partner.service.bean.entity.ThirdPartyHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThirdPartyHistoryRepository extends JpaRepository<ThirdPartyHistory, Long> {
}
