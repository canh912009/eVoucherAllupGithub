package com.evoucher.externalserviceapi.common.config;

import com.evoucher.externalserviceapi.common.model.LoggedInClient;
import com.evoucher.externalserviceapi.common.utils.ConstantUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Data
@EqualsAndHashCode(callSuper = false)
@Component
public class LoggedInClientContextFilter extends OncePerRequestFilter {

  private final Logger LOGGER = LoggerFactory.getLogger(LoggedInClientContextFilter.class);

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String token = request.getHeader(ConstantUtils.HEADER_AUTH_TOKEN);

    if (StringUtils.isNotBlank(token)) {
      LoggedInClientContext.setLoggedInClient(
          LoggedInClient.builder().token(token).build());
    }

    try {
      filterChain.doFilter(request, response);
    } finally {
      LoggedInClientContext.clear();
    }
  }

  @Override
  protected boolean isAsyncDispatch(final HttpServletRequest request) {
    return false;
  }

  @Override
  protected boolean shouldNotFilterErrorDispatch() {
    return false;
  }
}
