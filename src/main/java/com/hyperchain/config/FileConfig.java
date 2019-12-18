package com.hyperchain.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class FileConfig implements WebMvcConfigurer {
    @Value("${file.upload.location}")
    private String uploadPath;
    @Value("${file.map.location}")
    private String mapPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(mapPath).addResourceLocations("file:" + uploadPath);
    }
}