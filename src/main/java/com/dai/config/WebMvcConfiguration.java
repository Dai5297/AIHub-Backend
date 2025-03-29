package com.dai.config;

import com.dai.intercept.UserTokenIntercept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Autowired
    private UserTokenIntercept userTokenIntercept;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userTokenIntercept)
                .addPathPatterns("/**")
                .excludePathPatterns("/login");
    }
}
