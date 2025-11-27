package org.example.ProjetJavaEE.Ex.control;

import org.example.ProjetJavaEE.Ex.modele.Reservation;
import org.example.ProjetJavaEE.Ex.service.GestionCampusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/professor")
public class ProfessorController {
    @Autowired
    private GestionCampusService gcs; // ⬅️ Injection du service


    /**
     * Affiche l'emploi du temps pour le professeur actuellement connecté.
     */
    @GetMapping("/timetable")
    public String showTimetable(Model model) {

        // 1. Récupérer l'utilisateur actuellement connecté
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        // 1. Appel du service pour récupérer les réservations réelles
        List<Reservation> reservations = gcs.findReservationsByProf(currentUsername);

        // 2. Ajouter les données au modèle (Simulation)
        // Dans une application réelle, une recherche en base serait effectuée ici
        // pour récupérer les réservations liées à ce professeur.
        model.addAttribute("username", currentUsername);
        model.addAttribute("reservations", reservations);



        // 3. Retourne le template
        return "professor/timetable";
    }
}