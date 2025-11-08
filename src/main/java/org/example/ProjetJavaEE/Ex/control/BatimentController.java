package org.example.ProjetJavaEE.Ex.control;

import org.example.ProjetJavaEE.Ex.modele.Batiment;
import org.example.ProjetJavaEE.Ex.modele.Campus;
import org.example.ProjetJavaEE.Ex.service.GestionCampusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/batiment")
public class BatimentController {

    @Autowired
    private GestionCampusService gcs;

    /**
     * Mappe l'URL /batiment/list pour afficher tous les bâtiments d'un campus donné.
     * @param nomCampus Le nom du campus passé en paramètre d'URL (ex: Triolet)
     */


    @GetMapping("/list")
    public String listBatiments(@RequestParam("campusId") String nomCampus, Model model) {

        try {
            // 1. Récupération du Campus et de la liste de ses bâtiments
            Campus campus = gcs.findCampusWithBatiments(nomCampus);

            // NOUVEAU : Map pour stocker CodeB -> Capacité Totale
            Map<String, Long> capaciteParBatiment = new HashMap<>();

            for (Batiment batiment : campus.getBatiments()) {
                // Calcule la capacité pour chaque bâtiment
                Long capacite = gcs.calculerCapaciteTotaleBatiment(batiment.getCodeB());
                capaciteParBatiment.put(batiment.getCodeB(), capacite);
            }

            // NOUVEAU : Calculer la Capacité Totale du Campus
            Long capaciteCampus = gcs.calculerCapaciteTotaleCampus(nomCampus);

            // 2. Ajout des données au Modèle
            model.addAttribute("campus", campus); // Ajout du campus complet
            model.addAttribute("batimentList", campus.getBatiments()); // Ajout des bâtiments pour la boucle
            model.addAttribute("capaciteMap", capaciteParBatiment);

            // NOUVEAU : Ajouter la capacité totale du campus au modèle
            model.addAttribute("totalCampusCapacity", capaciteCampus);
            // 3. Retourne le nom du template
            return "batiment/listBatiments";

        } catch (IllegalArgumentException e) {
            // Gère le cas où le campus n'est pas trouvé
            model.addAttribute("errorMessage", e.getMessage());
            return "error/404";
        }
    }
}