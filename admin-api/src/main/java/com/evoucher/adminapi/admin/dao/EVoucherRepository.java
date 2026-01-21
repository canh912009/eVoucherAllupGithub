package com.evoucher.adminapi.admin.dao;

import com.evoucher.adminapi.admin.dao.models.EVoucher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EVoucherRepository extends JpaRepository<EVoucher, String> {

    Optional<EVoucher> findByPublishDetailId(Integer publishDetailId);
    Optional<List<EVoucher>> findByPublishId(Integer publishId);
}
