package org.example.ProjetJavaEE.Ex.control;

import org.example.ProjetJavaEE.Ex.domain.ProfesseurRepository;
import org.example.ProjetJavaEE.Ex.domain.SalleRepository;
import org.example.ProjetJavaEE.Ex.modele.Professeur;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


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

    @Autowired
    private ProfesseurRepository professeurRepository;

    /**
     * Mappe l'URL /reservation/new pour afficher le formulaire de réservation.
     */
    // Dans ReservationController.java

    @GetMapping("/new")
    public String showReservationForm(@RequestParam(required = false) String numSalle, Model model) {

        // 1. Lister toutes les Salles
        List<Salle> allSalles = salleRepository.findAll();
        model.addAttribute("allSalles", allSalles);

        // ⬅️ CORRECTION : Lister TOUS les utilisateurs stockés dans la table PROFESSEUR
        List<Professeur> allUsers = professeurRepository.findAll();
        model.addAttribute("allUsers", allUsers);

        // 3. Ajouter l'ID de la salle à pré-sélectionner au modèle
        model.addAttribute("selectedSalleId", numSalle);

        // 4. Modèle de données pour la soumission (Simplifié en String)
        model.addAttribute("reservationStatus", null);

        // Suppression de l'appel à getProfessorUsernames()

        return "reservation/reservationForm";
    }

    /**
     * Traite la soumission du formulaire de réservation.
     */
    // Dans src/main/java/org/example/ProjetJavaEE/Ex/control/ReservationController.java

    // Dans src/main/java/org/example/ProjetJavaEE/Ex/control/ReservationController.java

    @PostMapping("/save")
    public String saveReservation(
            @RequestParam String numSalle,
            @RequestParam String usernameProf,
            @RequestParam String dateHeureDebut,
            @RequestParam String dateHeureFin,
            RedirectAttributes redirectAttributes, // ⬅️ Pour les messages de SUCCÈS
            Model model) {

        String errorMessage = null;
        LocalDateTime debut;
        LocalDateTime fin;

        try {
            // 1. Conversion et Validation des Dates
            debut = LocalDateTime.parse(dateHeureDebut);
            fin = LocalDateTime.parse(dateHeureFin);

            // 2. Appel du service de réservation (Validation et Sauvegarde en BDD)
            gcs.reserverSalle(numSalle, usernameProf, debut, fin);

            // --- CAS DE SUCCÈS ---
            String message = "✅ RÉSERVATION ENREGISTRÉE : Salle " + numSalle +
                    " réservée pour " + usernameProf +
                    " (Du " + debut.toLocalDate() + " à " + debut.toLocalTime() +
                    " au " + fin.toLocalTime() + ").";

            // Stocke le message dans l'attribut flash pour l'affichage après redirection
            redirectAttributes.addFlashAttribute("reservationStatus", message);

            // Redirection vers le formulaire propre (PRG)
            return "redirect:/reservation/new";

        } catch (java.lang.IllegalArgumentException | java.time.format.DateTimeParseException e) {
            // --- CAS D'ÉCHEC (Conflit ou Date Invalide) ---
            errorMessage = "Erreur de Réservation : " + e.getMessage();
        } catch (Exception e) {
            // --- CAS D'ERREUR SYSTÈME ---
            errorMessage = "Erreur système lors de l'enregistrement: " + e.getMessage();
        }

        // --- LOGIQUE DE REVENIR AU FORMULAIRE EN CAS D'ÉCHEC ---

        // Repasser les listes nécessaires pour que la vue s'affiche correctement
        model.addAttribute("allSalles", salleRepository.findAll());
        // NOTE: allProfessors devrait être remplacé par allUsers/findAll()
        model.addAttribute("allProfessors", getProfessorUsernames());

        // Repasser les valeurs pour que le formulaire soit pré-rempli
        model.addAttribute("dateHeureDebut", dateHeureDebut);
        model.addAttribute("dateHeureFin", dateHeureFin);

        // Passe le message d'erreur pour l'affichage
        model.addAttribute("errorMessage", errorMessage);

        // Retourne la vue (l'attribut 'reservationStatus' sera null, masquant le message de succès)
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

