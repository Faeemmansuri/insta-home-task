//package com.instagram.config;
//
//import java.time.LocalDateTime;
//import java.util.Optional;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.domain.AuditorAware;
//import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
//
//@Configuration
//@EnableJpaAuditing
//public class JpaAuditing {
//    @Bean
//    public AuditorAware<LocalDateTime> auditorAware() {
//        return () -> Optional.of(LocalDateTime.now());
//    }
//}
//
