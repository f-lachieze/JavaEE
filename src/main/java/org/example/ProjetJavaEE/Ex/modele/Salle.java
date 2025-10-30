package org.example.ProjetJavaEE.Ex.modele;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "SALLE")
public class Salle {

    // Clé Primaire
    @Id
    @Column(name = "numSalle", unique = true, nullable = false)
    private String numSalle;

    @Column(name = "capacite", nullable = false)
    private int capacite;

    @Column(name = "accessibilite")
    private String accessibilite;

    @Column(name = "etage")
    private String etage;

    // TypeSalle (Enum)
    @Enumerated(EnumType.STRING)
    @Column(name = "typeS")
    private TypeSalle typeS;

    // Relation Many-to-One avec Batiment
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batiment")
    private Batiment batiment;

    // Constructeur par défaut (requis par JPA)
    public Salle() {}

    // ------------------------------------------
    //             GETTERS et SETTERS
    // ------------------------------------------

    // numSalle (Clé Primaire)
    public String getNumSalle() {
        return numSalle;
    }

    public void setNumSalle(String numSalle) {
        this.numSalle = numSalle;
    }

    // Capacite
    public int getCapacite() {
        return capacite;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    // Accessibilite
    public String getAccessibilite() {
        return accessibilite;
    }

    public void setAccessibilite(String accessibilite) {
        this.accessibilite = accessibilite;
    }

    // Etage
    public String getEtage() {
        return etage;
    }

    public void setEtage(String etage) {
        this.etage = etage;
    }

    // TypeSalle
    public TypeSalle getTypeS() {
        return typeS;
    }



    public void setTypeS(TypeSalle typeS) {
        this.typeS = typeS;
    }

    // Batiment (Relation)
    public Batiment getBatiment() {
        return batiment;
    }

    public void setBatiment(Batiment batiment) {
        this.batiment = batiment;
    }
}