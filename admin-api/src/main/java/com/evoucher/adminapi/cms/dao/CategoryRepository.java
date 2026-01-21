package com.evoucher.adminapi.cms.dao;

import com.evoucher.adminapi.cms.dao.models.Category;
import com.evoucher.adminapi.common.enums.EnumValidYn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> , CategoryRepositoryCustom{

    @Query(value = "select c.* from tb_category c " +
            "    join tb_category_goods_rel tcgr on c.ctgr_cd = tcgr.ctgr_cd " +
            "    join tb_goods tg on tg.goods_id = tcgr.goods_id " +
            " where tg.goods_id = ?1 and c.valid_yn = 'Y'; ", nativeQuery = true)
    List<Category> findByGoodIdAndValidYnIsYes(Integer goodsId);

    @Query(value = "select c.* from tb_category c " +
            "  where c.ctgr_cd in (?1)", nativeQuery = true)
    List<Category> findByCategoryCodeExists(List<String> categoriesId);

    Optional<Category> findByCategoryCodeAndValidYn(String id, EnumValidYn validYn);
}
