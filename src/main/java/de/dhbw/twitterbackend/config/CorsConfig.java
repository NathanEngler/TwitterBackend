package de.dhbw.twitterbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Erlaube alle Endpunkte
                        .allowedOrigins("http://localhost:4200") // Erlaube Anfragen vom Frontend
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Erlaube HTTP-Methoden
                        .allowedHeaders("*") // Erlaube alle Header
                        .allowCredentials(true); // Erlaube Cookies und Authentifizierungsheader
            }
        };
    }
}