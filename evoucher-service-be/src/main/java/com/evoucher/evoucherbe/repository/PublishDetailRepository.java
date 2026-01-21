package com.evoucher.evoucherbe.repository;

import com.evoucher.evoucherbe.entity.PublishDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublishDetailRepository extends JpaRepository<PublishDetail, Integer> {
}
