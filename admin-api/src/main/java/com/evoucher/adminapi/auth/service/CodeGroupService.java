package com.evoucher.adminapi.auth.service;

import com.evoucher.adminapi.auth.service.models.FilterSearchAuth;
import com.evoucher.adminapi.auth.service.models.CodeGroupDTO;
import com.evoucher.adminapi.auth.service.models.CodeGroupRequest;
import org.springframework.data.domain.Page;

public interface CodeGroupService {
    CodeGroupDTO findById(String id);

    CodeGroupDTO createCodeGroup(CodeGroupRequest menuGroupRequest);

    CodeGroupDTO editCodeGroup(String id, CodeGroupRequest menuGroupRequest);

    String deleteCodeGroup(String id);

    Page<CodeGroupDTO> searchCodeGroup(FilterSearchAuth filterSearchAuth);
}
