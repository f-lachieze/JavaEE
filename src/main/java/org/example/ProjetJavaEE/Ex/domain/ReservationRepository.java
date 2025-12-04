package org.example.ProjetJavaEE.Ex.domain;

import org.example.ProjetJavaEE.Ex.modele.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    /**
     * Vérifie les conflits de chevauchement de créneaux horaires pour une salle donnée.
     * La requête cherche toute réservation existante (r) dont la date de début ou de fin
     * tombe dans le nouvel intervalle [nouvelleDateDebut, nouvelleDateFin], OU si
     * la réservation existante contient entièrement le nouvel intervalle.
     */
    @Query("SELECT r FROM Reservation r WHERE r.salle.numSalle = :numSalle " +
            "AND (" +
            "  (r.dateDebut < :dateFin AND r.dateFin > :dateDebut)" + // Chevauchement standard
            ")")
    List<Reservation> findConflictingReservations(
            @Param("numSalle") String numSalle,
            @Param("dateDebut") LocalDateTime dateDebut,
            @Param("dateFin") LocalDateTime dateFin);

    /**
     * Récupère toutes les réservations pour un professeur donné (utilisé pour l'emploi du temps).
     */
    List<Reservation> findByProfesseurUsernameOrderByDateDebut(String professeurUsername);


    /**
     * Filtre les réservations par nom d'utilisateur, numéro de salle et date (début du jour).
     */
    @Query("SELECT r FROM Reservation r " +
            "WHERE (:username IS NULL OR r.professeurUsername = :username) " +
            "AND (:numSalle IS NULL OR r.salle.numSalle = :numSalle) " +
            "AND (:startDate IS NULL OR (r.dateDebut >= :startDate AND r.dateDebut < :endDate)) " +
            "ORDER BY r.dateDebut")
    List<Reservation> findByFilters(
            @Param("username") String username,
            @Param("numSalle") String numSalle,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );


}