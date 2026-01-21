package com.evoucher.adminapi.auth.service;


import com.evoucher.adminapi.auth.service.models.JwtDTO;
import com.evoucher.adminapi.auth.service.models.AdminLoginRequest;

public interface AuthService {

    JwtDTO login(AdminLoginRequest adminLoginRequest);

    void logout(String jwtToken);
    public String getLoggedInUserId();
}
