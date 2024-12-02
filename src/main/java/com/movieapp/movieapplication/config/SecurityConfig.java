package com.movieapp.movieapplication.config;

import com.movieapp.movieapplication.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Configuration
public class SecurityConfig {

    private final JwtUtil jwtUtil;

    public SecurityConfig(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {



        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Publiczne endpointy dostępne bez uwierzytelnienia 46 zmiana linijki
                        .requestMatchers("/api/users/register", "/api/users/login", "/swagger-ui/", "v3/api-docs/", "/api/movies/export", "/api/movies/import").permitAll()

                        // Endpoints dla użytkownika z rolą USER
                        .requestMatchers(HttpMethod.GET, "/api/movies/**").hasAuthority("ROLE_USER")
                        .requestMatchers(HttpMethod.POST, "/api/reviews").hasAuthority("ROLE_USER")
                        .requestMatchers(HttpMethod.PUT, "/api/users/{userId}/watched/**", "/api/users/{userId}/favorite/**").hasAuthority("ROLE_USER")

                        // Endpoints dla administratora (ROLE_ADMIN)
                        .requestMatchers(HttpMethod.POST, "/api/movies", "/api/categories", "/api/languages").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/movies/**", "/api/categories/**", "/api/languages/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/movies/**", "/api/categories/**", "/api/languages/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/api/users/admin").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/api/users/get-all").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/users/{id}").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/users/{id}").hasAuthority("ROLE_ADMIN")

                        // Endpoints recenzji dla administratorów
                        .requestMatchers(HttpMethod.PUT, "/api/reviews/{id}").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/reviews/{id}").hasAuthority("ROLE_ADMIN")


                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtAuthorizationFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    public static class JwtAuthorizationFilter extends OncePerRequestFilter {

        private final JwtUtil jwtUtil;

        public JwtAuthorizationFilter(JwtUtil jwtUtil) {
            this.jwtUtil = jwtUtil;
        }

        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
            System.out.println("Request URL: " + request.getRequestURI()); // Diagnostyka

            String header = request.getHeader("Authorization");
            if (header != null && header.startsWith("Bearer ")) {
                String token = header.substring(7);
                if (jwtUtil.validateToken(token)) {
                    String role = jwtUtil.extractRole(token);
                    SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);
                    var authentication = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                            null, null, Collections.singletonList(authority)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
            chain.doFilter(request, response);
        }
    }
}
