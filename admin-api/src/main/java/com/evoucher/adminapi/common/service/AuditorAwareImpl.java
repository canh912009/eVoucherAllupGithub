package com.evoucher.adminapi.common.service;

import com.evoucher.adminapi.auth.service.models.UserPrincipal;
import com.evoucher.adminapi.common.config.LoggedInUserContext;
import com.evoucher.adminapi.common.utils.Constant;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Primary
@Service
@Slf4j
public class AuditorAwareImpl implements AuditorAware<String> {

    @NotNull
    @Override
    public Optional<String> getCurrentAuditor() {
        try {
            UserPrincipal userPrincipal = LoggedInUserContext.getLoggedInUser();
            return Optional.of(userPrincipal.getId());
        } catch (Exception ex) {
            log.warn(ex.getMessage());
            return Optional.of(Constant.ANONYMOUS_USER);
        }
    }
}
