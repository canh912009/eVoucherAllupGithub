package com.evoucher.evoucherbe.repository;

import com.evoucher.evoucherbe.entity.Publish;
import com.evoucher.evoucherbe.common.enums.ApproveStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PublishRepository extends JpaRepository<Publish, Integer> {
    Optional<Publish> findByIdAndApproveStatusCode(Integer id, ApproveStatus approveStatus);
}
