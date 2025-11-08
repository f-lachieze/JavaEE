package org.example.ProjetJavaEE.Ex.control;

import org.example.ProjetJavaEE.Ex.modele.Campus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.example.ProjetJavaEE.Ex.service.GestionCampusService; // ⬅️ Utiliser l'interface métier

import java.util.List;

@Controller
@RequestMapping("/campus") // ⬅️ Ajout de l'URL de base pour la ressource Campus
public class CampusController {

    // 1. Injection du service par son interface
    @Autowired
    private GestionCampusService gcs; // ⬅️ Renommer l'objet injecté (ou le laisser 'cs' si vous préférez)

    /**
     * Mappe l'URL /campus/list pour afficher tous les campus.
     */
    @GetMapping("/list") // ⬅️ L'URL finale sera /campus/list
    public String listCampus(Model model) {

        // Appel direct de la méthode définie
        List<Campus> campusList = gcs.findAllCampus();
        model.addAttribute("campusList", campusList);

        // 3. Retourne le chemin du template que nous avons créé
        return "campus/listCampus";
    }

    // Le constructeur est correct si vous voulez conserver le println pour le débogage
    public CampusController() {
        System.out.println("les campus ");
    }
}