package com.evoucher.adminapi.auth.dao;

import com.evoucher.adminapi.auth.service.models.FilterSearchAuth;
import com.evoucher.adminapi.auth.service.models.MenuDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MenuRepositoryCustom {
    List<MenuDTO> searchMenu(FilterSearchAuth filterSearchAuth, Pageable pageable);

    Long countMenu(FilterSearchAuth filterSearchAuth);
}
