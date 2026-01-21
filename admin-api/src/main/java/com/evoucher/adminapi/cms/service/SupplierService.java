package com.evoucher.adminapi.cms.service;

import com.evoucher.adminapi.admin.service.models.ApproveRequest;
import com.evoucher.adminapi.cms.service.models.FilterSearchCms;
import com.evoucher.adminapi.cms.service.models.SupplierDTO;
import com.evoucher.adminapi.cms.service.models.request.SupplierRequest;
import org.springframework.data.domain.Page;

public interface SupplierService {

    Page<SupplierDTO> searchSupplierDTO(FilterSearchCms filterSearchCms);

    SupplierDTO findDtoById(String id);

    SupplierDTO createSupplier(SupplierRequest supplierRequest);

    SupplierDTO updateSupplier(String id, SupplierRequest supplierRequest);

    String deleteSupplierById(String id);

    String updateStatusSupplier(String id, ApproveRequest approveRequest);
}
