package br.zzz.infrastructure.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    private final String allowedOrigins;

    public CorsConfig(
            @Value("${app.cors.allowed-origins:http://localhost:5173,http://localhost:3000}") final String allowedOrigins
    ) {
        this.allowedOrigins = allowedOrigins;
    }

    @Override
    public void addCorsMappings(final CorsRegistry registry) {
        final var origins = Arrays.stream(this.allowedOrigins.split(","))
                .map(String::trim)
                .filter(it -> !it.isBlank())
                .toArray(String[]::new);

        registry.addMapping("/**")
                .allowedOriginPatterns(origins)
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
