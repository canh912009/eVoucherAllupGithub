package com.evoucher.adminapi.partner.dao;

import com.evoucher.adminapi.partner.dao.model.ExternalPublish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ExternalPublishRepository extends JpaRepository<ExternalPublish, String> {
}
