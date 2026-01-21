package com.evoucher.adminapi.admin.dao;

import com.evoucher.adminapi.admin.service.models.FilterSearchAdmin;
import com.evoucher.adminapi.admin.service.models.FilterSearchSupplierContract;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface SupplierContractRepositoryCustom {
    List<FilterSearchSupplierContract> searchContract(FilterSearchAdmin filterSearchAdmin, Pageable pageable);

    Long countContract(FilterSearchAdmin filterSearchAdmin, Pageable pageable);
}
