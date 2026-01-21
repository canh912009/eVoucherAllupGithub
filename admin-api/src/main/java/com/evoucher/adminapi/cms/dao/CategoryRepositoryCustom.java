package com.evoucher.adminapi.cms.dao;

import com.evoucher.adminapi.cms.dao.models.Category;
import com.evoucher.adminapi.cms.service.models.FilterSearchCms;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryRepositoryCustom {
    List<Category> searchCategory(FilterSearchCms filterSearchCms, Pageable pageable);

    Long countCategory(FilterSearchCms filterSearchCms, Pageable pageable);
}
