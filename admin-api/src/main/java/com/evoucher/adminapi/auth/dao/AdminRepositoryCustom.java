package com.evoucher.adminapi.auth.dao;

import com.evoucher.adminapi.auth.service.models.FilterSearchAuth;
import com.evoucher.adminapi.auth.service.models.SearchAdminResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface AdminRepositoryCustom {
    List<SearchAdminResponse> searchAdmin(FilterSearchAuth filterSearchAuth, Pageable pageable);

    Long countAdmin(FilterSearchAuth filterSearchAuth);
}
