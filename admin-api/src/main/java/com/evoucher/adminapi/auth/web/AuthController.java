package com.evoucher.adminapi.auth.web;

import com.evoucher.adminapi.auth.service.AuthService;
import com.evoucher.adminapi.auth.service.models.AdminLoginRequest;
import com.evoucher.adminapi.common.message.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<BaseResponse> login(@Valid @RequestBody AdminLoginRequest adminLoginRequest) {
        return new ResponseEntity<>(
                new BaseResponse(authService.login(adminLoginRequest)),
                HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken) {
        log.info("Logout with jwtToken: {}", jwtToken);
        authService.logout(jwtToken);
        return ResponseEntity.ok("");
    }
}
