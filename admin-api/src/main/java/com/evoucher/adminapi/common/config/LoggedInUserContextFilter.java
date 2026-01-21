package com.evoucher.adminapi.common.config;

import com.evoucher.adminapi.auth.service.JwtService;
import com.evoucher.adminapi.auth.service.AdminService;
import com.evoucher.adminapi.auth.service.models.UserPrincipal;
import com.evoucher.adminapi.common.exception.GenericError;
import com.evoucher.adminapi.common.utils.Constant;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class LoggedInUserContextFilter extends OncePerRequestFilter {

  private static final String GENERIC_ERROR = "Oops! There was an error.";
  private static final String DATA_ACCESS_ERROR = "DataAccess error!" ;
  private static final String UNAUTHORIZED_USER_ERROR = "Unauthorized user access!" ;

  private final AdminService userService;
  private final JwtService jwtService;

  private final Logger LOGGER = LoggerFactory.getLogger(LoggedInUserContextFilter.class);

  @Override
  protected void doFilterInternal(@NotNull HttpServletRequest request,
                                  @NotNull HttpServletResponse response,
                                  @NotNull FilterChain filterChain)
          throws ServletException, IOException {
    try {
      // Not the case where the user is logged in
      if (!request.getRequestURI().equals("/auth/login")) {
//        LOGGER.info("Start authenticate user");
        String jwtToken = request.getHeader("Authorization");
        if (Objects.nonNull(jwtToken)) {
          String token = jwtToken.replace("Bearer ", "");
          String username = jwtService.genToken(token);
          if (Objects.nonNull(username)) {
            UserPrincipal user = (UserPrincipal) userService.loadUserByUsername(username);
            LoggedInUserContext.setLoggedInUser(user);
            LOGGER.info("User is authenticated");
          }
        }
      }
      filterChain.doFilter(request, response);
    } catch (DataAccessException e) {
      LOGGER.error("Database related error: {}", e.getMessage(), e);
      setResponseHandleUnauthorized(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, DATA_ACCESS_ERROR);
      return;
    } catch (Exception e) {
      LOGGER.error("Failed to authenticate user: {}", e.getMessage(), e);
      setResponseHandleUnauthorized(response, HttpServletResponse.SC_UNAUTHORIZED, UNAUTHORIZED_USER_ERROR );
      return;
    } finally {
      // Clear the SecurityContext to prevent memory leaks
      LoggedInUserContext.clearContext();
    }
  }

  private void setResponseHandleUnauthorized(
          HttpServletResponse response,
          int httpErrorValue,
          String errorMessage ) throws IOException {
    GenericError error = GenericError.builder()
            .timestamp(LocalDateTime.now(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")))
            .status(httpErrorValue)
            .errorCode(GENERIC_ERROR)
            .message(errorMessage)
            .build();

    response.setContentType("application/json");
    response.setStatus(httpErrorValue);
    response.getWriter().write(Constant.gson.toJson(error));
  }
}
