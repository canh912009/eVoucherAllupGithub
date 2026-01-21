package com.evoucher.adminapi.admin.service;

import com.evoucher.adminapi.admin.service.models.*;
import org.springframework.data.domain.Page;

public interface SupplierContractService {
    SupplierContractDTO findById(Integer id);

    SupplierContractDTO createContract(SupplierContractRequest customerContractRequest);

    SupplierContractDTO updateContract(Integer id, SupplierContractRequest customerContractRequest);

    Integer deleteContractById(Integer id);

    Page<FilterSearchSupplierContract> searchContract(FilterSearchAdmin filterSearchAdmin);

    Integer approveStatusContract(Integer id, ApproveRequest approveRequest);
}
