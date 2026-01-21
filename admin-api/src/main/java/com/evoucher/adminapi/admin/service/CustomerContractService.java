package com.evoucher.adminapi.admin.service;

import com.evoucher.adminapi.admin.service.models.*;
import org.springframework.data.domain.Page;


public interface CustomerContractService {
    CustomerContractDTO findById(Integer id);

    CustomerContractDTO createContract(CustomerContractRequest customerContractRequest);

    CustomerContractDTO updateContract(Integer id, CustomerContractRequest customerContractRequest);

    Integer deleteContractById(Integer id);

    Page<FilterSearchCustomerContract> searchContract(FilterSearchAdmin filterSearchAdmin);

    Integer approveStatusContract(Integer id, ApproveRequest approveRequest);
}
