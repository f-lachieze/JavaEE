package org.example.ProjetJavaEE.Ex.service;

import org.example.ProjetJavaEE.Ex.domain.ProfesseurRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service // Indique que c'est un composant de service Spring
public class JpaUserDetailsService implements UserDetailsService {

    private final ProfesseurRepository professeurRepository;

    // Injection du Repository via le constructeur
    public JpaUserDetailsService(ProfesseurRepository professeurRepository) {
        this.professeurRepository = professeurRepository;
    }

    /**
     * Méthode principale de Spring Security pour charger l'utilisateur par son nom.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Utilise le Repository pour chercher le professeur en base de données
        return professeurRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé : " + username));

        // L'objet Professeur retourné implémente déjà UserDetails,
        // donc il est directement utilisable par Spring Security pour l'authentification.
    }
}