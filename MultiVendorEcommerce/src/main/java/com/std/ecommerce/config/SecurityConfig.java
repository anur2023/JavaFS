package com.std.ecommerce.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.HttpMethod;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    // 🔐 Password Encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 🔐 Authentication Manager (required for future use)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // 🔥 Main Security Config
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // ❌ Disable CSRF (for API testing)
                .csrf(csrf -> csrf.disable())

                // 🔥 Make session stateless (JWT based)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 🔐 Authorization rules
                .authorizeHttpRequests(auth -> auth

                        // ✅ Public endpoints
                        .requestMatchers("/api/auth/register", "/api/auth/login").permitAll()

                        // ✅ Public category read (GET only)
                        .requestMatchers(HttpMethod.GET, "/api/categories", "/api/categories/**").permitAll()

                        // ✅ Admin-only category write operations
                        .requestMatchers("/api/categories/admin/**").hasRole("ADMIN")

                        // 🔥 Role-based endpoints
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/vendor/**").hasRole("VENDOR")
                        .requestMatchers("/api/customer/**").hasRole("CUSTOMER")

                        // ✅ NEW: Cart - any logged-in user can access
                        .requestMatchers("/api/cart/**").authenticated()

                        // 🔒 All others require authentication
                        .anyRequest().authenticated()
                )

                // 🔥 Add JWT filter before Spring Security filter
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}