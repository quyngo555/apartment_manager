package com.vmo.apartment_manager.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry
        .addMapping("/**")
//        .allowedOriginPatterns("*")
        .allowedMethods("GET", "POST", "PUT", "DELETE");
//        .allowCredentials(false).maxAge(3600);
  }
}
