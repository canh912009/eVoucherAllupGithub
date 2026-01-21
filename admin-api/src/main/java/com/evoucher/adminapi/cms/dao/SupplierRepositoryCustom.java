package com.evoucher.adminapi.cms.dao;

import com.evoucher.adminapi.cms.service.models.FilterSearchCms;
import com.evoucher.adminapi.cms.service.models.SupplierDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SupplierRepositoryCustom {

    List<SupplierDTO> searchSupplier(FilterSearchCms filterSearchCms, Pageable pageable);

    long countSupplier(FilterSearchCms filterSearchCms);
}
