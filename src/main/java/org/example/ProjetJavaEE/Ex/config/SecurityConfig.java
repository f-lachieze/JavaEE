package org.example.ProjetJavaEE.Ex.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity // Active le support de la sécurité Web de Spring Security
public class SecurityConfig {

    // 1. Définition du Hachage de Mot de Passe
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2. Définition des Utilisateurs et Rôles (En mémoire pour le développement)
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {

        // Mot de passe crypté: "admin123"
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin123"))
                .roles("ADMIN", "GESTIONNAIRE") // L'admin a plusieurs rôles
                .build();

        // Mot de passe crypté: "gestion123"
        UserDetails gestionnaire = User.builder()
                .username("gest")
                .password(passwordEncoder.encode("gestion123"))
                .roles("GESTIONNAIRE")
                .build();

        return new InMemoryUserDetailsManager(admin, gestionnaire);
    }

    // 3. Définition des Règles d'Accès aux URL
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth

                        // Les pages de consultation (listCampus, listBatiments, listSalles) sont accessibles à tous (y compris ANONYME)
                        .requestMatchers(new AntPathRequestMatcher("/campus/list")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/batiment/list")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/salle/list")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/service/capacity")).permitAll()

                        // NOUVEAU : Autoriser l'accès à la racine (/) pour tous
                        .requestMatchers(new AntPathRequestMatcher("/")).permitAll()


                        // Les opérations de MODIFICATION/SUPPRESSION/AJOUT requièrent le rôle ADMIN
                        .requestMatchers(new AntPathRequestMatcher("/salle/new")).hasRole("ADMIN")
                        .requestMatchers(new AntPathRequestMatcher("/salle/save")).hasRole("ADMIN")
                        .requestMatchers(new AntPathRequestMatcher("/salle/edit")).hasRole("ADMIN")
                        .requestMatchers(new AntPathRequestMatcher("/salle/delete")).hasRole("ADMIN")
                        .requestMatchers(new AntPathRequestMatcher("/batiment/new")).hasRole("ADMIN")
                        .requestMatchers(new AntPathRequestMatcher("/batiment/save")).hasRole("ADMIN")

                        // Toutes les autres requêtes (y compris le reste du CRUD) requièrent l'authentification (rôle GESTIONNAIRE minimum)
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll() // Autorise l'accès à la page de login par tous
                )
                .logout(logout -> logout
                        .permitAll()
                );

        return http.build();
    }
}