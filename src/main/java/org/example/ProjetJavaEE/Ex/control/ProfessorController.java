package org.example.ProjetJavaEE.Ex.control;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/professor")
public class ProfessorController {

    /**
     * Affiche l'emploi du temps personnalisé pour le professeur connecté.
     * Accessible uniquement avec le rôle 'PROFESSOR'.
     */
    @GetMapping("/timetable")
    public String viewTimetable(Model model) {

        // 1. Récupération du nom d'utilisateur connecté
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();

        model.addAttribute("username", currentUserName);

        // 2. Logique métier pour charger les données (à développer avec JPA)
        // Dans une future version avec JPA et des entités 'Cours'/'Réservation' :
        // List<Course> timetable = professorService.getTimetable(currentUserName);
        // model.addAttribute("timetable", timetable);

        return "professor/timetable";
    }
}