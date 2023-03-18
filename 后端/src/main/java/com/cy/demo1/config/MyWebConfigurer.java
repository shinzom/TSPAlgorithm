package com.cy.demo1.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author 怯桦
 * @Date 2022-04-27 18:03
 * @Desc  跨域问题配置类
 * @Version 1.0
 */
@SpringBootConfiguration
public class MyWebConfigurer implements WebMvcConfigurer {
    /**
     * 所有请求都允许跨域，使用这种配置就不需要在interceptor中配置header了
     */
    @Override
    public void addCorsMappings(CorsRegistry corsRegistry){
        corsRegistry.addMapping("/**")
                .allowCredentials(true)
                .allowedOrigins("http://127.0.0.1:5173")
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
                .allowedHeaders("*")
                .maxAge(3600);
    }
}

