package org.example.ProjetJavaEE.Ex.modele;

import jakarta.persistence.*;

@Entity
@Table(name = "SALLE")
public class Salle {

    // Clé Primaire (en gras) [cite: 25, 38]
    @Id
    @Column(name = "numSalle", unique = true, nullable = false)
    private String numSalle;

    @Column(name = "capacite", nullable = false)
    private int capacite;

    @Column(name = "accessibilite")
    private String accessibilite;

    @Column(name = "etage")
    private String etage;

    // Utilise l'énumération TypeSalle et la stocke comme String dans la BDD [cite: 27, 61]
    @Enumerated(EnumType.STRING)
    @Column(name = "typeS")
    private TypeSalle typeS;

    // RELATION AVEC BATIMENT : Plusieurs Salles dans Un Batiment (Many-to-One) [cite: 19]
    // La colonne de clé étrangère 'batiment' est dans la table SALLE [cite: 46, 47]
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batiment") // Le nom de la colonne de clé étrangère
    private Batiment building; // Nom de l'attribut dans l'UML [cite: 17]

    // Constructeur par défaut (requis par JPA) [cite: 62]
    public Salle() {}

    // ... (Ajouter Getters et Setters ici)
}