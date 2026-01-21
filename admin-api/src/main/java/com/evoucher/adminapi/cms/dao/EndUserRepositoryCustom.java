package com.evoucher.adminapi.cms.dao;

import com.evoucher.adminapi.cms.service.models.EndUserDTO;
import com.evoucher.adminapi.cms.service.models.FilterSearchCms;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EndUserRepositoryCustom {
    List<EndUserDTO> searchEndUser(FilterSearchCms filterSearchCms, Pageable pageable);

    Long countUser(FilterSearchCms filterSearchCms, Pageable pageable);
}