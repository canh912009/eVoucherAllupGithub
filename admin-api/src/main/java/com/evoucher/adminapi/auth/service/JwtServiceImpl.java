package com.evoucher.adminapi.auth.service;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.evoucher.adminapi.auth.service.models.JwtDTO;
import com.evoucher.adminapi.auth.service.models.UserPrincipal;
import com.evoucher.adminapi.common.exception.CustomCodeException;
import com.evoucher.adminapi.common.service.RedisService;
import com.evoucher.adminapi.common.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.Objects;


@Service
@Slf4j
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    @Value("${auth.expirationTime}")
    private long expirationTime;
    @Value("${auth.secret}")
    private String secret;

    private final RedisService redisService;


    @Override
    public JwtDTO createToken(UserPrincipal user) {
        String username = user.getUsername();
        String token = JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                .sign(Algorithm.HMAC512(secret));

        return JwtDTO.builder()
                .token(token)
                .roleCode(user.getAdminType())
                .adminCorpId(user.getAdminCorpId())
                .expiredDate(expirationTime)
                .build();
    }

    @Override
    public String genToken(String token) {
        try {
//            log.info("Check token has logged out with token: {}", token);
            if (Objects.nonNull(redisService.getRedisByKey(token))) {
                throw new CustomCodeException("Client has logged out!", HttpStatus.UNAUTHORIZED);
            }
        } catch (CustomCodeException e) {
            log.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Get token by redis error: {}", e.getMessage(), e);
        }

        return JWT.require(Algorithm.HMAC512(secret))
                .build()
                .verify(token)
                .getSubject();
    }
}
