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
@EnableWebSecurity
public class SecurityConfig {

    // 1. Définition du Hachage de Mot de Passe
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2. Définition des Utilisateurs et Rôles (En mémoire)
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

        // NOUVEAU: Rôle Professeur
        UserDetails professor = User.builder()
                .username("prof")
                .password(passwordEncoder.encode("prof123"))
                .roles("PROFESSOR")
                .build();

        return new InMemoryUserDetailsManager(admin, gestionnaire, professor); // Ajout du professeur
    }

    // 3. Définition des Règles d'Accès aux URL
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth

                        // ----------------------------------------------------
                        // RÈGLES PUBLIQUES (ANONYME, GESTIONNAIRE, ADMIN, PROFESSOR)
                        // Les pages de consultation de base sont accessibles à tous
                        // ----------------------------------------------------
                        .requestMatchers(new AntPathRequestMatcher("/")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/campus/list")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/batiment/list")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/salle/list")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/service/capacity", "GET")).permitAll()
                        // NOUVEAU : Permettre l'accès au formulaire et au traitement d'inscription
                        .requestMatchers(new AntPathRequestMatcher("/register")).permitAll()

                        // ----------------------------------------------------
                        // RÈGLES PROFESSEUR
                        // ----------------------------------------------------
                                .requestMatchers(new AntPathRequestMatcher("/professor/timetable")).hasRole("PROFESSOR")

                        // ----------------------------------------------------
                        // RÈGLES GESTIONNAIRE (ACTIONS SPÉCIFIQUES & LECTURE AVANCÉE)
                        // ----------------------------------------------------
                        // Le GESTIONNAIRE peut utiliser les outils de calcul métier (POST)
                        .requestMatchers(new AntPathRequestMatcher("/service/calculate", "POST")).hasRole("GESTIONNAIRE")

                        // Le GESTIONNAIRE peut visualiser les formulaires (EDIT/NEW GET) pour la lecture avancée ou la préparation,
                        // mais ne peut pas sauvegarder ou créer (POST/DELETE).
                        // Ceci est important pour la future fonctionnalité de réservation.
                        .requestMatchers(new AntPathRequestMatcher("/salle/edit", "GET")).hasRole("GESTIONNAIRE")
                        .requestMatchers(new AntPathRequestMatcher("/batiment/edit", "GET")).hasRole("GESTIONNAIRE")


                        // ----------------------------------------------------
                        // RÈGLES ADMINISTRATEUR (CRUD ÉCRITURE/SUPPRESSION)
                        // Le GESTIONNAIRE ne peut PAS créer, ni modifier la base.
                        // ----------------------------------------------------
                        .requestMatchers(new AntPathRequestMatcher("/salle/new")).hasRole("ADMIN") // GET et POST par défaut
                        .requestMatchers(new AntPathRequestMatcher("/salle/save")).hasRole("ADMIN")
                        .requestMatchers(new AntPathRequestMatcher("/salle/delete")).hasRole("ADMIN")

                        .requestMatchers(new AntPathRequestMatcher("/batiment/new")).hasRole("ADMIN")
                        .requestMatchers(new AntPathRequestMatcher("/batiment/save")).hasRole("ADMIN")
                        .requestMatchers(new AntPathRequestMatcher("/batiment/delete")).hasRole("ADMIN")

                        // ----------------------------------------------------
                        // RÈGLES GÉNÉRALES (PROFESSOR et autres futurs rôles)
                        // ----------------------------------------------------
                        // Toutes les autres requêtes nécessitent au moins d'être authentifié.
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()
                )
                .logout(logout -> logout
                        .permitAll()
                );

        return http.build();
    }
}