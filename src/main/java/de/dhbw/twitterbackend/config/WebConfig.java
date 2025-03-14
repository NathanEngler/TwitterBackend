package de.dhbw.twitterbackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Konfiguriere den Handler für statische Dateien im "uploads"-Verzeichnis --> Profilbild aktuell halten
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
}
