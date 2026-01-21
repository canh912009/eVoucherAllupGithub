package com.evoucher.adminapi.common.config;

import com.evoucher.adminapi.auth.service.models.UserPrincipal;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class LoggedInUserContext {

  public static void setLoggedInUser(UserPrincipal user) {
    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    SecurityContextHolder.getContext().setAuthentication(auth);
  }

  public static UserPrincipal getLoggedInUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return (UserPrincipal) authentication.getPrincipal();
  }

  public static void clearContext() {
    SecurityContextHolder.clearContext();
  }
}
