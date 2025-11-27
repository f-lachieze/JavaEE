package org.example.ProjetJavaEE.Ex.modele;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "RESERVATION")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "prof_username", nullable = false)
    private String professeurUsername; // Nom de l'utilisateur (Professeur)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "num_salle", nullable = false)
    private Salle salle; // La salle réservée

    @Column(name = "date_debut", nullable = false)
    private LocalDateTime dateDebut;

    @Column(name = "date_fin", nullable = false)
    private LocalDateTime dateFin;

    // Constructeur par défaut (obligatoire pour JPA)
    public Reservation() {}

    // --- Getters et Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProfesseurUsername() {
        return professeurUsername;
    }

    public void setProfesseurUsername(String professeurUsername) {
        this.professeurUsername = professeurUsername;
    }

    public Salle getSalle() {
        return salle;
    }

    public void setSalle(Salle salle) {
        this.salle = salle;
    }

    public LocalDateTime getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDateTime dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDateTime getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDateTime dateFin) {
        this.dateFin = dateFin;
    }
}