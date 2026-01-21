package com.evoucher.adminapi.admin.dao;

import com.evoucher.adminapi.admin.dao.models.OperatorRequest;
import com.evoucher.adminapi.admin.enums.OperatorRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperatorRequestRepository extends JpaRepository<OperatorRequest, Long> , OperatorReqRepoCustom {
    List<OperatorRequest> findAllByEv(String ev);
    Long countAllByEvAndReqStatus(String ev, OperatorRequestStatus status);
}
