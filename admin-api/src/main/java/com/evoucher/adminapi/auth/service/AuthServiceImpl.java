package com.evoucher.adminapi.auth.service;

import com.evoucher.adminapi.auth.dao.AdminRepository;
import com.evoucher.adminapi.auth.dao.IpWhitelistRepository;
import com.evoucher.adminapi.auth.dao.models.Admin;
import com.evoucher.adminapi.auth.dao.models.IpWhitelist;
import com.evoucher.adminapi.auth.service.models.JwtDTO;
import com.evoucher.adminapi.auth.service.models.UserPrincipal;
import com.evoucher.adminapi.auth.service.models.AdminLoginRequest;
import com.evoucher.adminapi.common.enums.EnumRole;
import com.evoucher.adminapi.common.enums.EnumValidYn;
import com.evoucher.adminapi.common.exception.CustomCodeException;
import com.evoucher.adminapi.common.service.RedisService;
import com.evoucher.adminapi.common.utils.IPUtils;
import com.evoucher.adminapi.common.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AdminRepository adminRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RedisService redisService;
    private final IpWhitelistRepository ipWhitelistRepository;
    private final IPUtils ipUtils;

    @Value("${auth.expirationTime}")
    private long expirationTime;

    private final String SUCCESS = "success";
    private final String FAIL = "fail";

    @Override
    public JwtDTO login(AdminLoginRequest adminLoginRequest) {
        try {
//            ipWhitelistAdminOperator(adminLoginRequest);

            // Conduct authentication
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(adminLoginRequest.getPhoneNumber(),
                            adminLoginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

            log.info("Create token with adminId: {}", userPrincipal.getId());
            JwtDTO jwtDTO = jwtService.createToken(userPrincipal);

            // update last login date for admin
            updateAdminLoginInfo(adminLoginRequest.getPhoneNumber(), SUCCESS);

            return jwtDTO;
        } catch (BadCredentialsException e) {
            log.error("Login fail with phoneNumber: {} error: {}", adminLoginRequest.getPhoneNumber(), e.getMessage());

            // update login fail count for admin
            updateAdminLoginInfo(adminLoginRequest.getPhoneNumber(), FAIL);
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.account.invalid"),
                    HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            log.error("Login fail with phoneNumber: {} error: {}", adminLoginRequest.getPhoneNumber(), e.getMessage());
            throw e;
        }
    }

    private void ipWhitelistAdminOperator(AdminLoginRequest adminLoginRequest) {
        // check user exists
        Optional<Admin> adminOptional = adminRepository.findAdminByMobileNumberAndValidYn(adminLoginRequest.getPhoneNumber(), EnumValidYn.Y);
        if (adminOptional.isEmpty()) {
            log.warn("Admin not found with phoneNumber: {}", adminLoginRequest.getPhoneNumber());
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.account.invalid"), HttpStatus.UNAUTHORIZED);
        }

        Admin admin = adminOptional.get();

        // check IP whitelist for ADMIN and OPERATOR
        if (EnumRole.ROLE_ADMIN.name().equals(admin.getRoleCode()) || EnumRole.ROLE_OPERATOR.name().equals(admin.getRoleCode())) {
            String clientIp = ipUtils.getClientIp();
            Optional<IpWhitelist> whitelistedIp = ipWhitelistRepository.findByIpAddressAndValidYn(
                    clientIp,
                    EnumValidYn.Y
            );

            if (whitelistedIp.isEmpty()) {
                log.warn("IP {} attempted to login as {} role", clientIp, admin.getRoleCode());
                updateAdminLoginInfo(adminLoginRequest.getPhoneNumber(), FAIL);
                throw new CustomCodeException(
                        MessageUtils.getMessage("evoucher.ip.not.allowed"),
                        HttpStatus.FORBIDDEN
                );
            }
        }
    }

    @Override
    public void logout(String jwtToken) {
        String token = jwtToken.replace("Bearer ", "");
        long expirationTimeMinutes = expirationTime/1000/60;
        redisService.setRedisByKeyAndExpire(token, token, expirationTimeMinutes);
    }

    private void updateAdminLoginInfo(String phoneNumber, String action) {
        log.info("Find admin with phoneNumber: {}", phoneNumber);
        Optional<Admin> adminOptional = adminRepository.findAdminByMobileNumberAndValidYn(phoneNumber, EnumValidYn.Y);
        if (adminOptional.isEmpty()) {
            log.info("Admin not found with phoneNumber: {}", phoneNumber);
            return;
        }

        Admin admin = adminOptional.get();
        switch (action) {
            case SUCCESS: {
                admin.setLastLoginDate(new Date());
                break;
            }
            case FAIL: {
                admin.setLoginFailCount(admin.getLoginFailCount() + 1);
                break;
            }
            default:
                break;
        }
        log.info("Save last login date for adminId: {}", admin.getId());
        adminRepository.save(admin);
    }
    @Override
    public String getLoggedInUserId() {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Optional.ofNullable(userPrincipal).map(UserPrincipal::getId).orElse(null);

    }
}
