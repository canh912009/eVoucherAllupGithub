package com.evoucher.adminapi.auth.dao;

import com.evoucher.adminapi.auth.dao.models.Code;
import com.evoucher.adminapi.auth.dao.models.CodeId;
import com.evoucher.adminapi.common.enums.EnumValidYn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CodeRepository extends JpaRepository<Code, CodeId>, CodeRepositoryCustom {

    Optional<Code> findByCodeIdAndCodeGroupIdAndValidYn(String codeId, String codeGroupId, EnumValidYn validYn);

    List<Code> findAllByCodeGroupIdAndValidYnOrderBySortOrder(String codeGroupId, EnumValidYn validYn);

    boolean existsByCodeIdAndCodeGroupIdAndValidYn(String codeId, String codeGroupId, EnumValidYn validYn);
}
