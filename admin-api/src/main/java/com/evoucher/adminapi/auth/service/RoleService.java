package com.evoucher.adminapi.auth.service;

import com.evoucher.adminapi.auth.service.models.FilterSearchAuth;
import com.evoucher.adminapi.auth.service.models.RoleDTO;
import com.evoucher.adminapi.auth.service.models.RoleRequest;
import org.springframework.data.domain.Page;

public interface RoleService {
    RoleDTO findById(String roleCode);

    RoleDTO createRole(RoleRequest roleRequest);

    RoleDTO updateRole(String roleCode, RoleRequest roleRequest);

    String deleteRole(String roleCode);

    Page<RoleDTO> searchRole(FilterSearchAuth filterSearchAuth);
}
