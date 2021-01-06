package com.augurit.common.config;

import com.augurit.common.xss.CrossFilter;
import com.augurit.common.xss.XssFilter;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.DispatcherType;

/**
 * Filter配置
 */
@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean xssFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setDispatcherTypes(DispatcherType.REQUEST);
        registration.setFilter(new XssFilter());
        registration.addUrlPatterns("/*");
        registration.setName("xssFilter");
        registration.setOrder(Integer.MAX_VALUE);
        return registration;
    }

    @Value("${cas.server-url-prefix}")
    private String casServerUrlPrefix;

    @Bean
    public FilterRegistrationBean logoutFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new SingleSignOutFilter());
        registration.addInitParameter("casServerUrlPrefix", casServerUrlPrefix);
        registration.addUrlPatterns("/*");
        registration.setName("logoutFilter");
        registration.setOrder(0);
        return registration;
    }

    @Bean
    public FilterRegistrationBean crossFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new CrossFilter());
        registration.addUrlPatterns("/*");
        registration.setName("crossFilter");
        registration.setOrder(1);
        return registration;
    }
}
