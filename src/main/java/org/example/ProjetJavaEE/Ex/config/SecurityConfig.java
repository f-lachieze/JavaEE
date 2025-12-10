package org.example.ProjetJavaEE.Ex.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import org.example.ProjetJavaEE.Ex.service.JpaUserDetailsService;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // NÉCESSAIRE : Injection du service JPA (PAS DE PasswordEncoder ICI pour éviter la boucle)
    private final JpaUserDetailsService jpaUserDetailsService;

    // Constructeur : n'injecte plus PasswordEncoder pour éviter la dépendance circulaire
    public SecurityConfig(JpaUserDetailsService jpaUserDetailsService) {
        this.jpaUserDetailsService = jpaUserDetailsService;
    }

    // 1. Définition du Hachage de Mot de Passe
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2. Définition du UserDetailsService (Pointer vers l'implémentation JPA)
    @Bean
    public UserDetailsService userDetailsService() {
        return this.jpaUserDetailsService;
    }

    // 3. DÉFINITION EXPLICITE DU DAO AUTHENTICATION PROVIDER
    // ⬅️ CORRECTION : PasswordEncoder est injecté en paramètre (méthode préférée)
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(this.jpaUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    // 4. MISE À JOUR & FUSION DU FILTER CHAIN
    // ⬅️ CORRECTION : Le DaoAuthenticationProvider est injecté en paramètre
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, DaoAuthenticationProvider daoAuthenticationProvider) throws Exception {
        http
                // AJOUTÉ : Forcer l'utilisation du DaoAuthenticationProvider
                .authenticationProvider(daoAuthenticationProvider)

                .authorizeHttpRequests(auth -> auth

                        // RÈGLES PUBLIQUES (Lecture seule)
                        .requestMatchers(new AntPathRequestMatcher("/")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/campus/list")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/batiment/list")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/salle/list")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/register")).permitAll()

                        // RÈGLES PROFESSEUR
                        .requestMatchers(new AntPathRequestMatcher("/professor/timetable")).hasRole("PROFESSOR")

                        // RÈGLES GESTIONNAIRE (Lecture Avancée / Calculs / Réservation)
                        .requestMatchers(new AntPathRequestMatcher("/service/calculate", "POST")).hasRole("GESTIONNAIRE")
                        .requestMatchers(new AntPathRequestMatcher("/salle/edit", "GET")).hasRole("GESTIONNAIRE")
                        .requestMatchers(new AntPathRequestMatcher("/batiment/edit", "GET")).hasRole("GESTIONNAIRE")
                        .requestMatchers(new AntPathRequestMatcher("/reservation/new")).hasRole("GESTIONNAIRE")
                        .requestMatchers(new AntPathRequestMatcher("/reservation/save", "POST")).hasRole("GESTIONNAIRE")
                        .requestMatchers(new AntPathRequestMatcher("/reservation/listAll")).hasAnyRole("GESTIONNAIRE", "ADMIN")
                        .requestMatchers(new AntPathRequestMatcher("/reservation/delete")).hasAnyRole("GESTIONNAIRE", "ADMIN")


                        // RÈGLES ADMINISTRATEUR (Écriture / Suppression)
                        .requestMatchers(new AntPathRequestMatcher("/salle/new")).hasRole("ADMIN")
                        .requestMatchers(new AntPathRequestMatcher("/salle/save")).hasRole("ADMIN")
                        .requestMatchers(new AntPathRequestMatcher("/salle/delete")).hasRole("ADMIN")
                        .requestMatchers(new AntPathRequestMatcher("/batiment/new")).hasRole("ADMIN")
                        .requestMatchers(new AntPathRequestMatcher("/batiment/save")).hasRole("ADMIN")
                        .requestMatchers(new AntPathRequestMatcher("/batiment/delete")).hasRole("ADMIN")

                        .requestMatchers(new AntPathRequestMatcher("/campus/new")).hasRole("ADMIN")
                        .requestMatchers(new AntPathRequestMatcher("/campus/save")).hasRole("ADMIN")


                        .requestMatchers(new AntPathRequestMatcher("/service/capacity", "GET")).permitAll()
                        // RÈGLE GÉNÉRALE (Doit être la dernière)
                        .anyRequest().authenticated()
                )

                // Configuration du formulaire de connexion et du gestionnaire de succès
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(new CustomLoginSuccessHandler())
                        .permitAll()
                )

                // Configuration de la déconnexion
                .logout(logout -> logout
                        .permitAll()
                );

        return http.build();
    }
}