package com.evoucher.adminapi.admin.dao;

import com.evoucher.adminapi.admin.service.models.CsManagementFilterRequest;
import com.evoucher.adminapi.admin.service.models.CsManagementResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CsManagementRepository {
    List<CsManagementResponse> search(CsManagementFilterRequest filter, Pageable pageable);
    long count(CsManagementFilterRequest filter);

}
