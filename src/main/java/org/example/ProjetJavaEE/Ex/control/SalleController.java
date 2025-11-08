package org.example.ProjetJavaEE.Ex.control;

import org.example.ProjetJavaEE.Ex.modele.Batiment;
import org.example.ProjetJavaEE.Ex.service.GestionCampusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/salle")
public class SalleController {

    @Autowired
    private GestionCampusService gcs;

    /**
     * Mappe l'URL /salle/list pour afficher toutes les salles d'un bâtiment donné.
     * @param codeBatiment Le code du bâtiment passé en paramètre d'URL (ex: triolet_b36)
     */
    @GetMapping("/list")
    public String listSalles(@RequestParam("batimentId") String codeBatiment, Model model) {

        try {
            // 1. Récupération du Bâtiment et de la liste de ses salles
            Batiment batiment = gcs.findBatimentWithSalles(codeBatiment);

            // 2. Ajout des données au Modèle
            model.addAttribute("batiment", batiment); // Le bâtiment complet
            model.addAttribute("salleList", batiment.getSalles()); // La liste des salles pour la vue

            // 3. Retourne le nom du template
            return "salle/listSalles";

        } catch (IllegalArgumentException e) {
            // Gère le cas où le bâtiment n'est pas trouvé
            model.addAttribute("errorMessage", e.getMessage());
            return "error/404"; // Utilise le template d'erreur que vous avez créé
        }
    }
}