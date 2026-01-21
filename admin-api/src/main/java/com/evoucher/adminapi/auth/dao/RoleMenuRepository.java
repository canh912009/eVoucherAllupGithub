package com.evoucher.adminapi.auth.dao;

import com.evoucher.adminapi.auth.dao.models.RoleMenu;
import com.evoucher.adminapi.auth.dao.models.RoleMenuId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleMenuRepository extends JpaRepository<RoleMenu, RoleMenuId> {

    void deleteAllByRoleCode(String roleCode);
}
