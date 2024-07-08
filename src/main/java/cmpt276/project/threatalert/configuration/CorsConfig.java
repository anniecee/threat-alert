package cmpt276.project.threatalert.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")  // Restrict CORS configuration to specific endpoints, e.g., "/api/**"
                .allowedOrigins("*")  // Allow requests from all origins, you can restrict to specific domains: e.g., "https://example.com"
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // Allow specific HTTP methods
                .allowedHeaders("*");  // Allow all headers
    }
}