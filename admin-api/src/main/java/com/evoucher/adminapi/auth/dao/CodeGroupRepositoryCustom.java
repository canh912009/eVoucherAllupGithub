package com.evoucher.adminapi.auth.dao;

import com.evoucher.adminapi.auth.service.models.FilterSearchAuth;
import com.evoucher.adminapi.auth.service.models.CodeGroupDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CodeGroupRepositoryCustom {
    List<CodeGroupDTO> searchCodeGroup(FilterSearchAuth filterSearchAuth, Pageable pageable);

    Long countCodeGroup(FilterSearchAuth filterSearchAuth);
}
