package com.evoucher.externalserviceapi.api;

import com.evoucher.externalserviceapi.common.exception.ClientNotLoggedInException;
import com.evoucher.externalserviceapi.common.message.BaseResponse;
import com.evoucher.externalserviceapi.service.model.request.AuthLogin;
import com.evoucher.externalserviceapi.service.remote.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/")
public class AuthenticationController {

    private final AuthService authService;

    @PostMapping("/v1/auth")
    public ResponseEntity<BaseResponse> login(
            @Valid @RequestBody AuthLogin authLogin)
            throws IOException {
        log.info("Login with request: {}", authLogin);
        return new ResponseEntity<>(authService.login(authLogin), HttpStatus.OK);
    }

    @PostMapping("/v1/logout")
    public ResponseEntity<BaseResponse> logout()
            throws ClientNotLoggedInException, IOException {
        log.info("Logout");
        return new ResponseEntity<>(authService.logout(), HttpStatus.OK);
    }
}
