package com.pranavrajkota.library2026.security;

import com.pranavrajkota.library2026.entity.User;
import com.pranavrajkota.library2026.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
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

    private final JwtAuthFilter jwtAuthFilter;
    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter, CustomUserDetailsService customUserDetailsService) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public org.springframework.security.authentication.dao.DaoAuthenticationProvider authenticationProvider() {
        org.springframework.security.authentication.dao.DaoAuthenticationProvider authProvider =
                new org.springframework.security.authentication.dao.DaoAuthenticationProvider();

        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth

                        // public endpoints
                        .requestMatchers("/auth/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()


                        // only ADMIN
                        .requestMatchers("/books/page", "/books/search").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/books/my-books").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/books/borrow/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/books/return/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/books", "/books/**").hasRole("ADMIN")

                        // ADMIN only
                        .requestMatchers("/books/admin/**").hasRole("ADMIN")

                        // last
                        .requestMatchers("/books/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                );


        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // This hashes passwords so they aren't readable in the database
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    CommandLineRunner init(UserRepository repo, PasswordEncoder encoder) {
        return args -> {
            if (repo.findByUserName("admin").isEmpty()) {
                User user = new User();
                user.setUserName("admin");
                user.setPassword(encoder.encode("admin"));
                user.setRole("ADMIN");
                repo.save(user);
            }
        };
    }
}
