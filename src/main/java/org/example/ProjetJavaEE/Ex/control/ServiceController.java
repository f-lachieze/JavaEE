package org.example.ProjetJavaEE.Ex.control;

import org.example.ProjetJavaEE.Ex.domain.BatimentRepository;
import org.example.ProjetJavaEE.Ex.domain.CampusRepository;
import org.example.ProjetJavaEE.Ex.service.GestionCampusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/service")
public class ServiceController {

    @Autowired
    private GestionCampusService gcs;

    @Autowired
    private BatimentRepository batimentRepository;

    @Autowired
    private CampusRepository campusRepository;

    /**
     * Mappe l'URL /service/capacity (GET) pour afficher le formulaire de calculs (Q5, Q6).
     */
    @GetMapping("/capacity")
    public String showCapacityForm(Model model) {

        model.addAttribute("allBatiments", batimentRepository.findAll());
        // Utiliser findAll() sur le Repository est acceptable ici pour les listes de base.
        model.addAttribute("allCampus", campusRepository.findAll());

        // Initialiser la taille du groupe et les résultats à zéro avant le premier calcul
        model.addAttribute("tailleGroupe", 30);
        model.addAttribute("resultQ5Batiment", null); // Utiliser null pour masquer le résultat non calculé
        model.addAttribute("resultQ5Campus", null);
        model.addAttribute("resultQ6Batiment", null);
        model.addAttribute("resultQ6Campus", null);

        return "service/capacityForm";
    }

    /**
     * Traite la soumission du formulaire de calculs (POST) pour les Questions 5 et 6.
     * La logique est basée sur targetType, mais lit les IDs spécifiques (batimentId, campusId).
     */
    @PostMapping("/calculate")
    public String calculateCapacity(
            @RequestParam(required = false) String targetType,
            // Lecture des IDs des SELECT box (peut être null ou "")
            @RequestParam(required = false) String batimentId,
            @RequestParam(required = false) String campusId,
            @RequestParam(defaultValue = "30") int tailleGroupe,
            Model model) {

        // Repasser les listes et la tailleGroupe au modèle
        model.addAttribute("allBatiments", batimentRepository.findAll());
        model.addAttribute("allCampus", campusRepository.findAll());
        model.addAttribute("tailleGroupe", tailleGroupe);

        // Assurer que la taille du groupe est valide
        if (tailleGroupe < 1) {
            model.addAttribute("errorMessage", "La taille du groupe doit être supérieure à zéro.");
            return "service/capacityForm";
        }

        // --- LOGIQUE DE CALCUL BASEE SUR LE TYPE ET L'ID DISPONIBLE ---

        if ("batiment".equals(targetType) && batimentId != null && !batimentId.isEmpty()) {
            model.addAttribute("resultQ5Batiment", gcs.calculerNombreGroupesParBatiment(batimentId, tailleGroupe));
            model.addAttribute("resultQ6Batiment", gcs.calculerNbGroupesAmphiOuTdParBatiment(batimentId, tailleGroupe));
            model.addAttribute("targetLabel", "Bâtiment: " + batimentId);

        } else if ("campus".equals(targetType) && campusId != null && !campusId.isEmpty()) {
            model.addAttribute("resultQ5Campus", gcs.calculerNombreGroupesParCampus(campusId, tailleGroupe));
            model.addAttribute("resultQ6Campus", gcs.calculerNbGroupesAmphiOuTdParCampus(campusId, tailleGroupe));
            model.addAttribute("targetLabel", "Campus: " + campusId);

        } else {
            model.addAttribute("errorMessage", "Veuillez sélectionner une cible (Bâtiment OU Campus) pour le calcul.");
        }

        return "service/capacityForm";
    }
}