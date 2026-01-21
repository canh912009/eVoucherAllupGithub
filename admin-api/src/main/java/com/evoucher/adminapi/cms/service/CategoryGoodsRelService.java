package com.evoucher.adminapi.cms.service;


import com.evoucher.adminapi.cms.service.models.CategoryGoodsRelDTO;
import com.evoucher.adminapi.cms.service.models.request.CategoryGoodsRelRequest;

import java.util.List;

public interface CategoryGoodsRelService {
    List<CategoryGoodsRelDTO> findAll();

    CategoryGoodsRelDTO findById(String id);

    CategoryGoodsRelDTO createCategoryGoodsRel(CategoryGoodsRelRequest categoryGoodsRelRequest);

    CategoryGoodsRelDTO updateCategoryGoodsRel(CategoryGoodsRelRequest categoryGoodsRelRequest);

    boolean delete(String id);
}
