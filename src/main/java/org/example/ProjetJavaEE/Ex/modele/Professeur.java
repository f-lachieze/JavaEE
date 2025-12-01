package org.example.ProjetJavaEE.Ex.modele;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "PROFESSEUR")
public class Professeur implements UserDetails { // ⬅️ Implémente UserDetails

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "nom", nullable = true)
    private String nom;

    // Many-to-Many
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "PROFESSEUR_ROLE_JOIN",
            joinColumns = @JoinColumn(name = "professeur_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>(); // Collection d'objets Role

    // Constructeur par défaut
    public Professeur() {}

    // Constructeur pour l'enregistrement
    public Professeur(String username, String password, String nom, Set<Role> roles) {
        this.username = username;
        this.password = password;
        this.nom = nom;
        this.roles = roles;
    }

    // --- Implémentation des méthodes UserDetails ---

    /** Définit les rôles/autorisations de l'utilisateur. */
    // --- Implémentation des méthodes UserDetails (MISE À JOUR) ---
    // --- Implémentation des méthodes UserDetails (MISE À JOUR) ---
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Mappe chaque objet Role vers une SimpleGrantedAuthority
        return this.roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName())) // ⬅️ Utilise role.getName()
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    // Par défaut, nous supposons que le compte est valide et ne nécessite pas d'expiration.
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // --- Getters et Setters pour les champs spécifiques ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoleName(String roleName) {
        this.roles = roles;
    }


}