package com.evoucher.adminapi.auth.dao;

import com.evoucher.adminapi.auth.service.models.CodeDTO;
import com.evoucher.adminapi.auth.service.models.FilterSearchAuth;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CodeRepositoryCustom {
    List<CodeDTO> searchCode(FilterSearchAuth filterSearchAuth, Pageable pageable);

    Long countCode(FilterSearchAuth filterSearchAuth);
}
