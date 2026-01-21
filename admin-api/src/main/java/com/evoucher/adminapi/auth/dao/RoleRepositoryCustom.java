package com.evoucher.adminapi.auth.dao;

import com.evoucher.adminapi.auth.service.models.FilterSearchAuth;
import com.evoucher.adminapi.auth.service.models.RoleDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface RoleRepositoryCustom {
    List<RoleDTO> searchRole(FilterSearchAuth filterSearchAuth, Pageable pageable);

    Long countRole(FilterSearchAuth filterSearchAuth);
}
