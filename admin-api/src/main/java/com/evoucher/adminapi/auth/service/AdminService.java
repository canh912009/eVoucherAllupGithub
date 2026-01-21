package com.evoucher.adminapi.auth.service;

import com.evoucher.adminapi.auth.service.models.*;
import com.evoucher.adminapi.common.exception.CustomCodeException;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AdminService extends UserDetailsService {

    AdminDTO findById(String id);

    String createAdmin(AdminRequest adminRequest);

    String updateAdmin(String id, AdminUpdateRequest adminUpdateRequest);

    String deleteAdmin(String id);

    Page<SearchAdminResponse> searchAdmin(FilterSearchAuth filterSearchAuth);

    AdminDTO getAdminCurrent();

    void changePassword(AdminChangePasswordRequest changePasswordRequest);
    void validateCorpIdPermission(String customerId) throws CustomCodeException;
}
