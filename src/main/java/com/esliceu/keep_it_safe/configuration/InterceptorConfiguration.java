package com.esliceu.keep_it_safe.configuration;

import com.esliceu.keep_it_safe.filters.TokenInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@EnableWebMvc
public class InterceptorConfiguration implements WebMvcConfigurer {

    @Bean
    public TokenInterceptor getInterceptor() {
        return new TokenInterceptor();
    }

    @Override
    @Order(1)
    public void addInterceptors (InterceptorRegistry registry) {
        registry.addInterceptor(getInterceptor()).excludePathPatterns("/login/local", "/user", "/oAuth/google", "/forwardLoginGoogle", "/token/verify");
    }

    @Override
    @Order(0)
    public void addCorsMappings(CorsRegistry corsRegistry){
        corsRegistry.addMapping("/**")
            .allowedOrigins("http://localhost:8080")
            .allowedMethods("PUT", "GET", "POST", "DELETE", "OPTIONS");
    }
}