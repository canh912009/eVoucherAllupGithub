package com.evoucher.evoucherbe.config;

import com.evoucher.evoucherbe.common.models.LoggedInClient;
import com.evoucher.evoucherbe.utils.Constant;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.AuditorAware;
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
            LoggedInClient loggedInClient = LoggedInClientContext.getLoggedInClient();
            return Optional.of(loggedInClient.getId());
        } catch (Exception ex) {
            log.warn("logged in client is null: {}", ex.getMessage());
            return Optional.of(Constant.SYSTEM);
        }
    }
}
