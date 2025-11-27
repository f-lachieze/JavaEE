package org.example.ProjetJavaEE.Ex.control;

import org.example.ProjetJavaEE.Ex.domain.SalleRepository;
import org.example.ProjetJavaEE.Ex.modele.Salle;
import org.example.ProjetJavaEE.Ex.service.GestionCampusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.userdetails.UserDetails;
// Potentiellement nécessaire pour les listes de rôles

import java.time.LocalDateTime;
import java.util.List;




@Controller
@RequestMapping("/reservation")
public class ReservationController {

    @Autowired
    private SalleRepository salleRepository; // Pour lister les salles

    // Injection de l'UserDetailsService pour récupérer la liste des professeurs
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private GestionCampusService gcs;

    /**
     * Mappe l'URL /reservation/new pour afficher le formulaire de réservation.
     */
    @GetMapping("/new")
    public String showReservationForm(@RequestParam(required = false) String numSalle, Model model) {

        // 1. Lister toutes les Salles
        List<Salle> allSalles = salleRepository.findAll();
        model.addAttribute("allSalles", allSalles);

        // 2. Lister les Professeurs (Simulation via InMemoryUserDetailsManager)
        List<String> professors = getProfessorUsernames();
        model.addAttribute("allProfessors", professors);

        // 3. Ajouter l'ID de la salle à pré-sélectionner au modèle
        model.addAttribute("selectedSalleId", numSalle); // ⬅️ NOUVEAU

        // 4. Modèle de données pour la soumission (Simplifié en String)
        model.addAttribute("reservationStatus", null);

        return "reservation/reservationForm";
    }

    /**
     * Traite la soumission du formulaire de réservation.
     */
    // Dans src/main/java/org/example/ProjetJavaEE/Ex/control/ReservationController.java

    @PostMapping("/save")
    public String saveReservation(
            @RequestParam String numSalle,
            @RequestParam String usernameProf,
            @RequestParam String dateHeureDebut,
            @RequestParam String dateHeureFin,
            Model model) {

        String message = null;
        String errorMessage = null;
        LocalDateTime debut;
        LocalDateTime fin;

        try {
            // 1. Conversion des chaînes en LocalDateTime
            debut = LocalDateTime.parse(dateHeureDebut);
            fin = LocalDateTime.parse(dateHeureFin);

            // 2. Appel du service de réservation -> C'est ICI que la donnée est sauvegardée en BDD
            // Le service gcs.reserverSalle s'occupe de la validation du créneau et de la persistance.
            gcs.reserverSalle(numSalle, usernameProf, debut, fin);

            message = "✅ RÉSERVATION ENREGISTRÉE : Salle " + numSalle +
                    " réservée pour " + usernameProf +
                    " (Du " + debut.toLocalDate() + " à " + debut.toLocalTime() +
                    " au " + fin.toLocalTime() + ").";

        } catch (java.lang.IllegalArgumentException e) { // Gère les conflits ou les dates invalides (service)
            errorMessage = "Erreur de Réservation : " + e.getMessage();
        } catch (java.time.format.DateTimeParseException e) { // Gère les erreurs de format de date (parsing)
            errorMessage = "Erreur de format de date/heure. Veuillez vérifier les champs.";
        } catch (Exception e) {
            // Gère les erreurs inattendues (ex: problème de connexion à la BDD)
            errorMessage = "Erreur système lors de l'enregistrement: " + e.getMessage();
        }

        // Repasser les listes et les messages au modèle pour réaffichage
        model.addAttribute("allSalles", salleRepository.findAll());
        model.addAttribute("allProfessors", getProfessorUsernames());

        // Repasser les valeurs pour que le formulaire soit pré-rempli en cas d'erreur
        model.addAttribute("dateHeureDebut", dateHeureDebut);
        model.addAttribute("dateHeureFin", dateHeureFin);

        // Affichage des messages
        model.addAttribute("reservationStatus", message);
        model.addAttribute("errorMessage", errorMessage);

        return "reservation/reservationForm";
    }


    // Méthode utilitaire pour extraire les noms d'utilisateur des professeurs
    private List<String> getProfessorUsernames() {

        // Crée une liste de tous les utilisateurs connus pour itération
        List<String> knownUsernames = List.of("admin", "gest", "prof");

        // La liste pour stocker les noms des professeurs
        List<String> professorUsernames = new java.util.ArrayList<>();

        // Le UserDetailsService doit exister, pas besoin de le vérifier
        // if (userDetailsService instanceof InMemoryUserDetailsManager manager) {

        for (String username : knownUsernames) {
            try {
                // UTILISATION DU SEUL APPEL PUBLIC DISPONIBLE
                UserDetails user = userDetailsService.loadUserByUsername(username);

                // Vérification si l'utilisateur possède le rôle PROFESSOR
                boolean isProfessor = user.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_PROFESSOR"));

                if (isProfessor) {
                    professorUsernames.add(user.getUsername());
                }

            } catch (org.springframework.security.core.userdetails.UsernameNotFoundException e) {
                // Ignore si l'utilisateur n'est pas trouvé (ne devrait pas arriver avec les noms codés en dur)
            }
        }
        // }
        // Si l'injection est un autre type que InMemoryUserDetailsManager, cela fonctionnera toujours
        // tant qu'il implémente UserDetailsService.
        return professorUsernames;
    }
}

