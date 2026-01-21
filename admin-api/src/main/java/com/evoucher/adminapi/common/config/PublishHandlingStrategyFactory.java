package com.evoucher.adminapi.common.config;

import com.evoucher.adminapi.admin.enums.SMSType;
import com.evoucher.adminapi.admin.service.publish.impl.PublishHandlingStrategy;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class PublishHandlingStrategyFactory {
    private final ApplicationContext context;

    public PublishHandlingStrategy getStrategy(SMSType type) throws IllegalArgumentException {
        String serviceName;
        switch (type) {
            case EMAIL:
                serviceName = "emailTypedPublish";
                break;
            case SMS:
                serviceName = "smsTypedPublish";
                break;
            case PAPER:
                serviceName = "paperTypedPublish";
                break;
            case ZALO:
                serviceName = "zaloTypedPublish";
                break;
            case DOWNLOAD:
                serviceName = "downloadTypedPublish";
                break;
            default:
                log.error("publish handling type : {} is not supported", type);
                throw new IllegalArgumentException("Can not find publish handling strategy " + type);

        }
        return context.getBean(serviceName, PublishHandlingStrategy.class);
    }
}
