package com.evoucher.adminapi.cms.service;

import com.evoucher.adminapi.cms.service.models.EndUserDTO;
import com.evoucher.adminapi.cms.service.models.FilterSearchCms;
import com.evoucher.adminapi.cms.service.models.request.EndUserRequest;
import org.springframework.data.domain.Page;

public interface EndUserService {

    EndUserDTO findById(String id);

    EndUserDTO createEndUser(EndUserRequest endUserRequest);

    EndUserDTO updateEndUser(String id, EndUserRequest endUserRequest);

    String deleteEndUserById(String id);

    Page<EndUserDTO> search(FilterSearchCms filterSearchCms);

}
