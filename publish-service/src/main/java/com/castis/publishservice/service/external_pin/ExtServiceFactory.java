package com.castis.publishservice.service.external_pin;

import com.castis.publishservice.utils.enum_template.SystemType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExtServiceFactory {
    private static final List<SystemType> supportedTypes = List.of(SystemType.UR_BOX, SystemType.GIFTPOP, SystemType.EXTERNAL, SystemType.WATANE);
    private final ApplicationContext context;
    public IntegratedPinService getServiceByType(SystemType type) {
        return context.getBean(type.name(), IntegratedPinService.class);
    }
    public boolean isSupportType(SystemType type) {
        boolean isSupportType = supportedTypes.contains(type);
        log.info("External pin service {} contains {}", isSupportType ? "" : "doesn't", type);
        return isSupportType;
    }
}

