package com.agroconecta.agroconecta.config;

import com.agroconecta.agroconecta.auth.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // ===== ENDPOINTS PUBLICOS =====

                        // Auth endpoints (register, login, logout)
                        .requestMatchers("/auth/**").permitAll()

                        // Productos publicos (solo lectura)
                        .requestMatchers(HttpMethod.GET, "/api/v1/productos/detalle/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/productos/buscar").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/productos/filtro").permitAll()

                        // Metricas públicas del agricultor
                        .requestMatchers(HttpMethod.GET, "/api/v1/agricultor/*/metricas").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/agricultor/*/productos").permitAll()

                        // ===== ENDPOINTS PROTEGIDOS =====

                        // Usuarios autenticados
                        .requestMatchers(HttpMethod.GET, "/usuarios/me").authenticated()

                        // Productos (CRUD - requiere autenticación)
                        .requestMatchers(HttpMethod.POST, "/api/v1/productos").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/v1/productos").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/productos/eliminar/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/v1/productos/actualizar/**").authenticated()

                        // Favoritos (requiere autenticación)
                        .requestMatchers("/api/v1/favoritos/**").authenticated()

                        // Difusión (requiere autenticación - según Swagger no especifica rol)
                        .requestMatchers(HttpMethod.POST, "/api/v1/difusion/enviar").authenticated()

                        // Monetización (requiere autenticación)
                        .requestMatchers(HttpMethod.POST, "/api/v1/monetizacion/pago").authenticated()

                        // Administración (requiere rol ADMIN)
                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")

                        // Cualquier otro endpoint requiere autenticación por defecto
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}