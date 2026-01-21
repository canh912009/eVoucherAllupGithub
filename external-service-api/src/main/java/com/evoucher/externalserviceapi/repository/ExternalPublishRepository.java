package com.evoucher.externalserviceapi.repository;

import com.evoucher.externalserviceapi.entity.ExternalPublish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ExternalPublishRepository extends JpaRepository<ExternalPublish, String> {
}
