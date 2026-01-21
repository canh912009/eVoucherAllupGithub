package com.castis.publishservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:application-test.yml")
public class TestConfig {
    @Value("${e-voucher.url.service-be}")
    private String backendUrl;

    public String getBackendUrl() {
        return backendUrl;
    }
}
