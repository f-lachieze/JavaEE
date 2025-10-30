package org.example.ProjetJavaEE.Ex.modele;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "COMPOSANTE")
public class Composante {

    // Clé Primaire (en gras) [cite: 21, 38]
    @Id
    @Column(name = "acronyme", unique = true, nullable = false)
    private String acronyme;

    @Column(name = "nom")
    private String nom;

    @Column(name = "responsable")
    private String responsable;

    // RELATION AVEC BATIMENT : Many-to-Many via la table d'association 'Exploite' [cite: 6, 48]
    @ManyToMany
    @JoinTable(
            name = "EXPLOITE", // Nom de la table d'association [cite: 48]
            joinColumns = @JoinColumn(name = "team"), // Clé de Composante dans la table 'Exploite' [cite: 49]
            inverseJoinColumns = @JoinColumn(name = "building") // Clé de Batiment dans la table 'Exploite' [cite: 49]
    )
    private Set<Batiment> exploite = new HashSet<>();

    // Constructeur par défaut (requis par JPA) [cite: 62]
    public Composante() {}


// --- GETTERS et SETTERS ---

    // Acronyme (Clé Primaire)
    public String getAcronyme() {
        return acronyme;
    }

    public void setAcronyme(String acronyme) {
        this.acronyme = acronyme;
    }

    // Nom
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    // Responsable
    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    // Exploite (Relation Many-to-Many)
    public Set<Batiment> getExploite() {
        return exploite;
    }

    public void setExploite(Set<Batiment> exploite) {
        this.exploite = exploite;
    }
}