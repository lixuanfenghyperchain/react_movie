package com.hyperchain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.Filter;

@SpringBootApplication
public class ReactMovieApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReactMovieApplication.class, args);
    }


    //运行跨域配置
    @Bean
    public Filter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.addExposedHeader("x-auth-token");
        config.addExposedHeader("x-total-count");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
