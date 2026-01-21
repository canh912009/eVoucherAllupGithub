package com.evoucher.evoucherbe.config;

import com.evoucher.evoucherbe.common.models.LoggedInClient;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
@Slf4j
public class LoggedInClientContextFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String value = request.getHeader("ClientId");

    if (StringUtils.isNotBlank(value)) {
      LoggedInClientContext.setLoggedInClient(
              LoggedInClient.builder().id(value).build());
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
