package org.example.ProjetJavaEE.Ex.domain;

import org.example.ProjetJavaEE.Ex.modele.Professeur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfesseurRepository extends JpaRepository<Professeur, Long> {

    /**
     * Méthode nécessaire pour l'implémentation de UserDetailsService.
     * Spring Data JPA génère la requête SELECT * FROM PROFESSEUR WHERE username = ?
     */
    Optional<Professeur> findByUsername(String username);
}