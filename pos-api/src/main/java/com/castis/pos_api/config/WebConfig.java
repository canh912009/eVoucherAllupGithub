package com.castis.pos_api.config;

import com.castis.pos_api.filter.AuthHeaderFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {

    @Bean
    public FilterRegistrationBean<AuthHeaderFilter> authHeaderFilterRegistration(AuthHeaderFilter authHeaderFilter) {
        FilterRegistrationBean<AuthHeaderFilter> registrationBean = new FilterRegistrationBean<>();
        
        registrationBean.setFilter(authHeaderFilter);
        registrationBean.addUrlPatterns("/pos/v1/*");
        
        return registrationBean;
    }
}