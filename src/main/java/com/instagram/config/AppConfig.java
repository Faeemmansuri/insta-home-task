package com.instagram.config;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.instagram.properties.FileUploadProperties;

@Configuration
@EnableJpaAuditing
@EntityScan(basePackages = "com.instagram.entity")
@EnableJpaRepositories(basePackages = "com.instagram.repository")
@EnableConfigurationProperties({
    FileUploadProperties.class
})
public class AppConfig {
	
	@Bean
    public AuditorAware<LocalDateTime> auditorAware() {
        return () -> Optional.of(LocalDateTime.now());
    }
	
}
