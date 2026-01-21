package com.evoucher.adminapi.admin.dao;

import com.evoucher.adminapi.admin.service.models.FilterSearchAdmin;
import com.evoucher.adminapi.admin.service.models.FilterSearchCustomerContract;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomerContractRepositoryCustom {
    List<FilterSearchCustomerContract> searchContract(FilterSearchAdmin filterSearchAdmin, Pageable pageable);

    Long countContract(FilterSearchAdmin filterSearchAdmin, Pageable pageable);
}
