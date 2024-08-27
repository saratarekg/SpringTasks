package com.example.dependencyinjectiontask.Configurations;


import com.example.dependencyinjectiontask.Components.AdminFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {

    @Bean
    public FilterRegistrationBean<AdminFilter> adminFilterRegistrationBean() {
        FilterRegistrationBean<AdminFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(new AdminFilter());
        registrationBean.addUrlPatterns("/api/courses/add", "/api/courses/update/*", "/api/courses/delete/*"); // Apply filter only to specific URLs

        return registrationBean;
    }
}
