package com.castis.publishservice.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.listener.ConditionalRejectingErrorHandler;
import org.springframework.messaging.converter.MessageConversionException;

@Slf4j
public class CustomExceptionStrategy
        extends ConditionalRejectingErrorHandler.DefaultExceptionStrategy {

    @Override
    public boolean isFatal(Throwable throwable) {
        if (throwable.getCause() != null) {
//            if (throwable.getCause() instanceof MessageConversionException) {
//
//            }
            log.error(throwable.getMessage(), throwable);
            log.error(throwable.getCause().getMessage(), throwable.getCause());
            return true;
        }
        return false;
    }
}
