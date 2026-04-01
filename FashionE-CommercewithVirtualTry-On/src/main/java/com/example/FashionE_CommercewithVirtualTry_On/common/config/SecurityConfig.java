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
                // ❌ Disable CSRF (REST API)
                .csrf(csrf -> csrf.disable())

                // ❌ Stateless session (JWT)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 🔐 Authorization Rules
                .authorizeHttpRequests(auth -> auth

                        // 🔓 Public APIs
                        .requestMatchers("/api/auth/**").permitAll()

                        // 👤 CUSTOMER APIs
                        .requestMatchers("/api/user/**").hasRole("CUSTOMER")
                        .requestMatchers("/api/orders/**").hasRole("CUSTOMER")
                        .requestMatchers("/api/tryon/**").hasRole("CUSTOMER")

                        // 🛠️ ADMIN APIs
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/auth/admin/**").hasRole("ADMIN")

                        // 📖 Lookbook - customers browse published lookbooks
                        .requestMatchers("/api/user/lookbooks/**").hasRole("CUSTOMER")

                        // 📖 Lookbook - admin manages all lookbooks
                        .requestMatchers("/api/admin/lookbooks/**").hasRole("ADMIN")

                        // 📂 Static uploads (public image access)
                        .requestMatchers("/uploads/**").permitAll()

                        // 🔒 Everything else requires authentication
                        .anyRequest().authenticated()
                )

                // 🔐 JWT Filter
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}