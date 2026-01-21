package com.evoucher.adminapi.auth.dao;

import com.evoucher.adminapi.auth.dao.models.Menu;
import com.evoucher.adminapi.common.enums.EnumValidYn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Integer>, MenuRepositoryCustom {
    Optional<Menu> findByIdAndValidYn(Integer id, EnumValidYn validYn);

    List<Menu> findAllByMenuGroupIdAndValidYn(Integer menuGroupId, EnumValidYn validYN);

    @Query(value = "select m.* from tb_menu m " +
            "join tb_role_menu_rel rm on m.menu_id = rm.menu_id " +
            "join tb_role r on rm.role_code = r.role_code" +
            " where r.role_code = ?1 and m.valid_yn = ?2", nativeQuery = true)
    List<Menu> findByRoleCodeAndValidYn(String roleCode, String validYn);
}
