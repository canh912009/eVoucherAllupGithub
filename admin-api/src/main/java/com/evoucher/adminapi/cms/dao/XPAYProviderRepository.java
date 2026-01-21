package com.evoucher.adminapi.cms.dao;

import com.evoucher.adminapi.admin.dao.models.MessageTemplate;
import com.evoucher.adminapi.cms.dao.models.XPAYProvider;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XPAYProviderRepository extends JpaRepository<XPAYProvider, String> {
    long count(Specification<MessageTemplate> condition);
    List<XPAYProvider> findAll(Specification<MessageTemplate> condition, Pageable pageable);

}