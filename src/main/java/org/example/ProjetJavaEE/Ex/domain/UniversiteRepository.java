package org.example.ProjetJavaEE.Ex.domain;

import org.example.ProjetJavaEE.Ex.modele.Universite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UniversiteRepository extends JpaRepository<Universite, String> {
    // Spring Data JPA va générer les méthodes save(), findById(), etc.
}