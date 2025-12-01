package org.example.ProjetJavaEE.Ex.config;

import org.example.ProjetJavaEE.Ex.domain.ProfesseurRepository;
import org.example.ProjetJavaEE.Ex.modele.Professeur;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import java.util.Set;
import org.example.ProjetJavaEE.Ex.domain.RoleRepository;
import org.example.ProjetJavaEE.Ex.modele.Role;

@Configuration
public class DataInitializer {

    @Bean
    @Transactional
    public CommandLineRunner initUsers(ProfesseurRepository professeurRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        return args -> {

            // 1. CRÉATION ET RÉCUPÉRATION DES RÔLES UNIQUES (Seulement s'ils n'existent pas)
            Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElseGet(() -> roleRepository.save(new Role("ROLE_ADMIN")));
            Role gestRole = roleRepository.findByName("ROLE_GESTIONNAIRE").orElseGet(() -> roleRepository.save(new Role("ROLE_GESTIONNAIRE")));
            Role profRole = roleRepository.findByName("ROLE_PROFESSOR").orElseGet(() -> roleRepository.save(new Role("ROLE_PROFESSOR")));

            // Si la base de données est vide, on ajoute les comptes de base
            if (professeurRepository.count() == 0) {

                // --- Création de l'Administrateur (ROLES DOUBLES) ---
                Professeur admin = new Professeur(
                        "admin",
                        passwordEncoder.encode("admin123"),
                        "Administrateur Principal",
                        // ⬅️ ATTRIBUTION DES DEUX RÔLES (ADMIN + GESTIONNAIRE)
                        Set.of(adminRole, gestRole)
                );
                professeurRepository.save(admin);
                System.out.println("Compte 'admin' initialisé (Rôles : ADMIN, GESTIONNAIRE).");

                // --- Création du Gestionnaire ---
                Professeur gest = new Professeur(
                        "gest",
                        passwordEncoder.encode("gestion123"), // Mdp : gestion123
                        "Gestionnaire Courant",
                        Set.of(gestRole) // Rôle du gestionnaire
                );
                professeurRepository.save(gest);
                System.out.println("Compte 'gest' initialisé (MDP: gestion123).");

                // --- Création du Professeur de Test ---
                Professeur profTest = new Professeur(
                        "prof",
                        passwordEncoder.encode("prof123"), // Mdp : prof123
                        "Professeur Test",
                        Set.of(profRole) // Rôle du professeur
                );
                professeurRepository.save(profTest);
                System.out.println("Compte 'prof' initialisé (MDP: prof123).");
            }
        };
    }
}