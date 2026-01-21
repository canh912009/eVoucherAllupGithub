package com.castis.pos_api.filter;

import com.castis.pos_api.dto.response.ResponseData;
import com.castis.pos_api.entity.Brand;
import com.castis.pos_api.enum_constant.EnumValidYn;
import com.castis.pos_api.repositories.BrandRepository;
import com.castis.pos_api.utils.CustomResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthHeaderFilter extends OncePerRequestFilter {

    private final BrandRepository brandRepository;

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authCd = request.getHeader("authCd");
        String appId = request.getHeader("appId");
        log.info("AppId: {}", appId);
        log.info("authCd: {}", authCd);
        String clientIp = getClientIp(request);
        log.info("Client IP: {}", clientIp);

        if (Objects.isNull(appId) || appId.isEmpty()) {
            handlerFailAuthenticate(response, CustomResponse.E4001_APP_ID);
            return;
        }

        Optional<Brand> brandOptional = brandRepository.findByAppIdAndValidYn(appId, EnumValidYn.Y);
        if (brandOptional.isEmpty()) {
            handlerFailAuthenticate(response, CustomResponse.E4001_APP_ID);
            return;
        }
        Brand brand = brandOptional.get();

        if (Objects.isNull(authCd) || authCd.isEmpty() || !authCd.equals(brand.getAuthenticationKey())) {
            handlerFailAuthenticate(response, CustomResponse.E4002_AUTH_CD);
            return;
        }

        if (Objects.nonNull(brand.getIpWhiteList()) && List.of(brand.getIpWhiteList().split(",")).contains(clientIp)) {
            handlerFailAuthenticate(response, CustomResponse.E4004_IP_NOT_ALLOWED);
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return !path.startsWith("/pos/v1/");
    }

    private void handlerFailAuthenticate(HttpServletResponse response, CustomResponse customResponse) throws IOException {
        ResponseData responseData = ResponseData.error(customResponse);
        response.setContentType("application/json");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write(objectMapper.writeValueAsString(responseData));
    }

    private String getClientIp(HttpServletRequest request) {
        String xForwardedForHeader = request.getHeader("X-Forwarded-For");
        if (xForwardedForHeader == null) {
            return request.getRemoteAddr();
        }
        return xForwardedForHeader.split(",")[0].trim();
    }

}