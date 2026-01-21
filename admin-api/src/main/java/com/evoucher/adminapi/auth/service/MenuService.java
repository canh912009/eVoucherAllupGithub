package com.evoucher.adminapi.auth.service;

import com.evoucher.adminapi.auth.service.models.FilterSearchAuth;
import com.evoucher.adminapi.auth.service.models.MenuDTO;
import com.evoucher.adminapi.auth.service.models.MenuRequest;
import org.springframework.data.domain.Page;

public interface MenuService {
    MenuDTO findById(Integer id);

    MenuDTO createMenu(MenuRequest menuRequest);

    MenuDTO editMenu(Integer id, MenuRequest menuRequest);

    Integer deleteMenu(Integer id);

    Page<MenuDTO> searchMenu(FilterSearchAuth filterSearchAuth);
}
