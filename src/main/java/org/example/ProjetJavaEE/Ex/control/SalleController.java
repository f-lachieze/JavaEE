package org.example.ProjetJavaEE.Ex.control;

import org.example.ProjetJavaEE.Ex.modele.Batiment;
import org.example.ProjetJavaEE.Ex.modele.Salle;
import org.example.ProjetJavaEE.Ex.modele.TypeSalle;
import org.example.ProjetJavaEE.Ex.service.GestionCampusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
            Batiment batiment = gcs.getById(codeBatiment).get();

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

    /**
     * (Post)
     * Mappe l'URL /salle/new?batimentId=... pour afficher le formulaire d'ajout.
     */
    @GetMapping("/new")
    public String showAddSalleForm(@RequestParam("batimentId") String codeBatiment, Model model) {

        // 1. Récupérer le Bâtiment parent pour la liaison
        // Note: Nous utilisons findBatimentWithSalles car le Bâtiment doit exister.
        Batiment batiment = gcs.findBatimentByCode(codeBatiment);

        // 2. Ajouter les objets nécessaires au modèle
        model.addAttribute("salle", new Salle()); // Crée un objet Salle vide pour le formulaire
        model.addAttribute("batiment", batiment); // Passe l'objet Bâtiment pour l'ID et l'affichage

        // 3. Passe le TypeSalle ENUM pour la liste déroulante
        model.addAttribute("typesSalle", TypeSalle.values());

        return "salle/addSalleForm";

    }

    // Dans SalleController.java

    /**
     * (POST)
     * Traite la soumission du formulaire et sauvegarde la nouvelle salle.
     */
    /**
     * (POST)
     * Traite la soumission du formulaire et sauvegarde la nouvelle salle.
     */
    @PostMapping("/save")
    public String saveSalle(@ModelAttribute("salle") Salle salle,
                            BindingResult result,
                            @RequestParam("batimentId") String codeBatiment,
                            Model model) {

        // 1. Rattacher manuellement le Bâtiment (clé étrangère)
        Batiment batiment = gcs.findBatimentByCode(codeBatiment);
        salle.setBatiment(batiment);

        // Détermine si nous sommes en mode édition (pour le retour en cas d'erreur)
        boolean isEditMode = (salle.getNumSalle() != null && !salle.getNumSalle().isEmpty());
        String formTemplate = isEditMode ? "salle/editSalleForm" : "salle/addSalleForm";


        if (result.hasErrors()) {
            // En cas d'erreur de validation, retourne au formulaire approprié
            model.addAttribute("batiment", batiment);
            model.addAttribute("typesSalle", TypeSalle.values());
            return formTemplate;
        }

        try {
            // 2. Sauvegarde via le service
            gcs.saveSalle(salle);
        } catch (Exception e) {
            // Gestion des erreurs d'intégrité (ex: numéro de salle déjà utilisé)
            model.addAttribute("errorMessage", "Erreur d'enregistrement : le numéro de Salle est peut-être déjà utilisé.");
            model.addAttribute("batiment", batiment);
            model.addAttribute("typesSalle", TypeSalle.values());
            return formTemplate; // Retourne le formulaire approprié en cas d'erreur
        }

        // 3. Redirige vers la liste des salles du bâtiment
        return "redirect:/salle/list?batimentId=" + codeBatiment;
    }



    /**
     * Affiche le formulaire de modification d'une salle existante.
     * @param numSalle L'ID de la salle à éditer.
     */
    @GetMapping("/edit")
    public String showEditSalleForm(@RequestParam("numSalle") String numSalle, Model model) {

        // 1. Récupérer l'objet Salle existant
        Salle salle = gcs.findSalleByNumSalle(numSalle); // ⬅️ Méthode de service à créer

        // 2. Ajouter les objets nécessaires au modèle
        model.addAttribute("salle", salle);
        model.addAttribute("batiment", salle.getBatiment());
        model.addAttribute("typesSalle", TypeSalle.values());

        // 3. Réutiliser le même formulaire d'ajout pour la modification
        return "salle/editSalleForm"; // Le formulaire sera pré-rempli par Thymeleaf
    }

// Dans SalleController.java

    /**
     * Supprime une salle par son ID et redirige.
     * @param numSalle L'ID de la salle à supprimer.
     */
    @GetMapping("/delete")
    public String deleteSalle(@RequestParam("numSalle") String numSalle,
                              @RequestParam("batimentId") String codeBatiment) {

        gcs.deleteSalle(numSalle); // ⬅️ Méthode de service à créer

        // Redirige vers la liste des salles du bâtiment
        return "redirect:/salle/list?batimentId=" + codeBatiment;
    }

}