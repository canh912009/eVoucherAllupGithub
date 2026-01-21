package com.evoucher.adminapi.auth.service;

import com.evoucher.adminapi.auth.service.models.*;
import org.springframework.data.domain.Page;

public interface CodeService {
    CodeDTO findById(String codeId, String codeGroupId);

    CodeDTO createCode(CodeRequest codeRequest);

    CodeDTO editCode(String codeId, String codeGroupId, CodeRequest codeRequest);

    String deleteCode(String codeId, String codeGroupId);

    Page<CodeDTO> searchCode(FilterSearchAuth filterSearchAuth);
}
