package com.evoucher.adminapi.auth.service;


import com.evoucher.adminapi.auth.service.models.JwtDTO;
import com.evoucher.adminapi.auth.service.models.UserPrincipal;

public interface JwtService {

    JwtDTO createToken(UserPrincipal user);

    String genToken(String token);
}
