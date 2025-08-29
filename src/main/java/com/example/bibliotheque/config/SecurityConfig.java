package com.example.bibliotheque.config;

import com.example.bibliotheque.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // NOTE: Le @Autowired ici est optionnel dans les versions récentes de Spring
    // si vous utilisez un constructeur, mais laissons-le pour la clarté.
    // Assurez-vous d'avoir bien un UserService.
    // private final UserService userService;
    // public SecurityConfig(UserService userService) {
    //     this.userService = userService;
    // }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Cette méthode pour l'AuthenticationManager est pour les versions plus anciennes de Spring Boot.
    // Avec Spring Boot 3+, il est plus simple de ne pas le définir comme un bean explicite ici
    // car le SecurityFilterChain le configure déjà. Le code original était correct,
    // mais celui-ci est encore plus simple et moderne.

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. Désactiver la protection CSRF (standard pour les APIs REST stateless)
                .csrf(AbstractHttpConfigurer::disable)

                // 2. Définir les règles d'autorisation pour les requêtes HTTP
                .authorizeHttpRequests(authz -> authz
                        // RÈGLE CRUCIALE N°1 : Autoriser tout le monde à accéder à la console H2
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/auth/register").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .requestMatchers(HttpMethod.GET, "/books").permitAll()
                        .anyRequest().authenticated() // Toutes les autres requêtes nécessitent une authentification
                )

                // 3. Activer l'authentification HTTP Basic
                .httpBasic(withDefaults())

                // 4. S'assurer que l'application est "stateless" (pas de session)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // RÈGLE CRUCIALE N°2 : Autoriser l'affichage de la console H2 dans une frame
        http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()));

        return http.build();
    }
}