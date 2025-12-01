package org.example.ProjetJavaEE.Ex.domain;

import org.example.ProjetJavaEE.Ex.modele.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    // Méthode nécessaire pour récupérer l'objet rôle par son nom (Ex: "ROLE_PROFESSOR")
    Optional<Role> findByName(String name);
}