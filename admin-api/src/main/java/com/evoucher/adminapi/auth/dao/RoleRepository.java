package com.evoucher.adminapi.auth.dao;

import com.evoucher.adminapi.auth.dao.models.Role;
import com.evoucher.adminapi.common.enums.EnumValidYn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, String>, RoleRepositoryCustom {
    boolean existsByRoleCodeAndValidYn(String roleCode, EnumValidYn validYn);

    Optional<Role> findByRoleCodeAndValidYn(String roleCode, EnumValidYn validYn);
}
