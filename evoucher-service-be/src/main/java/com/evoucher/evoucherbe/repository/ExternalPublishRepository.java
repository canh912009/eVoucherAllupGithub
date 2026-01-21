package com.evoucher.evoucherbe.repository;

import com.evoucher.evoucherbe.entity.ExternalPublish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ExternalPublishRepository extends JpaRepository<ExternalPublish, String> {
}
