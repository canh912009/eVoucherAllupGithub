package com.evoucher.adminapi.auth.dao;

import com.evoucher.adminapi.auth.dao.models.CodeGroup;
import com.evoucher.adminapi.common.enums.EnumValidYn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CodeGroupRepository extends JpaRepository<CodeGroup, String>, CodeGroupRepositoryCustom {
    boolean existsByCodeGroupIdAndValidYn(String codeGroupId, EnumValidYn validYn);
    Optional<CodeGroup> findByCodeGroupIdAndValidYn(String codeGroupId, EnumValidYn validYn);
}
