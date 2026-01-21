package com.evoucher.adminapi.admin.dao;

import com.evoucher.adminapi.admin.dao.models.MessageTemplate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageTemplateRepository extends JpaRepository<MessageTemplate, Integer> {
    List<MessageTemplate> findAll(Specification<MessageTemplate> filters);
}
