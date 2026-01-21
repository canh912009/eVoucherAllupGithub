package com.castis.publishservice.config;

import com.castis.publishservice.entity.LoggedInClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Primary
@Service
@Slf4j
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        try {
            LoggedInClient loggedInClient = LoggedInClientContext.getLoggedInClient();
            return Optional.of(loggedInClient.getId());
        } catch (Exception ex) {
            return Optional.of("SYSTEM");
        }
    }
}
