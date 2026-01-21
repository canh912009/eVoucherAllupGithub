package com.evoucher.adminapi.cms.service;


import com.evoucher.adminapi.admin.service.models.ApproveRequest;
import com.evoucher.adminapi.cms.service.models.CustomerDTO;
import com.evoucher.adminapi.cms.service.models.FilterSearchCms;
import com.evoucher.adminapi.cms.service.models.request.CustomerRequest;
import org.springframework.data.domain.Page;

public interface CustomerService {
    CustomerDTO findById(String id);

    CustomerDTO createCustomer(CustomerRequest customerRequest);

    CustomerDTO updateCustomer(String id, CustomerRequest customerRequest);

    String delete(String id);

    String updateStatusCustomer(String id, ApproveRequest approveRequest);

    Page<CustomerDTO> searchCustomer(FilterSearchCms filterSearchCms);


}
