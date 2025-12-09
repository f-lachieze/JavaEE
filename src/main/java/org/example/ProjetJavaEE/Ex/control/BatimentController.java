package org.example.ProjetJavaEE.Ex.control;

import org.example.ProjetJavaEE.Ex.domain.BatimentRepository;
import org.example.ProjetJavaEE.Ex.modele.Batiment;
import org.example.ProjetJavaEE.Ex.modele.Campus;
import org.example.ProjetJavaEE.Ex.domain.CampusRepository;
import org.example.ProjetJavaEE.Ex.service.GestionCampusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/batiment")
public class BatimentController {

    @Autowired
    private GestionCampusService gcs;

    @Autowired
    private BatimentRepository batimentRepository;

    @Autowired
    private CampusRepository campusRepository;

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

    /**
     * Affiche le formulaire d'ajout d'un nouveau bâtiment.
     */
    @GetMapping("/new")
    public String showAddBatimentForm(Model model) {

        // 1. Ajouter l'objet Batiment vide pour le formulaire
        model.addAttribute("batiment", new Batiment());

        // 2. Récupérer la liste des Campus pour la liste déroulante (FK)
        // Nous utilisons findAllCampus du service (ou campusRepository.findAll())
        model.addAttribute("allCampus", gcs.findAllCampus());

        return "batiment/addBatimentForm";
    }


    // Dans BatimentController.java

    /**
     * Traite la soumission du formulaire et sauvegarde le nouveau bâtiment.
     */
    @PostMapping("/save")
    public String saveBatiment(@ModelAttribute("batiment") Batiment batiment,
                               BindingResult result,
                               Model model) {

        // 1. Gestion des erreurs de validation (BindingResult)
        if (result.hasErrors()) {
            // En cas d'erreur, repasser la liste des campus et retourner au formulaire
            model.addAttribute("allCampus", gcs.findAllCampus());

            // Choisir le formulaire de retour approprié
            if (batiment.getCodeB() == null || batiment.getCodeB().isEmpty()) {
                return "batiment/newBatimentForm"; // Mode Création
            } else {
                return "batiment/editBatimentForm"; // Mode Édition
            }
        }
        // Supprimez le bloc 'else {}' qui causait le problème.
        // Le code continue ici seulement s'il n'y a PAS d'erreurs de validation.


        // 2. Enregistrement des données (try/catch)
        try {
            // Sauvegarde via le service
            gcs.saveBatiment(batiment);
        } catch (Exception e) {
            // Gestion simple des erreurs d'intégrité (ex: codeB déjà utilisé)
            model.addAttribute("errorMessage", "Erreur d'enregistrement : le code Bâtiment est peut-être déjà utilisé.");
            model.addAttribute("allCampus", gcs.findAllCampus());

            // Retourne le formulaire approprié en cas d'erreur
            if (batiment.getCodeB() == null || batiment.getCodeB().isEmpty()) {
                return "batiment/newBatimentForm";
            } else {
                return "batiment/editBatimentForm";
            }
        }

        // 3. Redirection après succès
        // Redirige vers la liste des bâtiments du Campus nouvellement créé ou modifié
        return "redirect:/batiment/list?campusId=" + batiment.getCampus().getNomC();
    }


    /**
     * Affiche le formulaire pour modifier un bâtiment existant.
     * Le formulaire sera pré-rempli avec les données du bâtiment récupéré.
     */
    @GetMapping("/edit")
    public String showEditBatimentForm(@RequestParam("codeBatiment") String codeBatiment, Model model) {

        // 1. Récupérer l'objet Batiment existant
        Batiment batiment = gcs.findBatimentByCode(codeBatiment);

        // 2. Ajouter les objets nécessaires au modèle
        model.addAttribute("batiment", batiment);
        model.addAttribute("allCampus", gcs.findAllCampus());

        // 3. Retourne le formulaire d'ajout/modification
        return "batiment/editBatimentForm";
    }


    /**
     * Supprime un bâtiment par son code.
     */
    @GetMapping("/delete")
    public String deleteBatiment(@RequestParam("codeBatiment") String codeBatiment, Model model) {

        try {
            // Optionnel: On récupère le nom du campus pour la redirection après suppression
            Campus campus = gcs.findBatimentByCode(codeBatiment).getCampus();
            String nomCampus = campus.getNomC();

            gcs.deleteBatiment(codeBatiment);

            // Redirige vers la liste des bâtiments du campus parent
            return "redirect:/batiment/list?campusId=" + nomCampus;

        } catch (Exception e) {
            // En cas d'erreur (ex: impossible de supprimer car des Salles sont encore liées sans CASCADE.REMOVE)
            model.addAttribute("errorMessage", "Erreur lors de la suppression du bâtiment : " + e.getMessage());
            return "error/404";
        }
    }



}