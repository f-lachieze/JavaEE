package org.example.ProjetJavaEE.Ex.modele;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "UNIVERSITE")
public class Universite {

    @Id
    @Column(name = "acronyme", unique = true, nullable = false)
    private String acronyme;

    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "creation")
    private int creation;

    @Column(name = "presidence")
    private String presidence;

    // Relation 1-N : Une université a plusieurs campus
    @OneToMany(
            mappedBy = "universite", // 'universite' est le nom du champ dans l'entité Campus
            cascade = CascadeType.ALL, // Si on supprime l'université, on supprime ses campus
            fetch = FetchType.LAZY
    )
    private Set<Campus> campus = new HashSet<>();

    // Constructeur par défaut
    public Universite() {}

    // Getters et Setters
    public String getAcronyme() {
        return acronyme;
    }

    public void setAcronyme(String acronyme) {
        this.acronyme = acronyme;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getCreation() {
        return creation;
    }

    public void setCreation(int creation) {
        this.creation = creation;
    }

    public String getPresidence() {
        return presidence;
    }

    public void setPresidence(String presidence) {
        this.presidence = presidence;
    }

    public Set<Campus> getCampus() {
        return campus;
    }

    public void setCampus(Set<Campus> campus) {
        this.campus = campus;
    }
}