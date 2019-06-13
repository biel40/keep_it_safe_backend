package com.esliceu.keep_it_safe.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        corsRegistry.addMapping("/**")
                .allowCredentials(true)
                .exposedHeaders("token", "user")
                .allowedOrigins("http://localhost:8080")
                .allowedMethods("PUT", "GET", "POST", "DELETE", "OPTIONS");
    }
}
