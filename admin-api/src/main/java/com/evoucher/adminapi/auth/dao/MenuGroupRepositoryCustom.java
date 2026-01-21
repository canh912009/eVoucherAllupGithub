package com.evoucher.adminapi.auth.dao;

import com.evoucher.adminapi.auth.service.models.FilterSearchAuth;
import com.evoucher.adminapi.auth.service.models.MenuGroupDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MenuGroupRepositoryCustom {
    List<MenuGroupDTO> searchMenuGroup(FilterSearchAuth filterSearchAuth, Pageable pageable);

    Long countMenuGroup(FilterSearchAuth filterSearchAuth);
}
