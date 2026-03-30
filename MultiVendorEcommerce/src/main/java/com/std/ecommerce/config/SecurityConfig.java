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

    // 🔐 Authentication Manager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // 🔥 Main Security Config
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // ❌ Disable CSRF (REST API - stateless)
                .csrf(csrf -> csrf.disable())

                // 🔥 Stateless session (JWT based)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .authorizeHttpRequests(auth -> auth

                        // =========================================
                        // ✅ PUBLIC ENDPOINTS (no token required)
                        // =========================================

                        // Auth
                        .requestMatchers("/api/auth/register", "/api/auth/login").permitAll()

                        // Public product browsing
                        .requestMatchers(HttpMethod.GET, "/api/products", "/api/products/**").permitAll()

                        // Public category browsing
                        .requestMatchers(HttpMethod.GET, "/api/categories", "/api/categories/**").permitAll()

                        // Public vendor browsing
                        .requestMatchers(HttpMethod.GET, "/api/vendors", "/api/vendors/**").permitAll()

                        // =========================================
                        // 🔐 ADMIN-ONLY ENDPOINTS
                        // =========================================

                        // Admin role-based prefix
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // Category management (Admin)
                        .requestMatchers("/api/categories/admin/**").hasRole("ADMIN")

                        // Vendor deletion (Admin)
                        .requestMatchers("/api/vendors/admin/**").hasRole("ADMIN")

                        // View all orders (Admin)
                        .requestMatchers("/api/orders/admin/**").hasRole("ADMIN")

                        // =========================================
                        // 🏪 VENDOR-ONLY ENDPOINTS
                        // =========================================

                        // Vendor role-based prefix
                        .requestMatchers("/api/vendor/**").hasRole("VENDOR")

                        // Vendor profile management (only vendor can register/update their store)
                        .requestMatchers(HttpMethod.POST, "/api/vendors/register").hasRole("VENDOR")
                        .requestMatchers(HttpMethod.PUT, "/api/vendors/me").hasRole("VENDOR")
                        .requestMatchers(HttpMethod.GET, "/api/vendors/me").hasRole("VENDOR")

                        // View items sold (Vendor)
                        .requestMatchers("/api/orders/vendor/**").hasRole("VENDOR")

                        // Product management (Vendor manages their own products)
                        .requestMatchers(HttpMethod.POST, "/api/products").hasRole("VENDOR")
                        .requestMatchers(HttpMethod.PUT, "/api/products/**").hasRole("VENDOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/products/**").hasRole("VENDOR")

                        // =========================================
                        // 🛒 CUSTOMER-ONLY ENDPOINTS
                        // =========================================

                        .requestMatchers("/api/customer/**").hasRole("CUSTOMER")

                        // =========================================
                        // 🔒 AUTHENTICATED ENDPOINTS (any role)
                        // =========================================

                        // Cart (any logged-in user)
                        .requestMatchers("/api/cart/**").authenticated()

                        // Orders (place, view own, cancel own)
                        .requestMatchers("/api/orders/**").authenticated()

                        // Payments (initiate, view own)
                        .requestMatchers("/api/payments/**").authenticated()

                        // =========================================
                        // 🔒 CATCH-ALL
                        // =========================================
                        .anyRequest().authenticated()
                )

                // 🔥 JWT filter runs before Spring's auth filter
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}