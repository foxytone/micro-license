package org.neat0n.licensingservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebConfig {
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http)
            throws Exception {
        return http
                .authorizeRequests(
                )
                .anyRequest()
                .permitAll()
                .and()
                .formLogin()
                .and()
                .csrf()
                .disable()
                .build();
    }
}
