package com.example.FashionE_CommercewithVirtualTry_On.common.config;

import com.example.FashionE_CommercewithVirtualTry_On.common.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    // 🔐 Password Encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 🔐 Security Configuration
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // ❌ Disable CSRF (for REST APIs)
                .csrf(csrf -> csrf.disable())

                // ❌ Stateless session (JWT)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 🔐 Authorization Rules
                .authorizeHttpRequests(auth -> auth

                        // 🔓 Public APIs (ONLY these)
                        .requestMatchers(
                                "/api/auth/register",
                                "/api/auth/login",
                                "/api/auth/create-admin" // ⚠️ REMOVE in production
                        ).permitAll()

                        // 🛠️ ADMIN APIs
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // 👤 CUSTOMER APIs (optional, if you use this path)
                        .requestMatchers("/api/user/**").hasRole("CUSTOMER")

                        // 🔒 All other APIs require authentication
                        .anyRequest().authenticated()
                )

                // 🔐 Add JWT Filter
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}