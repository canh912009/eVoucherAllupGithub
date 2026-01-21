package com.evoucher.adminapi.cms.dao;

import com.evoucher.adminapi.admin.dao.models.MessageTemplate;
import com.evoucher.adminapi.cms.dao.models.VnptProvider;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VnptProviderRepository extends JpaRepository<VnptProvider, String> {
    long count(Specification<MessageTemplate> condition);
    List<VnptProvider> findAll(Specification<MessageTemplate> condition, Pageable pageable);

}