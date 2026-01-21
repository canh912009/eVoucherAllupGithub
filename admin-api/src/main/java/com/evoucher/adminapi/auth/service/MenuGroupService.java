package com.evoucher.adminapi.auth.service;

import com.evoucher.adminapi.auth.service.models.FilterSearchAuth;
import com.evoucher.adminapi.auth.service.models.MenuGroupDTO;
import com.evoucher.adminapi.auth.service.models.MenuGroupRequest;
import org.springframework.data.domain.Page;

public interface MenuGroupService {
    MenuGroupDTO findById(Integer id);

    MenuGroupDTO createMenuGroup(MenuGroupRequest menuGroupRequest);

    MenuGroupDTO editMenuGroup(Integer id, MenuGroupRequest menuGroupRequest);

    Integer deleteMenuGroup(Integer id);

    Page<MenuGroupDTO> searchMenuGroup(FilterSearchAuth filterSearchAuth);
}
