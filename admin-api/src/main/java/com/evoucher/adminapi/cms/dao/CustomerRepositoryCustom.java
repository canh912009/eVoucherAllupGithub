package com.evoucher.adminapi.cms.dao;

import com.evoucher.adminapi.cms.service.models.CustomerDTO;
import com.evoucher.adminapi.cms.service.models.FilterSearchCms;
import com.evoucher.adminapi.cms.service.models.GoodsDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomerRepositoryCustom {
    List<CustomerDTO> searchCustomer(FilterSearchCms filterSearchCms, Pageable pageable);

    Long countCustomer(FilterSearchCms filterSearchCms, Pageable pageable);
}
