package org.example.ProjetJavaEE.Ex.control;

import org.example.ProjetJavaEE.Ex.domain.CampusRepository;
import org.example.ProjetJavaEE.Ex.domain.UniversiteRepository;
import org.example.ProjetJavaEE.Ex.modele.Campus;
import org.example.ProjetJavaEE.Ex.modele.Universite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.example.ProjetJavaEE.Ex.service.GestionCampusService; // ⬅️ Utiliser l'interface métier

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/campus") //  Ajout de l'URL de base pour la ressource Campus
public class CampusController {

    // 1. Injection du service par son interface
    @Autowired
    private GestionCampusService gcs;

    @Autowired
    private CampusRepository campusRepository;

    @Autowired
    private UniversiteRepository universiteRepository;

    /**
     * Mappe l'URL /campus/list pour afficher tous les campus.
     */
    @GetMapping("/list") //  L'URL finale sera /campus/list
    public String listCampus(Model model) {

        // Appel direct de la méthode définie
        List<Campus> campusList = gcs.findAllCampus();
        model.addAttribute("campusList", campusList);

        // 3. Retourne le chemin du template que nous avons créé
        return "campus/listCampus";
    }


    // --- 1. Affichage du Formulaire (GET /campus/new) ---
    @GetMapping("/new")
    public String showCampusForm(Model model) {
        // Charge la liste des universités existantes pour le champ de sélection
        List<Universite> allUniversities = universiteRepository.findAll();
        model.addAttribute("allUniversities", allUniversities);

        // Initialise un Campus vide pour l'objet de formulaire
        model.addAttribute("campus", new Campus());

        return "campus/campusForm";
    }

    // --- 2. Soumission du Formulaire (POST /campus/save) ---
    @PostMapping("/save")
    public String saveCampus(
            @RequestParam String nomC,
            @RequestParam String ville,
            @RequestParam String universiteAcronyme) { // Récupère l'acronyme de l'université sélectionnée

        // Recherche l'entité Universite complète par son PK (acronyme)
        Optional<Universite> universiteOptional = universiteRepository.findById(universiteAcronyme);

        if (universiteOptional.isEmpty()) {
            // Gérer l'erreur si l'université n'est pas trouvée (ne devrait pas arriver si le formulaire est bien rempli)
            // Pour l'instant, on lance une exception simple
            throw new IllegalArgumentException("Université parente non trouvée.");
        }

        // Création de l'entité Campus
        Campus newCampus = new Campus(nomC, ville, universiteOptional.get());

        // Sauvegarde dans la base de données
        campusRepository.save(newCampus);

        // Redirection vers la liste des campus
        return "redirect:/campus/list";
    }


    /**
     * Supprime un campus (Uniquement s'il est vide).
     */
    @GetMapping("/delete")
    public String deleteCampus(@RequestParam String nomC, Model model) {

        // 1. On récupère le campus avec ses bâtiments (grâce à notre requête LEFT JOIN corrigée !)
        Optional<Campus> campusOpt = campusRepository.findByNomCFetchBatiments(nomC);

        if (campusOpt.isPresent()) {
            Campus campus = campusOpt.get();

            // 2. VÉRIFICATION DE SÉCURITÉ
            if (!campus.getBatiments().isEmpty()) {
                // S'il y a des bâtiments, on refuse la suppression
                model.addAttribute("errorMessage", "Impossible de supprimer le campus " + nomC + " car il contient encore des bâtiments. Supprimez-les d'abord.");

                // On recharge la liste pour afficher l'erreur
                // Note : idéalement, on devrait rediriger avec un FlashAttribute,
                // mais pour faire simple, on peut renvoyer la vue liste.
                model.addAttribute("campusList", campusRepository.findAll());
                return "campus/listCampus";
            }

            // 3. Suppression si vide
            campusRepository.delete(campus);
        }

        return "redirect:/campus/list";
    }

    @GetMapping("/edit")
    public String showEditCampusForm(@RequestParam String nomC, Model model) {
        Optional<Campus> campus = campusRepository.findById(nomC);

        if (campus.isPresent()) {
            model.addAttribute("campus", campus.get());
            model.addAttribute("allUniversities", universiteRepository.findAll());
            return "campus/editCampusForm"; // Un nouveau template ou réutiliser l'ancien avec logique
        }
        return "redirect:/campus/list";
    }

    // Le constructeur est correct si vous voulez conserver le println pour le débogage
    public CampusController() {
        System.out.println("les campus ");
    }
}