package com.toy.diary.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        //임시로 크로스오리진 전체를 품
//        registry.addMapping("/**")
//                .allowedOrigins("http://localhost:8080");
//    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //임시로 크로스오리진 전체를 품
        registry.addMapping("/**").allowedOrigins("*").allowedMethods("GET", "POST","PUT", "DELETE");


    }
}
