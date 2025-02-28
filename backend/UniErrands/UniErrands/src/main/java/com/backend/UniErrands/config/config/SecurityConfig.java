package com.backend.UniErrands.config.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.context.annotation.Bean;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())  // Disable CSRF if needed
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()  // Allow all requests
            )
            .httpBasic(withDefaults());  // Optional: Remove if you don't need authentication
        return http.build();
    }
}
