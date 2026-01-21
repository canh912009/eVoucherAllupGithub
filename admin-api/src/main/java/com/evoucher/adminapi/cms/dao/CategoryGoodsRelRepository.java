package com.evoucher.adminapi.cms.dao;

import com.evoucher.adminapi.cms.dao.models.CategoryGoodsId;
import com.evoucher.adminapi.cms.dao.models.CategoryGoodsRel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryGoodsRelRepository extends JpaRepository<CategoryGoodsRel, CategoryGoodsId> {

    void deleteCategoryGoodsRelByGoodsId(Integer goodsId);
}
