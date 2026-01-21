package com.castis.publishservice.repository;

import com.castis.publishservice.entity.ThirdPartyCallAPIHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThirdPartyCallAPIHistoryRepository extends JpaRepository<ThirdPartyCallAPIHistory, Long> {
    List<ThirdPartyCallAPIHistory> findAllBySystemAndResultAndAction(String system, String result, String action);
}
