package org.example.ProjetJavaEE.Ex.domain;

import org.example.ProjetJavaEE.Ex.modele.Composante;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComposanteRepository extends JpaRepository<Composante, String>{
    // Les méthodes de base (findAll, save, etc.) sont héritées.
}
