package com.evoucher.adminapi.auth.dao;

import com.evoucher.adminapi.auth.dao.models.MenuGroup;
import com.evoucher.adminapi.common.enums.EnumValidYn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MenuGroupRepository extends JpaRepository<MenuGroup, Integer>, MenuGroupRepositoryCustom {
    Optional<MenuGroup> findByIdAndValidYn(Integer id, EnumValidYn validYn);
}
