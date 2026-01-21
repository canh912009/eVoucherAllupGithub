package com.evoucher.adminapi.common.config;

import com.evoucher.adminapi.common.exception.GenericError;
import com.evoucher.adminapi.common.utils.Constant;
import com.evoucher.adminapi.common.utils.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private static final String GENERIC_ERROR = "Oops! There was an error.";

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException ex)
            throws IOException, ServletException {
        log.error(ex.getMessage(), ex);
        setResponseHandleForbidden(response);
    }

    public static void setResponseHandleForbidden(HttpServletResponse response) throws IOException {
        GenericError error = GenericError.builder()
                .timestamp(LocalDateTime.now(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")))
                .status(HttpStatus.FORBIDDEN.value())
                .errorCode(GENERIC_ERROR)
                .message(MessageUtils.getMessage("evoucher.account.permission"))
                .build();

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write(Constant.gson.toJson(error));
    }
}
