package com.augurit.common.config;

import net.unicon.cas.client.configuration.CasClientConfigurerAdapter;
import net.unicon.cas.client.configuration.EnableCasClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class CasConfig extends CasClientConfigurerAdapter {

    @Value("${cas-ignore-pattern}")
    private String ignorePattern;

    @Override
    public void configureAuthenticationFilter(FilterRegistrationBean authenticationFilter) {
        authenticationFilter.getInitParameters().put("ignorePattern",ignorePattern);
    }
}